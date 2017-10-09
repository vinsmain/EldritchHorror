package ru.mgusev.eldritchhorror.fragment;

import android.content.Context;
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
        System.out.println("123");
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

        System.out.println("123456");
        initActivityElements();
        setListeners();

        addGameValuesToFields();

        /*game = (Game) getIntent().getParcelableExtra("game");
        if (game.id != -1) addGameValuesToFields();
        else refreshScore();*/

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
        //score = (TextView) view.findViewById(R.id.totalScore); TODO Добавить вывод результата игры
    }

    /*private void initToolbar() {
        toolbar = (Toolbar) view.findViewById(R.id.toolbarInvChoice);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.activity_result_party_header);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }*/

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
        gatesCount.setText(String.valueOf(activity.getGame().gatesCount));
        monstersCount.setText(String.valueOf(activity.getGame().monstersCount));
        curseCount.setText(String.valueOf(activity.getGame().curseCount));
        rumorsCount.setText(String.valueOf(activity.getGame().rumorsCount));
        cluesCount.setText(String.valueOf(activity.getGame().cluesCount));
        blessedCount.setText(String.valueOf(activity.getGame().blessedCount));
        doomCount.setText(String.valueOf(activity.getGame().doomCount));
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
        //score.setText(String.valueOf(getScore()));
    }
}
