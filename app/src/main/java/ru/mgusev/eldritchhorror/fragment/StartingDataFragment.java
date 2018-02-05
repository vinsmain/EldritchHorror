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
import ru.mgusev.eldritchhorror.model.Prelude;

public class StartingDataFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    public int pageNumber;

    View view;
    PassMeLinkOnObject activity;
    ImageView expansionImage;

    TableRow dateRow;
    TableRow ancientOneRow;
    TableRow preludeRow;
    TableRow playersCountRow;
    TextView dateField;
    Spinner ancientOneSpinner;
    public ArrayAdapter<String> ancientOneAdapter;
    Spinner preludeSpinner;
    public ArrayAdapter<String> preludeAdapter;
    Spinner playersCountSpinner;
    Switch isSimpleMyths;
    Switch isNormalMyths;
    Switch isHardMyths;
    Switch isStartingRumor;
    List <String> ancientOneList;
    List <String> preludeList;
    String[] playersCountArray;
    String currentAncientOneName;
    String currentPreludeName;

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
        preludeRow = (TableRow) view.findViewById(R.id.prelude_row);
        playersCountRow = (TableRow) view.findViewById(R.id.players_count_row);
        dateRow.setOnClickListener(this);
        ancientOneRow.setOnClickListener(this);
        preludeRow.setOnClickListener(this);
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
        initPreludeArray();
        playersCountArray = getResources().getStringArray(R.array.playersCountArray);

        initAncientOneSpinner();
        initPreludeSpinner();
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

    public void initPreludeArray() {
        try {
            if (preludeList == null) preludeList = new ArrayList<>();
            boolean flag = false;
            preludeList.clear();
            preludeList.addAll(HelperFactory.getStaticHelper().getPreludeDAO().getPreludeNameList());
            for (String name : preludeList) {
                if (name.equals(HelperFactory.getStaticHelper().getPreludeDAO().getPreludeNameByID(activity.getGame().preludeID))) {
                    currentPreludeName = name;
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                preludeList.add(HelperFactory.getStaticHelper().getPreludeDAO().getPreludeNameByID(activity.getGame().preludeID));
                Collections.sort(preludeList);
            }
            preludeList.remove(HelperFactory.getStaticHelper().getPreludeDAO().getPreludeNameByID(0));
            preludeList.add(0, HelperFactory.getStaticHelper().getPreludeDAO().getPreludeNameByID(0));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        try {
            activity.getGame().date = MainActivity.formatter.parse(dateField.getText().toString()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date_row:
                Bundle args = new Bundle();
                args.putString("date", (String) dateField.getText());
                DialogFragment dateDialog = new DatePicker();
                dateDialog.setArguments(args);
                dateDialog.show(getActivity().getFragmentManager(), "datePicker");
                break;
            case R.id.ancient_one_row:
                ancientOneSpinner.performClick();
                break;
            case R.id.prelude_row:
                preludeSpinner.performClick();
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
                activity.getGame().date = MainActivity.formatter.parse(dateField.getText().toString()).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                activity.getGame().ancientOneID = HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneIDByName(ancientOneList.get(ancientOneSpinner.getSelectedItemPosition()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                activity.getGame().preludeID = HelperFactory.getStaticHelper().getPreludeDAO().getPreludeIDByName(preludeList.get(preludeSpinner.getSelectedItemPosition()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            activity.getGame().playersCount = getPlayersCount();
            activity.getGame().isSimpleMyths = isSimpleMyths.isChecked();
            activity.getGame().isNormalMyths = isNormalMyths.isChecked();
            activity.getGame().isHardMyths = isHardMyths.isChecked();
            activity.getGame().isStartingRumor = isStartingRumor.isChecked();
        }
    }

    public int getPlayersCount() {
        return Integer.parseInt(playersCountArray[playersCountSpinner.getSelectedItemPosition()]);
    }

    public void setDataToFields() {
        dateField.setText(MainActivity.formatter.format(activity.getGame().date));
        try {
            ancientOneSpinner.setSelection(getItemIndexInArray(ancientOneList.toArray(new String[ancientOneList.size()]),
                    HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneNameByID(activity.getGame().ancientOneID)));

            preludeSpinner.setSelection(getItemIndexInArray(preludeList.toArray(new String[preludeList.size()]),
                    HelperFactory.getStaticHelper().getPreludeDAO().getPreludeNameByID(activity.getGame().preludeID)));
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
                ((ResultGameFragment)activity.getPagerAdapter().getItem(2)).setVisibilityRadioButtons();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void initPreludeSpinner() {
        preludeAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner, preludeList);
        preludeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        preludeSpinner = (Spinner) view.findViewById(R.id.preludeSpinner);
        preludeSpinner.setAdapter(preludeAdapter);

        preludeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                addDataToGame();
                initPreludeArray();
                preludeAdapter.notifyDataSetChanged();
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
                activity.getGame().playersCount = getPlayersCount();
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

    public void selectRandomAncientOne() {
        int j = 0;
        try {
            do {
                j = (int) (Math.random() * ancientOneList.size());
            } while (!HelperFactory.getStaticHelper().getExpansionDAO().isEnableByID(getAncientOneExpansionID(ancientOneList.get(j))) || ancientOneSpinner.getSelectedItemPosition() == j);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ancientOneSpinner.setSelection(j);
    }

    public void selectRandomPrelude() {
        if (preludeList.size() > 2) {
            int j = 0;
            try {
                do {
                    j = (int) (Math.random() * preludeList.size());
                }
                while (!HelperFactory.getStaticHelper().getExpansionDAO().isEnableByID(getPreludeExpansionID(preludeList.get(j))) || preludeSpinner.getSelectedItemPosition() == j || j == 0);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            preludeSpinner.setSelection(j);
        }
    }

    private int getAncientOneExpansionID(String name) {
        AncientOne ancientOne = new AncientOne();
        try {
            ancientOne = HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ancientOne.expansionID;
    }

    private int getPreludeExpansionID(String name) {
        Prelude prelude = new Prelude();
        try {
            prelude = HelperFactory.getStaticHelper().getPreludeDAO().getPreludeByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prelude.expansionID;
    }
}