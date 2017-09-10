package ru.mgusev.eldritchhorror;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ResultPartyActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    Toolbar toolbar;
    EditText gatesCount;
    EditText monstersCount;
    EditText curseCount;
    EditText rumorsCount;
    EditText cluesCount;
    EditText blessedCount;
    EditText doomCount;
    TextView score;
    Button savePartyButton;

    Party party;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_party);

        initToolbar();

        gatesCount = (EditText) findViewById(R.id.gatesCount);
        monstersCount = (EditText) findViewById(R.id.monstersCount);
        curseCount = (EditText) findViewById(R.id.curseCount);
        rumorsCount = (EditText) findViewById(R.id.rumorsCount);
        cluesCount = (EditText) findViewById(R.id.cluesCount);
        blessedCount = (EditText) findViewById(R.id.blessedCount);
        doomCount = (EditText) findViewById(R.id.doomCount);
        score = (TextView) findViewById(R.id.totalScore);
        savePartyButton = (Button) findViewById(R.id.savePartyButton);

        savePartyButton.setOnClickListener(this);
        gatesCount.addTextChangedListener(this);

        party = (Party) getIntent().getParcelableExtra("party");

        dbHelper = new DBHelper(this);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        writeDataToParty();
        score.setText(String.valueOf(party.score));

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_DATE, party.date);
        contentValues.put(DBHelper.KEY_ANCIENT_ONE, party.ancientOne);
        contentValues.put(DBHelper.KEY_PLAYERS_COUNT, party.playersCount);
        contentValues.put(DBHelper.KEY_SIMPLE_MYTHS, party.isSimpleMyths);
        contentValues.put(DBHelper.KEY_NORMAL_MYTHS, party.isNormalMyths);
        contentValues.put(DBHelper.KEY_HARD_MYTHS, party.isHardMyths);
        contentValues.put(DBHelper.KEY_STARTING_RUMOR, party.isStartingRumor);
        contentValues.put(DBHelper.KEY_GATES_COUNT, party.gatesCount);
        contentValues.put(DBHelper.KEY_MONSTERS_COUNT, party.monstersCount);
        contentValues.put(DBHelper.KEY_CURSE_COUNT, party.curseCount);
        contentValues.put(DBHelper.KEY_RUMORS_COUNT, party.rumorsCount);
        contentValues.put(DBHelper.KEY_CLUES_COUNT, party.cluesCount);
        contentValues.put(DBHelper.KEY_BLESSED_COUNT, party.blessedCount);
        contentValues.put(DBHelper.KEY_DOOM_COUNT, party.doomCount);
        contentValues.put(DBHelper.KEY_SCORE, party.score);

        database.insert(DBHelper.TABLE_GAMES, null, contentValues);

        boolean refresh = true;

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("refresh", refresh);
        startActivity(intent);
    }

    private void writeDataToParty() {
        party.gatesCount = Integer.parseInt(gatesCount.getText().toString());
        party.monstersCount = Integer.parseInt(monstersCount.getText().toString());
        party.curseCount = Integer.parseInt(curseCount.getText().toString());
        party.rumorsCount = Integer.parseInt(rumorsCount.getText().toString());
        party.cluesCount = Integer.parseInt(cluesCount.getText().toString());
        party.blessedCount = Integer.parseInt(blessedCount.getText().toString());
        party.doomCount = Integer.parseInt(doomCount.getText().toString());
        party.score = getScore();
    }

    private int getScore() {
        int monstersCount = (int)Math.ceil(party.monstersCount / 3.0f);
        int cluesCount = (int)Math.ceil(party.cluesCount / 3.0f);
        return party.gatesCount + monstersCount + party.curseCount + party.rumorsCount * 3 - cluesCount - party.blessedCount - party.doomCount;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        System.out.println(getScore());
        score.setText(String.valueOf(getScore()));
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
