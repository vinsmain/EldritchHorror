package ru.mgusev.eldritchhorror.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.sql.SQLException;

import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.adapter.EHFragmentPagerAdapter;
import ru.mgusev.eldritchhorror.database.HelperFactory;
import ru.mgusev.eldritchhorror.eh_interface.PassMeLinkOnObject;
import ru.mgusev.eldritchhorror.fragment.InvestigatorsChoiceFragment;
import ru.mgusev.eldritchhorror.fragment.ResultGameFragment;
import ru.mgusev.eldritchhorror.fragment.StartingDataFragment;
import ru.mgusev.eldritchhorror.model.Game;

import static java.security.AccessController.getContext;

public class GamesPagerActivity extends AppCompatActivity implements PassMeLinkOnObject {

    static final String TAG = "myLogs";
    public static final int PAGE_COUNT = 3;

    public Game game;

    private MenuItem clearItem;

    ViewPager pager;
    EHFragmentPagerAdapter pagerAdapter;
    Toolbar toolbar;
    TextView score;
    View currentFocusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_pager);

        initToolbar();

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new EHFragmentPagerAdapter(this, getSupportFragmentManager(), this);
        System.out.println("Oncreate " + this);
        pager.setAdapter(pagerAdapter);

        score = (TextView) findViewById(R.id.score_pager);

        if (game == null) game = (Game) getIntent().getParcelableExtra("editParty");
        if (game == null) game = new Game();
        score.setText(String.valueOf(game.score));

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected, position = " + position);

                if (position == 1) clearItem.setVisible(true);
                else clearItem.setVisible(false);

                /*if (position == 2) {
                    if (currentFocusView != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInputFromWindow(currentFocusView.getApplicationWindowToken(),InputMethodManager.SHOW_IMPLICIT, 0);
                        currentFocusView.requestFocus();
                    }
                }

                if (position != 2) {
                    currentFocusView = getCurrentFocus();

                    if (currentFocusView != null) {
                        System.out.println(currentFocusView.findFocus());
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(currentFocusView.getWindowToken(), 0);
                    }
                }*/
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarPager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_game_header);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_games_pager_activity, menu);
        clearItem = menu.findItem(R.id.action_clear);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                addDataToGame();
                writeGameToDB();
                return true;
            case R.id.action_clear:
                ((InvestigatorsChoiceFragment)pagerAdapter.getItem(1)).cleanDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Game getGame() {
        return game;
    }

    private void addDataToGame() {
        ((StartingDataFragment)pagerAdapter.getItem(0)).addDataToGame();
        ((InvestigatorsChoiceFragment)pagerAdapter.getItem(1)).addDataToGame();
        ((ResultGameFragment)pagerAdapter.getItem(2)).addDataToGame();
    }

    private void writeGameToDB() {
        try {
            int id = HelperFactory.getHelper().getGameDAO().writeGameToDB(game);
            HelperFactory.getHelper().getInvestigatorDAO().deleteInvestigatorsByGameID(id);
            for (int i = 0; i < game.invList.size(); i++) {
                game.invList.get(i).gameId = id;
                HelperFactory.getHelper().getInvestigatorDAO().create(game.invList.get(i));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("refreshGameList", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        addDataToGame();
        outState.putParcelable("game", game);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        game = savedInstanceState.getParcelable("game");
        System.out.println(game.date);
    }
}