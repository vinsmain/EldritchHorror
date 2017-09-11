package ru.mgusev.eldritchhorror;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddPartyActivity extends AppCompatActivity implements View.OnClickListener {

    Party party;
    boolean isEdit;
    Toolbar toolbar;
    ImageView dateButton;
    TextView dateField;
    Spinner ancientOneSpinner;
    Spinner playersCountSpinner;
    CheckBox isSimpleMyths;
    CheckBox isNormalMyths;
    CheckBox isHardMyths;
    CheckBox isStartingRumor;
    Button nextButton;
    String[] ancientOneArray;
    String[] playersCountArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_party);
        dateButton = (ImageView) findViewById(R.id.dateButtonIV);
        dateButton.setOnClickListener(this);

        dateField = (TextView) findViewById(R.id.dateField);
        dateField.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date()));

        isSimpleMyths = (CheckBox) findViewById(R.id.isSimpleMyths);
        isNormalMyths = (CheckBox) findViewById(R.id.isNormalMyths);
        isHardMyths = (CheckBox) findViewById(R.id.isHardMyths);
        isStartingRumor = (CheckBox) findViewById(R.id.isStartingMyth);

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);

        ancientOneArray = getResources().getStringArray(R.array.ancientOneArray);
        playersCountArray = getResources().getStringArray(R.array.playersCountArray);

        initAncientOneSpinner();
        initPlayersCountSpinner();
        initToolbar();

        party = (Party) getIntent().getParcelableExtra("editParty");
        if (party != null) {
            setPartyForEdit();
            isEdit = true;
        }
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
        switch (view.getId()) {
            case R.id.dateButtonIV:
                DialogFragment dateDialog = new DatePicker();
                dateDialog.show(getFragmentManager(), "datePicker");
                break;
            case R.id.nextButton:
                Intent intent = new Intent(this, ResultPartyActivity.class);
                if (isEdit) addValuesToParty();
                else {
                    party = new Party();
                    addValuesToParty();
                }
                intent.putExtra("party", party);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void addValuesToParty() {
        party.date = dateField.getText().toString();
        party.ancientOne = ancientOneArray[ancientOneSpinner.getSelectedItemPosition()];
        party.playersCount = Integer.parseInt(playersCountArray[playersCountSpinner.getSelectedItemPosition()]);
        party.isSimpleMyths = isSimpleMyths.isChecked();
        party.isNormalMyths = isNormalMyths.isChecked();
        party.isHardMyths = isHardMyths.isChecked();
        party.isStartingRumor = isStartingRumor.isChecked();
    }

    private void setPartyForEdit() {
        dateField.setText(party.date);
        ancientOneSpinner.setSelection(getItemIndexInArray(ancientOneArray, party.ancientOne));
        playersCountSpinner.setSelection(getItemIndexInArray(playersCountArray, String.valueOf(party.playersCount)));
        isSimpleMyths.setChecked(party.isSimpleMyths);
        isNormalMyths.setChecked(party.isNormalMyths);
        isHardMyths.setChecked(party.isHardMyths);
        isStartingRumor.setChecked(party.isStartingRumor);
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
        ArrayAdapter<String> ancientOneAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.ancientOneArray));
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
        ArrayAdapter<String> playersCountAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, playersCountArray);
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