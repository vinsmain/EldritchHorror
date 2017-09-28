package ru.mgusev.eldritchhorror;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.sql.SQLException;

public class ResultGameActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    Toolbar toolbar;
    EditText gatesCount;
    EditText monstersCount;
    EditText curseCount;
    EditText rumorsCount;
    EditText cluesCount;
    EditText blessedCount;
    EditText doomCount;
    TextView score;
    FloatingActionButton savePartyButton;
    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_party);
        initActivityElements();
        setListeners();

        game = (Game) getIntent().getParcelableExtra("game");
        if (game.id != -1) addGameValuesToFields();
        else refreshScore();
    }

    private void setListeners() {
        savePartyButton.setOnClickListener(this);
        gatesCount.addTextChangedListener(this);
        monstersCount.addTextChangedListener(this);
        curseCount.addTextChangedListener(this);
        rumorsCount.addTextChangedListener(this);
        cluesCount.addTextChangedListener(this);
        blessedCount.addTextChangedListener(this);
        doomCount.addTextChangedListener(this);
    }

    private void initActivityElements() {
        initToolbar();
        gatesCount = (EditText) findViewById(R.id.gatesCount);
        monstersCount = (EditText) findViewById(R.id.monstersCount);
        curseCount = (EditText) findViewById(R.id.curseCount);
        rumorsCount = (EditText) findViewById(R.id.rumorsCount);
        cluesCount = (EditText) findViewById(R.id.cluesCount);
        blessedCount = (EditText) findViewById(R.id.blessedCount);
        doomCount = (EditText) findViewById(R.id.doomCount);
        score = (TextView) findViewById(R.id.totalScore);
        savePartyButton = (FloatingActionButton) findViewById(R.id.savePartyButton);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarInvChoice);
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
        try {
            game.gatesCount =  getResultToField(gatesCount);
            game.monstersCount = getResultToField(monstersCount);
            game.curseCount =  getResultToField(curseCount);
            game.rumorsCount = getResultToField(rumorsCount);
            game.cluesCount = getResultToField(cluesCount);
            game.blessedCount = getResultToField(blessedCount);
            game.doomCount = getResultToField(doomCount);
            game.score = getScore();
            HelperFactory.getHelper().getGameDAO().createOrUpdate(game);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("refreshGameList", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void addGameValuesToFields() {
        gatesCount.setText(String.valueOf(game.gatesCount));
        monstersCount.setText(String.valueOf(game.monstersCount));
        curseCount.setText(String.valueOf(game.curseCount));
        rumorsCount.setText(String.valueOf(game.rumorsCount));
        cluesCount.setText(String.valueOf(game.cluesCount));
        blessedCount.setText(String.valueOf(game.blessedCount));
        doomCount.setText(String.valueOf(game.doomCount));
    }

    private int getScore() {
        return getResultToField(gatesCount) + (int)Math.ceil(getResultToField(monstersCount) / 3.0f) + getResultToField(curseCount)
                + getResultToField(rumorsCount) * 3 - (int)Math.ceil(getResultToField(cluesCount) / 3.0f) - getResultToField(blessedCount) - getResultToField(doomCount);
    }

    private int getResultToField(EditText editText) {
        if (editText.getText().toString().equals("")) return 0;
        else return Integer.parseInt(editText.getText().toString());
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
        score.setText(String.valueOf(getScore()));
    }
}