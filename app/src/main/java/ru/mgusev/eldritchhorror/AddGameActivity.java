package ru.mgusev.eldritchhorror;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AddGameActivity extends AppCompatActivity implements View.OnClickListener {

    Game game;
    boolean isEdit;
    Toolbar toolbar;
    ImageButton dateButton;
    TextView dateField;
    Spinner ancientOneSpinner;
    Spinner playersCountSpinner;
    Switch isSimpleMyths;
    Switch isNormalMyths;
    Switch isHardMyths;
    Switch isStartingRumor;
    FloatingActionButton nextButton;
    String[] ancientOneArray;
    String[] playersCountArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);
        dateButton = (ImageButton) findViewById(R.id.dateImgBtn);
        dateButton.setOnClickListener(this);

        dateField = (TextView) findViewById(R.id.dateField);
        dateField.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date()));

        isSimpleMyths = (Switch) findViewById(R.id.isSimpleMyths);
        isNormalMyths = (Switch) findViewById(R.id.isNormalMyths);
        isHardMyths = (Switch) findViewById(R.id.isHardMyths);
        isStartingRumor = (Switch) findViewById(R.id.isStartingMyth);

        nextButton = (FloatingActionButton) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);

        try {
            ancientOneArray = HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneArray();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        playersCountArray = getResources().getStringArray(R.array.playersCountArray);

        initAncientOneSpinner();
        initPlayersCountSpinner();
        initToolbar();

        game = (Game) getIntent().getParcelableExtra("editParty");
        if (game != null) {
            setPartyForEdit();
            isEdit = true;
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarInvChoice);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.activity_add_party_header);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dateImgBtn:
                DialogFragment dateDialog = new DatePicker();
                dateDialog.show(getFragmentManager(), "datePicker");
                break;
            case R.id.nextButton:
                Intent intent = new Intent(this, InvestigatorsChoiceAvtivity.class);
                if (isEdit) addValuesToParty();
                else {
                    game = new Game();
                    addValuesToParty();
                }
                intent.putExtra("game", game);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void addValuesToParty() {
        game.date = dateField.getText().toString();
        try {
            game.ancientOneID = HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneIDByName(ancientOneArray[ancientOneSpinner.getSelectedItemPosition()]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        game.playersCount = Integer.parseInt(playersCountArray[playersCountSpinner.getSelectedItemPosition()]);
        game.isSimpleMyths = isSimpleMyths.isChecked();
        game.isNormalMyths = isNormalMyths.isChecked();
        game.isHardMyths = isHardMyths.isChecked();
        game.isStartingRumor = isStartingRumor.isChecked();
    }

    private void setPartyForEdit() {
        dateField.setText(game.date);
        try {
            ancientOneSpinner.setSelection(getItemIndexInArray(ancientOneArray, HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneNameByID(game.ancientOneID)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        playersCountSpinner.setSelection(getItemIndexInArray(playersCountArray, String.valueOf(game.playersCount)));
        isSimpleMyths.setChecked(game.isSimpleMyths);
        isNormalMyths.setChecked(game.isNormalMyths);
        isHardMyths.setChecked(game.isHardMyths);
        isStartingRumor.setChecked(game.isStartingRumor);
    }

    private int getItemIndexInArray(String[] array, String value) {
        for (int i = 0; i < array.length; i++) {
            if(array[i].equals(value)) {
                return i;
            }
        }
        return 0;
    }

    private void initAncientOneSpinner() {
        ArrayAdapter<String> ancientOneAdapter = new ArrayAdapter<String>(this, R.layout.spinner, ancientOneArray);
        ancientOneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ancientOneSpinner = (Spinner) findViewById(R.id.ancientOneSpinner);
        ancientOneSpinner.setAdapter(ancientOneAdapter);

        ancientOneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initPlayersCountSpinner() {
        ArrayAdapter<String> playersCountAdapter = new ArrayAdapter<>(this, R.layout.spinner, playersCountArray);
        playersCountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        playersCountSpinner = (Spinner) findViewById(R.id.playersCountSpinner);
        playersCountSpinner.setAdapter(playersCountAdapter);

        playersCountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}