package ru.mgusev.eldritchhorror.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.adcolony.sdk.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.adapter.RVAdapter;
import ru.mgusev.eldritchhorror.database.HelperFactory;
import ru.mgusev.eldritchhorror.eh_interface.OnItemClicked;
import ru.mgusev.eldritchhorror.model.Game;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnItemClicked {

    private final static String APP_ID = "appfe3bcc9bb3be4c1190";
    private final static String ZONE_ID = "vze74bd5b196ed4ff49c";
    final private String TAG = "AdColonyDemo";

    public static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    private int columnsCount = 1;

    private List<Game> gameList;
    RecyclerView recyclerView;
    RecyclerView.OnScrollListener onScrollListener;
    RVAdapter adapter;
    Game deletingGame;
    FloatingActionButton addPartyButton;
    String bestScoreValue = "";
    String worstScoreValue = "";

    TextView gamesCount;
    TextView bestScore;
    TextView worstScore;

    AlertDialog alert;
    boolean isAlert;

    private AdColonyInterstitial adc;
    private AdColonyInterstitialListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AdColonyAppOptions app_options = new AdColonyAppOptions().setUserID(Installation.id(this));

        /**
         * Configure AdColony in your launching Activity's onCreate() method so that cached ads can
         * be available as soon as possible.
         */
        AdColony.configure(this, app_options, APP_ID, ZONE_ID);

        /** Create and set a reward listener */
        AdColony.setRewardListener(new AdColonyRewardListener() {
            @Override
            public void onReward(AdColonyReward reward) {
                /** Query reward object for info here */
                Log.d( TAG, "onReward" );
            }
        });

        /**
         * Set up listener for interstitial ad callbacks. You only need to implement the callbacks
         * that you care about. The only required callback is onRequestFilled, as this is the only
         * way to get an ad object.
         */
        listener = new AdColonyInterstitialListener() {
            /** Ad passed back in request filled callback, ad can now be shown */
            @Override
            public void onRequestFilled(AdColonyInterstitial ad) {
                adc = ad;
                Log.d(TAG, "onRequestFilled");
                Log.d(TAG, adc.getZoneID());
            }

            /** Ad request was not filled */
            @Override
            public void onRequestNotFilled( AdColonyZone zone ) {
                Log.d(TAG, zone.getZoneID());
                Log.d(TAG, "onRequestNotFilled");
            }

            /** Ad opened, reset UI to reflect state change */
            @Override
            public void onOpened(AdColonyInterstitial ad) {
                Log.d(TAG, "onOpened");
            }

            /** Request a new ad if ad is expiring */
            @Override
            public void onExpiring(AdColonyInterstitial ad) {
                AdColony.requestInterstitial(ZONE_ID, this);
                Log.d( TAG, "onExpiring" );
            }
        };






        if (savedInstanceState!= null) {
            gameList = savedInstanceState.getParcelableArrayList("gameList");
            deletingGame = savedInstanceState.getParcelable("deletingGame");
            isAlert = savedInstanceState.getBoolean("DIALOG");
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

        setScoreValues();

        if (getIntent().getBooleanExtra("refreshGameList", false)) initGameList();
        if (isAlert) deleteDialog();
    }

    public void initGameList() {
        if (gameList == null) gameList = new ArrayList<>();
        try {
            gameList.clear();
            gameList.addAll(HelperFactory.getHelper().getGameDAO().getAllGames());
            for (int i = 0; i < gameList.size(); i ++) {
                gameList.get(i).invList = HelperFactory.getHelper().getInvestigatorDAO().getInvestigatorsListByGameID(gameList.get(i).id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                Intent intent = new Intent(this, DonateActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addPartyButton:

                if (adc != null) adc.show();
                //Intent intent = new Intent(this, GamesPagerActivity.class);
                //startActivity(intent);
                break;
            default:
                break;
        }
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
                                deleteParty();
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

    private void deleteParty() {
        System.out.println(deletingGame.ancientOneID);
        try {
            HelperFactory.getHelper().getGameDAO().delete(deletingGame);
            HelperFactory.getHelper().getInvestigatorDAO().deleteInvestigatorsByGameID(deletingGame.id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initGameList();
        adapter.notifyDataSetChanged();
        setScoreValues();
        Toast.makeText(this, R.string.success_deleting_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("DIALOG", isAlert);
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

    @Override
    protected void onResume()
    {
        super.onResume();

        /**
         * It's somewhat arbitrary when your ad request should be made. Here we are simply making
         * a request if there is no valid ad available onResume, but really this can be done at any
         * reasonable time before you plan on showing an ad.
         */
        if (adc == null || adc.isExpired())
        {
            /**
             * Optionally update location info in the ad options for each request:
             * LocationManager location_manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
             * Location location = location_manager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
             * ad_options.setUserMetadata( ad_options.getUserMetadata().setUserLocation( location ) );
             */
            AdColony.requestInterstitial(ZONE_ID, listener);
        }

    }
}