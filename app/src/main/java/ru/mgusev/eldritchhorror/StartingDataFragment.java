package ru.mgusev.eldritchhorror;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StartingDataFragment extends Fragment implements View.OnClickListener {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    int pageNumber;

    View view;
    PassMeLinkOnObject activity;

    ImageButton dateButton;
    TextView dateField;
    Spinner ancientOneSpinner;
    Spinner playersCountSpinner;
    Switch isSimpleMyths;
    Switch isNormalMyths;
    Switch isHardMyths;
    Switch isStartingRumor;
    String[] ancientOneArray;
    String[] playersCountArray;

    static StartingDataFragment newInstance(int page) {
        StartingDataFragment pageFragment = new StartingDataFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (PassMeLinkOnObject)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_starting_data, null);

        dateButton = (ImageButton) view.findViewById(R.id.dateImgBtn);
        dateButton.setOnClickListener(this);
        dateField = (TextView) view.findViewById(R.id.dateField);
        dateField.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date()));

        isSimpleMyths = (Switch) view.findViewById(R.id.isSimpleMyths);
        isNormalMyths = (Switch) view.findViewById(R.id.isNormalMyths);
        isHardMyths = (Switch) view.findViewById(R.id.isHardMyths);
        isStartingRumor = (Switch) view.findViewById(R.id.isStartingMyth);

        try {
            ancientOneArray = HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneArray();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        playersCountArray = getResources().getStringArray(R.array.playersCountArray);

        initAncientOneSpinner();
        initPlayersCountSpinner();

        setDataToFields();


        return view;
    }

    /*private void initToolbar() {
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
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dateImgBtn:
                DialogFragment dateDialog = new DatePicker();
                dateDialog.show(getActivity().getFragmentManager(), "datePicker");
                break;
            /*case R.id.nextButton:
                Intent intent = new Intent(getContext(), InvestigatorsChoiceAvtivity.class);
                if (isEdit) addDataToGame();
                else {
                    game = new Game();
                    addDataToGame();
                }
                intent.putExtra("game", game);
                startActivity(intent);
                break;*/
            default:
                break;
        }
    }

    public void addDataToGame() {
        System.out.println(activity.getGame());
        activity.getGame().date = dateField.getText().toString();
        try {
            activity.getGame().ancientOneID = HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneIDByName(ancientOneArray[ancientOneSpinner.getSelectedItemPosition()]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        activity.getGame().playersCount = Integer.parseInt(playersCountArray[playersCountSpinner.getSelectedItemPosition()]);
        activity.getGame().isSimpleMyths = isSimpleMyths.isChecked();
        activity.getGame().isNormalMyths = isNormalMyths.isChecked();
        activity.getGame().isHardMyths = isHardMyths.isChecked();
        activity.getGame().isStartingRumor = isStartingRumor.isChecked();
    }

    private void setDataToFields() {
        dateField.setText(activity.getGame().date);
        try {
            ancientOneSpinner.setSelection(getItemIndexInArray(ancientOneArray, HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneNameByID(activity.getGame().ancientOneID)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        playersCountSpinner.setSelection(getItemIndexInArray(playersCountArray, String.valueOf(activity.getGame().playersCount)));
        isSimpleMyths.setChecked(activity.getGame().isSimpleMyths);
        isNormalMyths.setChecked(activity.getGame().isNormalMyths);
        isHardMyths.setChecked(activity.getGame().isHardMyths);
        isStartingRumor.setChecked(activity.getGame().isStartingRumor);
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
        ArrayAdapter<String> ancientOneAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner, ancientOneArray);
        ancientOneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ancientOneSpinner = (Spinner) view.findViewById(R.id.ancientOneSpinner);
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
        ArrayAdapter<String> playersCountAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner, playersCountArray);
        playersCountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        playersCountSpinner = (Spinner) view.findViewById(R.id.playersCountSpinner);
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
