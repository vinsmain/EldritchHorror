package ru.mgusev.eldritchhorror.database;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.SQLException;
import java.util.Date;

import ru.mgusev.eldritchhorror.activity.MainActivity;
import ru.mgusev.eldritchhorror.model.Game;
import ru.mgusev.eldritchhorror.model.Investigator;

public class FirebaseHelper {
    private static FirebaseHelper helper;
    private static FirebaseDatabase mDatabase;
    private static DatabaseReference reference;
    private MainActivity mainActivity;

    public static FirebaseHelper getInstance() {
        if (helper == null) {
            helper = new FirebaseHelper();
        }
        initDatabase();
        return helper;
    }

    public static void initDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
    }

    public void getReference(FirebaseUser user) {
        reference = mDatabase.getReference().child("users").child(user.getUid()).child("games");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Game addingGame = dataSnapshot.getValue(Game.class);
                changeGame(addingGame);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Game changingGame = dataSnapshot.getValue(Game.class);
                changeGame(changingGame);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Game deletingGame = dataSnapshot.getValue(Game.class);
                try {
                    if (HelperFactory.getHelper().getGameDAO().hasGame(deletingGame)) {
                        try {
                            HelperFactory.getHelper().getGameDAO().delete(deletingGame);
                            HelperFactory.getHelper().getInvestigatorDAO().deleteInvestigatorsByGameID(deletingGame.id);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        mainActivity.initGameList();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void changeGame(Game game) {
        if (game.id == -1) game.id = (new Date()).getTime();
        try {
            if (!(HelperFactory.getHelper().getGameDAO().hasGame(game) && game.lastModified == HelperFactory.getHelper().getGameDAO().getGameByID(game).lastModified) ) {
                HelperFactory.getHelper().getGameDAO().writeGameToDB(game);
                HelperFactory.getHelper().getInvestigatorDAO().deleteInvestigatorsByGameID(game.id);
                for (Investigator investigator : game.invList) {
                    if (investigator != null) {
                        investigator.gameId = game.id;
                        investigator.id = (new Date()).getTime();
                        HelperFactory.getHelper().getInvestigatorDAO().create(investigator);
                    }
                }
                mainActivity.initGameList();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public static void addGame(Game game) {
        if (reference != null) reference.child(String.valueOf(game.id)).setValue(game);
    }

    public static void removeGame(Game game) throws NullPointerException {
        if (reference != null) reference.child(String.valueOf(game.id)).removeValue();
    }
}