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
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.mgusev.eldritchhorror.activity.MainActivity;
import ru.mgusev.eldritchhorror.database.HelperFactory;
import ru.mgusev.eldritchhorror.eh_interface.PassMeLinkOnObject;
import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.model.AncientOne;

public class StartingDataFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    public int pageNumber;

    View view;
    PassMeLinkOnObject activity;
    ImageView expansionImage;

    TableRow dateRow;
    TableRow ancientOneRow;
    TableRow playersCountRow;
    TextView dateField;
    Spinner ancientOneSpinner;
    public ArrayAdapter<String> ancientOneAdapter;
    Spinner playersCountSpinner;
    Switch isSimpleMyths;
    Switch isNormalMyths;
    Switch isHardMyths;
    Switch isStartingRumor;
    List <String> ancientOneList;
    String[] playersCountArray;
    String currentAncientOneName;

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

    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_starting_data, null);

        expansionImage = (ImageView) getActivity().findViewById(R.id.expansion_image_pager);

        dateRow = (TableRow) view.findViewById(R.id.date_row);
        ancientOneRow = (TableRow) view.findViewById(R.id.ancient_one_row);
        playersCountRow = (TableRow) view.findViewById(R.id.players_count_row);
        dateRow.setOnClickListener(this);
        ancientOneRow.setOnClickListener(this);
        playersCountRow.setOnClickListener(this);

        dateField = (TextView) view.findViewById(R.id.dateField);

        isSimpleMyths = (Switch) view.findViewById(R.id.isSimpleMyths);
        isNormalMyths = (Switch) view.findViewById(R.id.isNormalMyths);
        isHardMyths = (Switch) view.findViewById(R.id.isHardMyths);
        isStartingRumor = (Switch) view.findViewById(R.id.isStartingMyth);

        isSimpleMyths.setOnCheckedChangeListener(this);
        isNormalMyths.setOnCheckedChangeListener(this);
        isHardMyths.setOnCheckedChangeListener(this);

        initAncientOneArray();
        playersCountArray = getResources().getStringArray(R.array.playersCountArray);

        initAncientOneSpinner();
        initPlayersCountSpinner();
        setDataToFields();

        return view;
    }

    public void initAncientOneArray() {
        try {
            if (ancientOneList == null) ancientOneList = new ArrayList<>();
            boolean flag = false;
            ancientOneList.clear();
            ancientOneList.addAll(HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneNameList());
            if (activity.getGame().ancientOneID == -1) activity.getGame().ancientOneID = HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneIDByName(ancientOneList.get(0));
                for (String name : ancientOneList) {
                if (name.equals(HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneNameByID(activity.getGame().ancientOneID))) {
                    currentAncientOneName = name;
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                ancientOneList.add(HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneNameByID(activity.getGame().ancientOneID));
                Collections.sort(ancientOneList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            case R.id.date_row:
                Bundle args=new Bundle();
                args.putString("date", (String) dateField.getText());
                DialogFragment dateDialog = new DatePicker();
                dateDialog.setArguments(args);
                dateDialog.show(getActivity().getFragmentManager(), "datePicker");
                break;
            case R.id.ancient_one_row:
                ancientOneSpinner.performClick();
                break;
            case R.id.players_count_row:
                playersCountSpinner.performClick();
                break;
            default:
                break;
        }
    }

    public void addDataToGame() {
        if (view != null) {
            try {
                activity.getGame().date = MainActivity.formatter.parse(dateField.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                activity.getGame().ancientOneID = HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneIDByName(ancientOneList.get(ancientOneSpinner.getSelectedItemPosition()));
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

    public void setDataToFields() {
        dateField.setText(MainActivity.formatter.format(activity.getGame().date));
        try {
            ancientOneSpinner.setSelection(getItemIndexInArray(ancientOneList.toArray(new String[ancientOneList.size()]),
                    HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneNameByID(activity.getGame().ancientOneID)));
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

    public void initAncientOneSpinner() {
        ancientOneAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner, ancientOneList);
        ancientOneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ancientOneSpinner = (Spinner) view.findViewById(R.id.ancientOneSpinner);
        ancientOneSpinner.setAdapter(ancientOneAdapter);

        ancientOneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int resourceId;
                AncientOne selectedAncientOne;
                Resources resources = getResources();
                try {
                    selectedAncientOne = HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneByName(ancientOneList.get(i));
                    resourceId = resources.getIdentifier(selectedAncientOne.imageResource, "drawable", getActivity().getPackageName());
                    ((ImageView)getActivity().findViewById(R.id.background_pager)).setImageResource(resourceId);
                    String resourceName = HelperFactory.getStaticHelper().getExpansionDAO().getImageResourceByID(selectedAncientOne.expansionID);
                    if (resourceName != null) {
                        resourceId = resources.getIdentifier(resourceName, "drawable", getContext().getPackageName());
                        expansionImage.setImageResource(resourceId);
                        expansionImage.setVisibility(View.VISIBLE);
                    } else expansionImage.setVisibility(View.GONE);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                addDataToGame();
                initAncientOneArray();
                ancientOneAdapter.notifyDataSetChanged();
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