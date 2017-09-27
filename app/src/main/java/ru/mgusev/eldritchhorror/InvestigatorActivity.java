package ru.mgusev.eldritchhorror;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class InvestigatorActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    Investigator investigator;
    ImageView invPhotoDialog;
    TextView invNameDialog;
    TextView invOccupationDialog;
    Switch switchStartingGame;
    Switch switchReplacement;
    Switch switchDead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investigator);

        investigator = (Investigator) getIntent().getParcelableExtra("investigator");

        invPhotoDialog = (ImageView) findViewById(R.id.invPhotoDialog);
        invNameDialog = (TextView) findViewById(R.id.invNameDialog);
        invOccupationDialog = (TextView) findViewById(R.id.invOccupationDialog);
        switchStartingGame = (Switch) findViewById(R.id.switchStartingGame);
        switchReplacement = (Switch) findViewById(R.id.switchReplacement);
        switchDead = (Switch) findViewById(R.id.switchDead);

        setListeners();
        initInvestigator();
    }

    private void setListeners() {
        switchStartingGame.setOnCheckedChangeListener(this);
        switchReplacement.setOnCheckedChangeListener(this);
        switchDead.setOnCheckedChangeListener(this);
    }

    private void initInvestigator() {
        Resources resources = this.getResources();
        final int resourceId = resources.getIdentifier(investigator.imageResource, "drawable", this.getPackageName());
        invPhotoDialog.setImageResource(resourceId);
        invNameDialog.setText(investigator.name);
        invOccupationDialog.setText(investigator.occupation);

        if (!investigator.isMale) {
            switchStartingGame.setText(R.string.starting_game_female);
            switchReplacement.setText(R.string.replacement_female);
            switchDead.setText(R.string.dead_female);
        }

        switchStartingGame.setChecked(investigator.isStarting);
        switchReplacement.setChecked(investigator.isReplacement);
        switchDead.setChecked(investigator.isDead);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.switchStartingGame:
                if (b && switchReplacement.isChecked()) switchReplacement.setChecked(false);
                break;
            case R.id.switchReplacement:
                if (b && switchStartingGame.isChecked()) switchStartingGame.setChecked(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void finish() {
        if (!switchStartingGame.isChecked() && !switchReplacement.isChecked()) {
            switchDead.setChecked(false);
        }
        investigator.isStarting = switchStartingGame.isChecked();
        investigator.isReplacement = switchReplacement.isChecked();
        investigator.isDead = switchDead.isChecked();

        Intent intent = new Intent();
        intent.putExtra("investigator", investigator);
        setResult(RESULT_OK, intent);

        super.finish();
    }
}
