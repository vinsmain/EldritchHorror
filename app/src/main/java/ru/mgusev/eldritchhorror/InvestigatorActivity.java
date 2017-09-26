package ru.mgusev.eldritchhorror;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class InvestigatorActivity extends Activity {

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

        initInvestigator();
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
    }
}
