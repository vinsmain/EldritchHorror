package ru.mgusev.eldritchhorror;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RVAdapter.OnItemClicked {

    private List<Party> partyList;
    FloatingActionButton addPartyButton;
    DBHelper dbHelper;
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

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.partyList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        dbHelper = new DBHelper(this);

        initPartyList();

        RVAdapter adapter = new RVAdapter(partyList);
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(this);

        setScoreValues();

        gamesCount.setText(String.valueOf(adapter.getItemCount()));
        bestScore.setText(bestScoreValue);
        worstScore.setText(worstScoreValue);

        if ((Boolean) getIntent().getBooleanExtra("refreshPArtyList", false)) initPartyList();
    }

    public void initPartyList() {
        partyList = new ArrayList<>();

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_GAMES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE);
            int ancientOneIndex = cursor.getColumnIndex(DBHelper.KEY_ANCIENT_ONE);
            int playersCountIndex = cursor.getColumnIndex(DBHelper.KEY_PLAYERS_COUNT);
            int isSimpleMythsIndex = cursor.getColumnIndex(DBHelper.KEY_SIMPLE_MYTHS);
            int isNormalMythsIndex = cursor.getColumnIndex(DBHelper.KEY_NORMAL_MYTHS);
            int isHardMythsIndex = cursor.getColumnIndex(DBHelper.KEY_HARD_MYTHS);
            int isStartingRumorIndex = cursor.getColumnIndex(DBHelper.KEY_STARTING_RUMOR);
            int gatesCountIndex = cursor.getColumnIndex(DBHelper.KEY_GATES_COUNT);
            int monstersCountIndex = cursor.getColumnIndex(DBHelper.KEY_MONSTERS_COUNT);
            int curseCountIndex = cursor.getColumnIndex(DBHelper.KEY_CURSE_COUNT);
            int rumorsCountIndex = cursor.getColumnIndex(DBHelper.KEY_RUMORS_COUNT);
            int cluesCountIndex = cursor.getColumnIndex(DBHelper.KEY_CLUES_COUNT);
            int blessedCountIndex = cursor.getColumnIndex(DBHelper.KEY_BLESSED_COUNT);
            int doomCountIndex = cursor.getColumnIndex(DBHelper.KEY_DOOM_COUNT);
            int scoreIndex = cursor.getColumnIndex(DBHelper.KEY_SCORE);

            do {
                Party party = new Party();
                party.id = cursor.getInt(idIndex);
                party.date = cursor.getString(dateIndex);
                party.ancientOne = cursor.getString(ancientOneIndex);
                party.playersCount = cursor.getInt(playersCountIndex);
                party.isSimpleMyths = cursor.getInt(isSimpleMythsIndex) == 1;
                party.isNormalMyths = cursor.getInt(isNormalMythsIndex) == 1;
                party.isHardMyths = cursor.getInt(isHardMythsIndex) == 1;
                party.isStartingRumor = cursor.getInt(isStartingRumorIndex) == 1;
                party.gatesCount = cursor.getInt(gatesCountIndex);
                party.monstersCount = cursor.getInt(monstersCountIndex);
                party.curseCount = cursor.getInt(curseCountIndex);
                party.rumorsCount = cursor.getInt(rumorsCountIndex);
                party.cluesCount = cursor.getInt(cluesCountIndex);
                party.blessedCount = cursor.getInt(blessedCountIndex);
                party.doomCount = cursor.getInt(doomCountIndex);
                party.score = cursor.getInt(scoreIndex);
                partyList.add(party);
            } while (cursor.moveToNext());

        }
        cursor.close();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addPartyButton:
                Intent intent = new Intent(this, AddPartyActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intentPartyDetails = new Intent(this, PartyDetails.class);
        intentPartyDetails.putExtra("party", partyList.get(position));
        startActivity(intentPartyDetails);
    }

    private void setScoreValues() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_GAMES, null, null, null, null, null, DBHelper.KEY_SCORE);

        if (cursor.moveToFirst()) {
            bestScoreValue = String.valueOf(cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_SCORE)));
        }
        if (cursor.moveToLast()) {
            worstScoreValue = String.valueOf(cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_SCORE)));
        }
        cursor.close();
    }
}
