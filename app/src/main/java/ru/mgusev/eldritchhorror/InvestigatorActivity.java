package ru.mgusev.eldritchhorror;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class InvestigatorActivity extends Activity {

    InvestigatorLocal investigatorLocal;
    ImageView invPhotoDialog;
    TextView invNameDialog;
    TextView invOccupationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investigator);

        investigatorLocal = (InvestigatorLocal) getIntent().getParcelableExtra("investigator");

        invPhotoDialog = (ImageView) findViewById(R.id.invPhotoDialog);
        invNameDialog = (TextView) findViewById(R.id.invNameDialog);
        invOccupationDialog = (TextView) findViewById(R.id.invOccupationDialog);

        initInvestigator();
    }

    private void initInvestigator() {
        Resources resources = this.getResources();
        final int resourceId = resources.getIdentifier(investigatorLocal.imageResource, "drawable", this.getPackageName());
        invPhotoDialog.setImageResource(resourceId);
        invNameDialog.setText(investigatorLocal.name);
        invOccupationDialog.setText(investigatorLocal.occupation);
    }
}
