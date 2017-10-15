package ru.mgusev.eldritchhorror.fragment;

import android.app.DialogFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.sql.SQLException;
import java.text.ParseException;

import ru.mgusev.eldritchhorror.activity.MainActivity;
import ru.mgusev.eldritchhorror.database.HelperFactory;
import ru.mgusev.eldritchhorror.eh_interface.PassMeLinkOnObject;
import ru.mgusev.eldritchhorror.R;

public class StartingDataFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

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

    public void setActivity(PassMeLinkOnObject activity) {
        this.activity = activity;
    }

    public static StartingDataFragment newInstance(int page, PassMeLinkOnObject activity) {
        StartingDataFragment fragment = new StartingDataFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        fragment.setActivity(activity);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_starting_data, null);

        dateButton = (ImageButton) view.findViewById(R.id.dateImgBtn);
        dateButton.setOnClickListener(this);
        dateField = (TextView) view.findViewById(R.id.dateFieldq);

        isSimpleMyths = (Switch) view.findViewById(R.id.isSimpleMyths);
        isNormalMyths = (Switch) view.findViewById(R.id.isNormalMyths);
        isHardMyths = (Switch) view.findViewById(R.id.isHardMyths);
        isStartingRumor = (Switch) view.findViewById(R.id.isStartingMyth);

        isSimpleMyths.setOnCheckedChangeListener(this);
        isNormalMyths.setOnCheckedChangeListener(this);
        isHardMyths.setOnCheckedChangeListener(this);

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

    @Override
    public void onPause() {
        try {
            activity.getGame().date = MainActivity.formatter.parse(dateField.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dateImgBtn:
                Bundle args=new Bundle();
                args.putString("date", (String) dateField.getText());
                DialogFragment dateDialog = new DatePicker();
                dateDialog.setArguments(args);
                dateDialog.show(getActivity().getFragmentManager(), "datePicker");
                break;
            default:
                break;
        }
    }

    public void addDataToGame() {
        if (view != null) {
            try {
                activity.getGame().date = MainActivity.formatter.parse(dateField.getText().toString());
                System.out.println(activity.getGame().date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
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
    }

    private void setDataToFields() {
        dateField.setText(MainActivity.formatter.format(activity.getGame().date));
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
                final int resourceId;
                Resources resources = getResources();
                try {
                    resourceId = resources.getIdentifier(HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneImageResourceByID(i + 1), "drawable", getActivity().getPackageName());
                    ((ImageView)getActivity().findViewById(R.id.background_pager)).setImageResource(resourceId);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.isSimpleMyths:
                if (!b && !isNormalMyths.isChecked() && !isHardMyths.isChecked()) isNormalMyths.setChecked(true);
                break;
            case R.id.isNormalMyths:
                if (!b && !isSimpleMyths.isChecked() && !isHardMyths.isChecked()) isSimpleMyths.setChecked(true);
                break;
            case R.id.isHardMyths:
                if (!b && !isSimpleMyths.isChecked() && !isNormalMyths.isChecked()) isSimpleMyths.setChecked(true);
                break;
            default:
                break;
        }
    }
}