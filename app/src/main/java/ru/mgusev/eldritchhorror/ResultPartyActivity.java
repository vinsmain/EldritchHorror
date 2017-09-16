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

    int gatesCountResult;
    int monstersCountResult;
    int curseCountResult;
    int rumorsCountResult;
    int cluesCountResult;
    int blessedCountResult;
    int doomCountResult;

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
        monstersCount.addTextChangedListener(this);
        curseCount.addTextChangedListener(this);
        rumorsCount.addTextChangedListener(this);
        cluesCount.addTextChangedListener(this);
        blessedCount.addTextChangedListener(this);
        doomCount.addTextChangedListener(this);

        party = (Party) getIntent().getParcelableExtra("party");
        if (party.id != -1) setPartyForEdit();
        refreshScore();

        dbHelper = new DBHelper(this);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.activity_result_party_header);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_DATE, party.date);
        contentValues.put(DBHelper.KEY_ANCIENT_ONE, party.ancientOne);
        contentValues.put(DBHelper.KEY_PLAYERS_COUNT, party.playersCount);
        contentValues.put(DBHelper.KEY_SIMPLE_MYTHS, party.isSimpleMyths);
        contentValues.put(DBHelper.KEY_NORMAL_MYTHS, party.isNormalMyths);
        contentValues.put(DBHelper.KEY_HARD_MYTHS, party.isHardMyths);
        contentValues.put(DBHelper.KEY_STARTING_RUMOR, party.isStartingRumor);
        contentValues.put(DBHelper.KEY_GATES_COUNT, gatesCountResult);
        contentValues.put(DBHelper.KEY_MONSTERS_COUNT, monstersCountResult);
        contentValues.put(DBHelper.KEY_CURSE_COUNT, curseCountResult);
        contentValues.put(DBHelper.KEY_RUMORS_COUNT, rumorsCountResult);
        contentValues.put(DBHelper.KEY_CLUES_COUNT, cluesCountResult);
        contentValues.put(DBHelper.KEY_BLESSED_COUNT, blessedCountResult);
        contentValues.put(DBHelper.KEY_DOOM_COUNT, doomCountResult);
        contentValues.put(DBHelper.KEY_SCORE, getScore());

        if (party.id != -1) database.update(DBHelper.TABLE_GAMES, contentValues, "_id=" + party.id, null);
        else database.insert(DBHelper.TABLE_GAMES, null, contentValues);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("refreshPartyList", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void setResultsForCalculation() {
        gatesCountResult = getResultToField(gatesCount);
        monstersCountResult = getResultToField(monstersCount);
        curseCountResult = getResultToField(curseCount);
        rumorsCountResult = getResultToField(rumorsCount);
        cluesCountResult = getResultToField(cluesCount);
        blessedCountResult = getResultToField(blessedCount);
        doomCountResult = getResultToField(doomCount);
    }

    private void setPartyForEdit() {
        gatesCount.setText(String.valueOf(party.gatesCount));
        monstersCount.setText(String.valueOf(party.monstersCount));
        curseCount.setText(String.valueOf(party.curseCount));
        rumorsCount.setText(String.valueOf(party.rumorsCount));
        cluesCount.setText(String.valueOf(party.cluesCount));
        blessedCount.setText(String.valueOf(party.blessedCount));
        doomCount.setText(String.valueOf(party.doomCount));
    }

    private int getResultToField(EditText editText) {
        if (editText.getText().toString().equals("")) return 0;
        else return Integer.parseInt(editText.getText().toString());
    }

    private int getScore() {
        int monstersCount = (int)Math.ceil(monstersCountResult / 3.0f);
        int cluesCount = (int)Math.ceil(cluesCountResult / 3.0f);
        return gatesCountResult + monstersCount + curseCountResult + rumorsCountResult * 3 - cluesCount - blessedCountResult - doomCountResult;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        refreshScore();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void refreshScore() {
        setResultsForCalculation();
        score.setText(String.valueOf(getScore()));
    }
}