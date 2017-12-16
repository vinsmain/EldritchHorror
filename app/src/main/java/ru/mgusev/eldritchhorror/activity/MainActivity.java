package ru.mgusev.eldritchhorror.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
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

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.adapter.RVAdapter;
import ru.mgusev.eldritchhorror.database.HelperFactory;
import ru.mgusev.eldritchhorror.eh_interface.OnItemClicked;
import ru.mgusev.eldritchhorror.fragment.DonateDialogFragment;
import ru.mgusev.eldritchhorror.fragment.RateDialogFragment;
import ru.mgusev.eldritchhorror.model.Game;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnItemClicked {

    public static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    private static final int SORT_MODE_DATE_DOWN = 1;
    private static final int SORT_MODE_DATE_UP = 2;
    private static final int SORT_MODE_ANCIENT_ONE = 3;
    private static final int SORT_MODE_SCORE_DOWN = 4;
    private static final int SORT_MODE_SCORE_UP = 5;

    private int columnsCount = 1;
    private int currentSortMode = SORT_MODE_DATE_UP;
    private MenuItem sortItem;

    private List<Game> gameList;
    RecyclerView recyclerView;
    RecyclerView.OnScrollListener onScrollListener;
    RVAdapter adapter;
    Game deletingGame;
    FloatingActionButton addPartyButton;
    TextView messageStarting;
    String bestScoreValue = "";
    String worstScoreValue = "";

    TextView gamesCount;
    TextView bestScore;
    TextView worstScore;

    AlertDialog alert;
    boolean isAlert;
    boolean isAdvertisingDialog;

    private DonateDialogFragment donateDialog;
    private RateDialogFragment rateDialog;
    private PrefHelper prefHelper;

    private AdColonyHelper helper;
    private AdColonyTask task;
    private boolean isLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefHelper = new PrefHelper(this);
        currentSortMode = prefHelper.loadSortMode();

        initDonateDialog();

        if (savedInstanceState!= null) {
            gameList = savedInstanceState.getParcelableArrayList("gameList");
            deletingGame = savedInstanceState.getParcelable("deletingGame");
            isAlert = savedInstanceState.getBoolean("DIALOG");
            isAdvertisingDialog = savedInstanceState.getBoolean("DIALOG_ADVERTISING");
            isLock = savedInstanceState.getBoolean("LOCK");
        }

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

        setScoreValues();
        showMessageStarting();

        if (getIntent().getBooleanExtra("refreshGameList", false)) initGameList();
        if (isAlert) deleteDialog();
        if (isAdvertisingDialog) showDonateDialog();

        if (prefHelper.isRate()) initRateDialog();
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

    public void initGameList() {
        if (gameList == null) gameList = new ArrayList<>();
        try {
            gameList.clear();
            gameList.addAll(getSortedGames());
            for (int i = 0; i < gameList.size(); i ++) {
                gameList.get(i).invList = HelperFactory.getHelper().getInvestigatorDAO().getInvestigatorsListByGameID(gameList.get(i).id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (adapter != null) adapter.notifyDataSetChanged();
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
        try {
            HelperFactory.getHelper().getGameDAO().delete(deletingGame);
            HelperFactory.getHelper().getInvestigatorDAO().deleteInvestigatorsByGameID(deletingGame.id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initGameList();
        adapter.notifyDataSetChanged();
        setScoreValues();
        showMessageStarting();
        Toast.makeText(this, R.string.success_deleting_message, Toast.LENGTH_SHORT).show();
    }

    public void setAdvertisingDialog(boolean advertisingDialog) {
        isAdvertisingDialog = advertisingDialog;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("DIALOG", isAlert);
        outState.putBoolean("DIALOG_ADVERTISING", isAdvertisingDialog);
        outState.putBoolean("LOCK", isLock);
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