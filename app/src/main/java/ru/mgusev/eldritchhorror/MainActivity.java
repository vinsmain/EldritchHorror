package ru.mgusev.eldritchhorror;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RVAdapter.OnItemClicked {

    private List<Game> gameList;
    FloatingActionButton addPartyButton;
    String bestScoreValue = "";
    String worstScoreValue = "";

    TextView gamesCount;
    TextView bestScore;
    TextView worstScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addPartyButton = (FloatingActionButton) findViewById(R.id.addPartyButton);
        addPartyButton.setOnClickListener(this);

        gamesCount = (TextView) findViewById(R.id.gamesCount);
        bestScore = (TextView) findViewById(R.id.bestScore);
        worstScore = (TextView) findViewById(R.id.worstScore);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.gameList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        initGameList();

        RVAdapter adapter = new RVAdapter(gameList);
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);

        setScoreValues();

        gamesCount.setText(String.valueOf(adapter.getItemCount()));
        bestScore.setText(bestScoreValue);
        worstScore.setText(worstScoreValue);

        if ((Boolean) getIntent().getBooleanExtra("refreshGameList", false)) initGameList();
    }

    public void initGameList() {
        gameList = new ArrayList<>();

        try {
            gameList = HelperFactory.getHelper().getGameDAO().getAllGames();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addPartyButton:
                Intent intent = new Intent(this, AddGameActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intentGameDetails = new Intent(this, GameDetails.class);
        intentGameDetails.putExtra("game", gameList.get(position));
        startActivity(intentGameDetails);
    }

    private void setScoreValues() {
        try {
            Game game = HelperFactory.getHelper().getGameDAO().getTopGameToSort(true);
            if (game != null) bestScoreValue = String.valueOf(game.score);
            game = HelperFactory.getHelper().getGameDAO().getTopGameToSort(false);
            if (game != null) worstScoreValue = String.valueOf(game.score);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
