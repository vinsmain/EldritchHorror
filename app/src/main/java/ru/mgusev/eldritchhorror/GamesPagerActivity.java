package ru.mgusev.eldritchhorror;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.sql.SQLException;

public class GamesPagerActivity extends AppCompatActivity implements PassMeLinkOnObject, View.OnClickListener {

    static final String TAG = "myLogs";
    static final int PAGE_COUNT = 3;

    public Game game;

    ViewPager pager;
    EHFragmentPagerAdapter pagerAdapter;
    Toolbar toolbar;
    FloatingActionButton saveGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_pager);

        initToolbar();

        saveGameButton = (FloatingActionButton) findViewById(R.id.saveGameButton);
        saveGameButton.setOnClickListener(this);
        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new EHFragmentPagerAdapter(this, getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        game = (Game) getIntent().getParcelableExtra("editParty");
        if (game == null) game = new Game();

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected, position = " + position);
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

        //editButton = (FloatingActionButton) findViewById(R.id.editButton);
        //editButton.setOnClickListener(this);
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void onClick(View view) {
        addDataToGame();
        writeGameToDB(view);
    }

    private void addDataToGame() {
        StartingDataFragment startingDataFragment = (StartingDataFragment) pagerAdapter.getItem(0);
        startingDataFragment.addDataToGame();
        InvestigatorsChoiceFragment investigatorsChoiceFragment = (InvestigatorsChoiceFragment) pagerAdapter.getItem(1);
        investigatorsChoiceFragment.addDataToGame();
        ResultGameFragment resultGameFragment = (ResultGameFragment)  pagerAdapter.getItem(2);
        resultGameFragment.addDataToGame();
    }

    private void writeGameToDB(View view) {
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

        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra("refreshGameList", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        StartingDataFragment startingDataFragment;
        InvestigatorsChoiceFragment investigatorsChoiceFragment;
        ResultGameFragment resultGameFragment;

        String[] titleArray = {getString(R.string.activity_add_party_header), getString(R.string.activity_investigators_choice_header), getString(R.string.activity_result_party_header)};

        MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                startingDataFragment = StartingDataFragment.newInstance(position);
                return startingDataFragment;
            }
            else if (position == 1) {
                investigatorsChoiceFragment = InvestigatorsChoiceFragment.newInstance(position);
                return investigatorsChoiceFragment;
            }
            else {
                resultGameFragment = ResultGameFragment.newInstance(position);
                return resultGameFragment;
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleArray[position];
        }

        public Fragment getFragment(int position) {
            return new Fragment();
        }
    }


}
