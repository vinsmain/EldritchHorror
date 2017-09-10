package ru.mgusev.eldritchhorror;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class PartyDetails extends AppCompatActivity {

    Party party;
    Toolbar toolbar;
    TextView dateField;
    TextView ancientOne;
    TextView playersCount;
    CheckBox isSimpleMyths;
    CheckBox isNormalMyths;
    CheckBox isHardMyths;
    CheckBox isStartingRumor;
    TextView gatesCount;
    TextView monstersCount;
    TextView curseCount;
    TextView rumorsCount;
    TextView cluesCount;
    TextView blessedCount;
    TextView doomCount;
    TextView score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_details);

        party = (Party) getIntent().getParcelableExtra("party");

        initToolbar();

        dateField = (TextView) findViewById(R.id.dataDetail);
        ancientOne = (TextView) findViewById(R.id.ancientOneDetail);
        playersCount = (TextView) findViewById(R.id.playersCountDetail);
        isSimpleMyths = (CheckBox) findViewById(R.id.simpleMythsDetail);
        isNormalMyths = (CheckBox) findViewById(R.id.normalMythsDetail);
        isHardMyths = (CheckBox) findViewById(R.id.hardMythsDetail);
        isStartingRumor = (CheckBox) findViewById(R.id.startingRumorDetail);
        gatesCount = (TextView) findViewById(R.id.gatesCountDetail);
        monstersCount = (TextView) findViewById(R.id.monstersCountDetail);
        curseCount = (TextView) findViewById(R.id.curseCountDetail);
        rumorsCount = (TextView) findViewById(R.id.rumorsCountDetail);
        cluesCount = (TextView) findViewById(R.id.cluesCountDetail);
        blessedCount = (TextView) findViewById(R.id.blessedCountDetail);
        doomCount = (TextView) findViewById(R.id.doomCountDetail);
        score = (TextView) findViewById(R.id.totalScoreDetail);

        isSimpleMyths.setClickable(false);
        isNormalMyths.setClickable(false);
        isHardMyths.setClickable(false);
        isStartingRumor.setClickable(false);

        initPartyDetails();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarDetails);
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

    private void initPartyDetails() {
        dateField.setText(party.date);
        ancientOne.setText(party.ancientOne);
        playersCount.setText(String.valueOf(party.playersCount));
        isSimpleMyths.setChecked(party.isSimpleMyths);
        isNormalMyths.setChecked(party.isNormalMyths);
        isHardMyths.setChecked(party.isHardMyths);
        isStartingRumor.setChecked(party.isStartingRumor);
        gatesCount.setText(String.valueOf(party.gatesCount));
        monstersCount.setText(String.valueOf(party.monstersCount));
        curseCount.setText(String.valueOf(party.curseCount));
        rumorsCount.setText(String.valueOf(party.rumorsCount));
        cluesCount.setText(String.valueOf(party.cluesCount));
        blessedCount.setText(String.valueOf(party.blessedCount));
        doomCount.setText(String.valueOf(party.doomCount));
        score.setText(String.valueOf(party.score));
    }
}
