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
    private static FirebaseDatabase mDatabase;
    public static DatabaseReference reference;
    private static MainActivity mainActivity;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

    public static void getReference(FirebaseUser user) {
        reference = FirebaseHelper.getDatabase().getReference().child("users").child(user.getUid()).child("games");
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
                //addGame(changingGame);
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

    private static void changeGame(Game game) {
        try {
            if (game.id == -1) game.id = (new Date()).getTime();
            HelperFactory.getHelper().getGameDAO().writeGameToDB(game);
            HelperFactory.getHelper().getInvestigatorDAO().deleteInvestigatorsByGameID(game.id);
            for (Investigator investigator : game.invList) {
                if (investigator != null) {
                    investigator.gameId = game.id;
                    investigator.id = (new Date()).getTime();
                    HelperFactory.getHelper().getInvestigatorDAO().create(investigator);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mainActivity.initGameList();
    }

    public static void setMainActivity(MainActivity mainActivity) {
        FirebaseHelper.mainActivity = mainActivity;
    }

    public static void addGame(Game game) {
        if (reference != null) reference.child(String.valueOf(game.id)).setValue(game);
    }

    public static void removeGame(Game game) throws NullPointerException {
        if (reference != null) reference.child(String.valueOf(game.id)).removeValue();
    }

}
