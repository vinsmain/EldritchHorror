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

import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.adapter.RVAdapter;
import ru.mgusev.eldritchhorror.database.HelperFactory;
import ru.mgusev.eldritchhorror.eh_interface.OnItemClicked;
import ru.mgusev.eldritchhorror.fragment.DonateDialogFragment;
import ru.mgusev.eldritchhorror.model.Game;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnItemClicked {

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
    boolean isAdvertisingDialog;

    private DonateDialogFragment dialog;
    private DateHelper dateHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateHelper = new DateHelper(this);

        dialog = new DonateDialogFragment();
        dialog.setActivity(this);

        if (savedInstanceState!= null) {
            gameList = savedInstanceState.getParcelableArrayList("gameList");
            deletingGame = savedInstanceState.getParcelable("deletingGame");
            isAlert = savedInstanceState.getBoolean("DIALOG");
            isAdvertisingDialog = savedInstanceState.getBoolean("DIALOG_ADVERTISING");
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
        if (isAdvertisingDialog) onClick(addPartyButton);
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
                if (dateHelper.isAdvertisingShow()) {
                    isAdvertisingDialog = true;
                    dialog.show(getSupportFragmentManager(), "DonateDialogFragment");
                }
                else addGame();
                break;
            default:
                break;
        }
    }

    private void addGame() {
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

    public void setAdvertisingDialog(boolean advertisingDialog) {
        isAdvertisingDialog = advertisingDialog;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("DIALOG", isAlert);
        outState.putBoolean("DIALOG_ADVERTISING", isAdvertisingDialog);
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
}