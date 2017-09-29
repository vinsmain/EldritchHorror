package ru.mgusev.eldritchhorror;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.sql.SQLException;

public class GameDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    Game game;

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
    FloatingActionButton editButton;
    FloatingActionButton deleteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        game = (Game) getIntent().getParcelableExtra("game");

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
        isSimpleMyths.setEnabled(false);
        isNormalMyths.setEnabled(false);
        isHardMyths.setEnabled(false);
        isStartingRumor.setEnabled(false);

        initPartyDetails();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarDetails);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.activity_party_details_header);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editButton = (FloatingActionButton) findViewById(R.id.editButton);
        deleteButton = (FloatingActionButton) findViewById(R.id.deleteButton);
        editButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
    }

    private void initPartyDetails() {
        dateField.setText(game.date);
        try {
            ancientOne.setText(HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneNameByID(game.ancientOneID));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        playersCount.setText(String.valueOf(game.playersCount));
        isSimpleMyths.setChecked(game.isSimpleMyths);
        isNormalMyths.setChecked(game.isNormalMyths);
        isHardMyths.setChecked(game.isHardMyths);
        isStartingRumor.setChecked(game.isStartingRumor);
        gatesCount.setText(String.valueOf(game.gatesCount));
        monstersCount.setText(String.valueOf(game.monstersCount));
        curseCount.setText(String.valueOf(game.curseCount));
        rumorsCount.setText(String.valueOf(game.rumorsCount));
        cluesCount.setText(String.valueOf(game.cluesCount));
        blessedCount.setText(String.valueOf(game.blessedCount));
        doomCount.setText(String.valueOf(game.doomCount));
        score.setText(String.valueOf(game.score));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editButton:
                Intent intentEdit = new Intent(this, AddGameActivity.class);
                intentEdit.putExtra("editParty", game);
                startActivity(intentEdit);
                break;
            case R.id.deleteButton:
                deleteDialog();
                break;
            default:
                break;
        }
    }

    private void deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.deleteDialogAlert))
                .setMessage(getResources().getString(R.string.deleteDialogMessage))
                .setIcon(android.R.drawable.ic_menu_delete)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.messageOK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteParty();
                                dialog.cancel();
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

    private void deleteParty() {
        try {
            HelperFactory.getHelper().getGameDAO().delete(game);
            HelperFactory.getHelper().getInvestigatorDAO().deleteInvestigatorsByGameID(game.id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Intent intentDelete = new Intent(this, MainActivity.class);
        intentDelete.putExtra("refreshPartyList", true);
        intentDelete.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentDelete);
    }
}