package ru.mgusev.eldritchhorror.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import ru.mgusev.eldritchhorror.model.Investigator;

public class GamesPagerActivity extends AppCompatActivity implements PassMeLinkOnObject {

    static final String TAG = "myLogs";
    public static final int PAGE_COUNT = 3;

    public Game game;

    private MenuItem clearItem;

    ViewPager pager;
    EHFragmentPagerAdapter pagerAdapter;
    Toolbar toolbar;
    TextView score;
    int currentPosition = 0;
    int titleResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_pager);
        AndroidBug5497Workaround.assistActivity(this);
        //https://github.com/chenxiruanhai/AndroidBugFix/blob/master/bug-5497/AndroidBug5497Workaround.java

        currentPosition = (int) getIntent().getIntExtra("setPosition", 0);

        if (savedInstanceState!= null) {
            currentPosition = savedInstanceState.getInt("position", 0);
            game = savedInstanceState.getParcelable("game");
        }
        ((NestedScrollView)findViewById(R.id.pager_scroll)).setFillViewport(true);
        pagerAdapter = new EHFragmentPagerAdapter (this, getSupportFragmentManager(), this);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(pagerAdapter);

        score = (TextView) findViewById(R.id.score_pager);

        if (game == null) game = (Game) getIntent().getParcelableExtra("editParty");
        if (game == null) game = new Game();

        score.setText(String.valueOf(game.score));

        if (game.id == -1) titleResource = R.string.add_new_game;
        else titleResource = R.string.edit_game;

        initToolbar();

        pager.postDelayed(new Runnable() {
            @Override
            public void run() {
                pager.setCurrentItem(currentPosition, false);
                pagerAdapter.notifyDataSetChanged();
            }
        }, 100);

            invalidateOptionsMenu();
            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    Log.d(TAG, "onPageSelected, position = " + position);
                    currentPosition = position;

                    if (position == 1) clearItem.setVisible(true);
                    else clearItem.setVisible(false);

                    if (position != 2) {
                        View view = getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }
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
        getSupportActionBar().setTitle(titleResource);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishDialog();
            }
        });
    }

    private void finishDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.dialogBackAlert))
                .setMessage(getResources().getString(R.string.backDialogMessage))
                .setIcon(R.drawable.back_icon)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.messageOK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.messageCancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
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
        addDataToGame();
        outState.putParcelable("game", game);
        outState.putInt("position", currentPosition);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pagerAdapter.getItem(1).onActivityResult(requestCode, resultCode, data);
    }
}