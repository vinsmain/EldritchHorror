package ru.mgusev.eldritchhorror.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.adapter.RVAdapter;
import ru.mgusev.eldritchhorror.database.FirebaseHelper;
import ru.mgusev.eldritchhorror.database.HelperFactory;
import ru.mgusev.eldritchhorror.eh_interface.OnItemClicked;
import ru.mgusev.eldritchhorror.fragment.DonateDialogFragment;
import ru.mgusev.eldritchhorror.fragment.RateDialogFragment;
import ru.mgusev.eldritchhorror.fragment.SignOutDialogFragment;
import ru.mgusev.eldritchhorror.model.Game;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnItemClicked {

    public static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    private static final String TAG = "FIREBASE";

    private static final int SORT_MODE_DATE_DOWN = 1;
    private static final int SORT_MODE_DATE_UP = 2;
    private static final int SORT_MODE_ANCIENT_ONE = 3;
    private static final int SORT_MODE_SCORE_DOWN = 4;
    private static final int SORT_MODE_SCORE_UP = 5;

    private int columnsCount = 1;
    private int currentSortMode = SORT_MODE_DATE_UP;
    private MenuItem sortItem;
    private MenuItem authItem;

    private List<Game> gameList;
    private RecyclerView recyclerView;
    private RecyclerView.OnScrollListener onScrollListener;
    private RVAdapter adapter;
    private Game deletingGame;
    private FloatingActionButton addPartyButton;
    private TextView messageStarting;
    private String bestScoreValue = "";
    private String worstScoreValue = "";

    private TextView gamesCount;
    private TextView bestScore;
    private TextView worstScore;

    private AlertDialog alert;
    private boolean isAlert;
    private boolean isAdvertisingDialog;

    private DonateDialogFragment donateDialog;
    private RateDialogFragment rateDialog;
    private SignOutDialogFragment signOutDialog;
    private PrefHelper prefHelper;

    private AdColonyHelper helper;
    private AdColonyTask task;
    private boolean isLock;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser currentUser;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseHelper fbHelper;
    private int RC_SIGN_IN = 100;
    private UserPhoto userPhoto;
    private boolean isConnecting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefHelper = new PrefHelper(this);
        currentSortMode = prefHelper.loadSortMode();

        fbHelper = FirebaseHelper.getInstance();
        fbHelper.setMainActivity(this);
        userPhoto = UserPhoto.initUserPhoto();
        userPhoto.setMain(this);

        initDonateDialog();

        if (savedInstanceState!= null) {
            gameList = savedInstanceState.getParcelableArrayList("gameList");
            deletingGame = savedInstanceState.getParcelable("deletingGame");
            isAlert = savedInstanceState.getBoolean("DIALOG");
            isAdvertisingDialog = savedInstanceState.getBoolean("DIALOG_ADVERTISING");
            isLock = savedInstanceState.getBoolean("LOCK");
            isConnecting = savedInstanceState.getBoolean("CONNECTING");
        }

        System.out.println("onCreate " + isConnecting);

        helper = AdColonyHelper.getInstance(this);
        if (!isLock && !helper.isAdvertisingLoad()) {
            task = new AdColonyTask();
            task.execute();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            setTitle(R.string.main_header);
        }

        addPartyButton = (FloatingActionButton) findViewById(R.id.addPartyButton);
        addPartyButton.setOnClickListener(this);

        gamesCount = (TextView) findViewById(R.id.gamesCount);
        bestScore = (TextView) findViewById(R.id.bestScore);
        worstScore = (TextView) findViewById(R.id.worstScore);
        messageStarting = (TextView) findViewById(R.id.message_starting);
        recyclerView = (RecyclerView) findViewById(R.id.gameList);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) columnsCount = 2;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, columnsCount);
        //LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        if (gameList == null) initGameList();

        adapter = new RVAdapter(this.getApplicationContext(), gameList);
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);

        initRVListener();
        recyclerView.addOnScrollListener(onScrollListener);

        showMessageStarting();

        if (getIntent().getBooleanExtra("refreshGameList", false)) initGameList();
        if (isAlert) deleteDialog();
        if (isAdvertisingDialog) showDonateDialog();

        if (prefHelper.isRate()) initRateDialog();
        //initSignOutDialog();


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    // User is signed in
                    fbHelper.getReference(currentUser);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + currentUser.getUid());

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) Log.d(TAG, "CURRENT USER: signed_in: " + currentUser.getUid());
        if (currentUser != null) {
            setPhoto();
            fbHelper.getReference(currentUser);
            initGameList();
        }
        System.out.println("onStart " + isConnecting);
        if (isConnecting) signIn();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOut() {
        mAuth.signOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        authItem.setIcon(R.drawable.google_icon);
                        currentUser = null;
                        userPhoto.clearPhoto();
                        isConnecting = false;
                    }
                });
        try {
            HelperFactory.getHelper().getGameDAO().clearTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initGameList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            isConnecting = true;
            System.out.println("onActivityResult " + isConnecting);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        if (authItem != null) authItem.setIcon(R.drawable.google_signed_in);

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            currentUser = mAuth.getCurrentUser();
                            setPhoto();
                            fbHelper.getReference(currentUser);
                            isConnecting = false;
                            initGameList();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            currentUser = null;
                            authItem.setIcon(R.drawable.google_icon);
                            isConnecting = false;
                            Toast.makeText(getApplicationContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void setPhoto() {
        Drawable icon = userPhoto.getPhoto(currentUser);
        if (authItem != null && icon != null) authItem.setIcon(icon);
    }

    private void initDonateDialog() {
        donateDialog = new DonateDialogFragment();
        donateDialog.setActivity(this);
        donateDialog.setCancelable(false);
    }

    private void initRateDialog() {
        rateDialog = new RateDialogFragment();
        rateDialog.setActivity(this);
        rateDialog.setCancelable(false);
        rateDialog.show(getSupportFragmentManager(), "RateDialogFragment");
    }

    private void initSignOutDialog() {
        signOutDialog = new SignOutDialogFragment();
        signOutDialog.setActivity(this);
        signOutDialog.show(getSupportFragmentManager(), "SignOutDialogFragment");
    }

    public void initGameList() {
        if (gameList == null) gameList = new ArrayList<>();
        try {
            gameList.clear();
            gameList.addAll(getSortedGames());
            for (int i = 0; i < gameList.size(); i ++) {
                gameList.get(i).invList = HelperFactory.getHelper().getInvestigatorDAO().getInvestigatorsListByGameID(gameList.get(i).id);
                if (currentUser != null && gameList.get(i).userID == null) {
                    Log.w(TAG, "addGame " + gameList.get(i).ancientOneID);
                    gameList.get(i).userID = currentUser.getUid();
                    gameList.get(i).lastModified = (new Date()).getTime();
                    FirebaseHelper.addGame(gameList.get(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            setScoreValues();
            showMessageStarting();
        }
    }

    private List<Game> getSortedGames() {
        try {
            if (currentSortMode == SORT_MODE_DATE_UP) return HelperFactory.getHelper().getGameDAO().getGamesSortDateUp();
            else if (currentSortMode == SORT_MODE_DATE_DOWN) return HelperFactory.getHelper().getGameDAO().getGamesSortDateDown();
            else if (currentSortMode == SORT_MODE_ANCIENT_ONE) return HelperFactory.getHelper().getGameDAO().getGamesSortAncientOne();
            else if (currentSortMode == SORT_MODE_SCORE_UP) return HelperFactory.getHelper().getGameDAO().getGamesSortScoreUp();
            else if (currentSortMode == SORT_MODE_SCORE_DOWN) return HelperFactory.getHelper().getGameDAO().getGamesSortScoreDown();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<Game>();
    }

    private void initRVListener() {
        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
                adapter.closeSwipeLayout();
            }

            @Override
            public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
                adapter.closeSwipeLayout();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        sortItem = menu.findItem(R.id.action_sort);
        authItem = menu.findItem(R.id.action_auth);
        if (currentUser == null) authItem.setIcon(R.drawable.google_icon);
        else setPhoto();
        setSortItemIcon();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                Intent intent = new Intent(this, DonateActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_auth:
                if (currentUser != null) showPopupMenu(findViewById(R.id.action_auth));
                else signIn();
                return true;
            case R.id.action_sort:
                if (currentSortMode != SORT_MODE_SCORE_UP) currentSortMode++;
                else currentSortMode = SORT_MODE_DATE_DOWN;
                prefHelper.saveSortMode(currentSortMode);
                setSortItemIcon();
                initGameList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setSortItemIcon() {
        if (currentSortMode == SORT_MODE_DATE_UP) {
            sortItem.setIcon(R.drawable.calendar_sort_up);
            sortItem.setTitle(R.string.sort_mode_message_2);
        } else if (currentSortMode == SORT_MODE_DATE_DOWN) {
            sortItem.setIcon(R.drawable.calendar_sort_down);
            sortItem.setTitle(R.string.sort_mode_message_1);
        } else if (currentSortMode == SORT_MODE_ANCIENT_ONE) {
            sortItem.setIcon(R.drawable.ancien_one_sort);
            sortItem.setTitle(R.string.sort_mode_message_3);
        } else if (currentSortMode == SORT_MODE_SCORE_UP) {
            sortItem.setIcon(R.drawable.score_sort_up);
            sortItem.setTitle(R.string.sort_mode_message_5);
        } else if (currentSortMode == SORT_MODE_SCORE_DOWN) {
            sortItem.setIcon(R.drawable.score_sort_down);
            sortItem.setTitle(R.string.sort_mode_message_4);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addPartyButton:
                if (helper.isAdvertisingLoad() && prefHelper.isAdvertisingShow() && adapter.getItemCount() >= 5) {
                    showDonateDialog();
                }
                else addGame();
                break;
            default:
                break;
        }
    }

    public void showDonateDialog() {
        isAdvertisingDialog = true;
        donateDialog.show(getSupportFragmentManager(), "DonateDialogFragment");
    }

    public void showAD() {
        try {
            helper.getAdc().show();
        } catch (NullPointerException e) {
            Log.d("Fail show video", "Get video again");
            task = new AdColonyTask();
            task.execute();
            addGame();
        }
    }

    public void addGame() {
        Intent intent = new Intent(this, GamesPagerActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        Intent intentGameDetails = new Intent(this, GameDetailsActivity.class);
        intentGameDetails.putExtra("game", gameList.get(position));
        startActivity(intentGameDetails);
    }

    @Override
    public void onEditClick(int position) {
        Intent intentEdit = new Intent(this, GamesPagerActivity.class);
        intentEdit.putExtra("editParty", gameList.get(position));
        startActivity(intentEdit);
    }

    @Override
    public void onDeleteClick(int position) {
        deletingGame = gameList.get(position);
        isAlert = true;
        deleteDialog();
    }

    private void deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.dialogAlert))
                .setMessage(getResources().getString(R.string.deleteDialogMessage))
                .setIcon(R.drawable.delete)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.messageOK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteParty(deletingGame);
                                dialog.cancel();
                                isAlert = false;
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.messageCancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                isAlert = false;
                            }
                        });
        alert = builder.create();
        alert.show();
    }

    public void deleteParty(Game deletingGame) {
        try {
            HelperFactory.getHelper().getGameDAO().delete(deletingGame);
            HelperFactory.getHelper().getInvestigatorDAO().deleteInvestigatorsByGameID(deletingGame.id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initGameList();
        Toast.makeText(this, R.string.success_deleting_message, Toast.LENGTH_SHORT).show();
        FirebaseHelper.removeGame(deletingGame);
    }

    public void setAdvertisingDialog(boolean advertisingDialog) {
        isAdvertisingDialog = advertisingDialog;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("DIALOG", isAlert);
        outState.putBoolean("DIALOG_ADVERTISING", isAdvertisingDialog);
        outState.putBoolean("LOCK", isLock);
        outState.putBoolean("CONNECTING", isConnecting);
        outState.putParcelableArrayList("gameList", (ArrayList<? extends Parcelable>) gameList);
        outState.putParcelable("deletingGame", deletingGame);
        if (alert != null) alert.cancel();
    }

    private void setScoreValues() {
        try {
            Game game = HelperFactory.getHelper().getGameDAO().getTopGameToSort(true);
            if (game != null) bestScoreValue = String.valueOf(game.score);
            else bestScoreValue = "";

            game = HelperFactory.getHelper().getGameDAO().getTopGameToSort(false);
            if (game != null) worstScoreValue = String.valueOf(game.score);
            else worstScoreValue = "";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        gamesCount.setText(String.valueOf(adapter.getItemCount()));
        bestScore.setText(bestScoreValue);
        worstScore.setText(worstScoreValue);
    }

    public int getGamesCount() {
        return adapter.getItemCount();
    }

    public PrefHelper getPrefHelper() {
        return prefHelper;
    }

    private void showMessageStarting() {
        if (adapter.getItemCount() == 0) messageStarting.setText(R.string.message_starting);
        else messageStarting.setText("");
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.menu_logout); // Для Android 4.0

        Object menuHelper;
        Class[] argTypes;
        try {
            Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
            fMenuHelper.setAccessible(true);
            menuHelper = fMenuHelper.get(popupMenu);
            argTypes = new Class[] { boolean.class };
            menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
        } catch (Exception e) {
            Log.w(TAG, "error forcing menu icons to show", e);
            popupMenu.show();
            return;
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Toast.makeText(PopupMenuDemoActivity.this,
                        // item.toString(), Toast.LENGTH_LONG).show();
                        // return true;
                        switch (item.getItemId()) {

                            case R.id.menu_sign_out:
                                signOut();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {

            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });
        popupMenu.show();
    }

    //AdColonyTask
    private class AdColonyTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            isLock = true;
            helper.startRequestInterstitial();
            int i = 0;
            while (!helper.isAdvertisingLoad() && !helper.isNotFilled() && i < 30) {
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            isLock = false;
        }
    }
}