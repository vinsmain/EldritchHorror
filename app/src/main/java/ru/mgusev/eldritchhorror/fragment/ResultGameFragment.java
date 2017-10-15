package ru.mgusev.eldritchhorror.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import ru.mgusev.eldritchhorror.eh_interface.PassMeLinkOnObject;
import ru.mgusev.eldritchhorror.R;

public class ResultGameFragment extends Fragment implements TextWatcher {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    int pageNumber;

    View view;
    PassMeLinkOnObject activity;

    EditText gatesCount;
    EditText monstersCount;
    EditText curseCount;
    EditText rumorsCount;
    EditText cluesCount;
    EditText blessedCount;
    EditText doomCount;
    TextView score;

    public void setActivity(PassMeLinkOnObject activity) {
        this.activity = activity;
    }

    public static ResultGameFragment newInstance(int page, PassMeLinkOnObject activity) {
        ResultGameFragment fragment = new ResultGameFragment();
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
        view = inflater.inflate(R.layout.fragment_result_game, null);

        initActivityElements();
        setListeners();

        addGameValuesToFields();

        return view;
    }

    private void setListeners() {
        gatesCount.addTextChangedListener(this);
        monstersCount.addTextChangedListener(this);
        curseCount.addTextChangedListener(this);
        rumorsCount.addTextChangedListener(this);
        cluesCount.addTextChangedListener(this);
        blessedCount.addTextChangedListener(this);
        doomCount.addTextChangedListener(this);
    }

    private void initActivityElements() {
        gatesCount = (EditText) view.findViewById(R.id.gatesCount);
        monstersCount = (EditText) view.findViewById(R.id.monstersCount);
        curseCount = (EditText) view.findViewById(R.id.curseCount);
        rumorsCount = (EditText) view.findViewById(R.id.rumorsCount);
        cluesCount = (EditText) view.findViewById(R.id.cluesCount);
        blessedCount = (EditText) view.findViewById(R.id.blessedCount);
        doomCount = (EditText) view.findViewById(R.id.doomCount);
        score = (TextView) getActivity().findViewById(R.id.score_pager);
    }

    public void addDataToGame() {
        if (view != null) {
            activity.getGame().gatesCount = getResultToField(gatesCount);
            activity.getGame().monstersCount = getResultToField(monstersCount);
            activity.getGame().curseCount = getResultToField(curseCount);
            activity.getGame().rumorsCount = getResultToField(rumorsCount);
            activity.getGame().cluesCount = getResultToField(cluesCount);
            activity.getGame().blessedCount = getResultToField(blessedCount);
            activity.getGame().doomCount = getResultToField(doomCount);
            activity.getGame().score = getScore();
        }
    }

    private void addGameValuesToFields() {
        int i = activity.getGame().gatesCount;
        gatesCount.setText(i == 0 ? "" : String.valueOf(i));

        i = activity.getGame().monstersCount;
        monstersCount.setText(i == 0 ? "" : String.valueOf(i));

        i = activity.getGame().curseCount;
        curseCount.setText(i == 0 ? "" : String.valueOf(i));

        i = activity.getGame().rumorsCount;
        rumorsCount.setText(i == 0 ? "" : String.valueOf(i));

        i = activity.getGame().cluesCount;
        cluesCount.setText(i == 0 ? "" : String.valueOf(i));

        i = activity.getGame().blessedCount;
        blessedCount.setText(i == 0 ? "" : String.valueOf(i));

        i = activity.getGame().doomCount;
        doomCount.setText(i == 0 ? "" : String.valueOf(i));
    }

    private int getScore() {
        return getResultToField(gatesCount) + (int)Math.ceil(getResultToField(monstersCount) / 3.0f) + getResultToField(curseCount)
                + getResultToField(rumorsCount) * 3 - (int)Math.ceil(getResultToField(cluesCount) / 3.0f) - getResultToField(blessedCount) - getResultToField(doomCount);
    }

    private int getResultToField(EditText editText) {
        if (editText.getText().toString().equals("")) return 0;
        else return Integer.parseInt(editText.getText().toString());
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        refreshScore();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void refreshScore() {
        score.setText(String.valueOf(getScore()));
    }
}
