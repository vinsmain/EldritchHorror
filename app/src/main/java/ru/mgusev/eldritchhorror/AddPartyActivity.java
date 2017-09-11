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

public class AddPartyActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    ImageView dateButton;
    TextView dateField;
    Spinner ancientOneSpinner;
    Spinner playersCountSpinner;
    CheckBox isSimpleMyths;
    CheckBox isNormalMyths;
    CheckBox isHardMyths;
    CheckBox isStartingMyth;
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
        dateField.setText(new SimpleDateFormat("dd.MM.yyyy").format(new Date()));

        isSimpleMyths = (CheckBox) findViewById(R.id.isSimpleMyths);
        isNormalMyths = (CheckBox) findViewById(R.id.isNormalMyths);
        isHardMyths = (CheckBox) findViewById(R.id.isHardMyths);
        isStartingMyth = (CheckBox) findViewById(R.id.isStartingMyth);

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);

        ancientOneArray = getResources().getStringArray(R.array.ancientOneArray);
        playersCountArray = getResources().getStringArray(R.array.playersCountArray);

        initAncientOneSpinner();
        initPlayersCountSpinner();
        initToolbar();
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
                intent.putExtra("party", createParty());
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private Party createParty() {
        String date = dateField.getText().toString();
        String ancientOne = ancientOneArray[ancientOneSpinner.getSelectedItemPosition()];
        int playersCount = Integer.parseInt(playersCountArray[playersCountSpinner.getSelectedItemPosition()]);
        boolean isSimpleMythsChecked = isSimpleMyths.isChecked();
        boolean isNormalMythsChecked = isNormalMyths.isChecked();
        boolean isHardMythsChecked = isHardMyths.isChecked();
        boolean isStartingMythChecked = isStartingMyth.isChecked();

        return new Party(date, ancientOne, playersCount, isSimpleMythsChecked, isNormalMythsChecked, isHardMythsChecked, isStartingMythChecked);
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
