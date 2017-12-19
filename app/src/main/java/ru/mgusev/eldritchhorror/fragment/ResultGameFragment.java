package ru.mgusev.eldritchhorror.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import android.support.annotation.NonNull;

import ru.mgusev.eldritchhorror.eh_interface.PassMeLinkOnObject;
import ru.mgusev.eldritchhorror.R;

public class ResultGameFragment extends Fragment implements TextWatcher, CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    int pageNumber;

    View view;
    PassMeLinkOnObject activity;

    TableLayout winTable;
    TableLayout defeatTable;

    TextView resultGameText;
    Switch resultGameSwitch;
    Switch defeatByElimination;
    Switch defeatByMythosDeplition;
    Switch defeatByAwakenedAncientOne;

    TableRow gatesCountRow;
    TableRow monstersCountRow;
    TableRow curseCountRow;
    TableRow rumorsCountRow;
    TableRow cluesCountRow;
    TableRow blessedCountRow;
    TableRow doomCountRow;

    EditText gatesCount;
    EditText monstersCount;
    EditText curseCount;
    EditText rumorsCount;
    EditText cluesCount;
    EditText blessedCount;
    EditText doomCount;

    ImageView winImage;
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
        setRetainInstance(true);
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
        resultGameSwitch.setOnCheckedChangeListener(this);
        defeatByElimination.setOnCheckedChangeListener(this);
        defeatByMythosDeplition.setOnCheckedChangeListener(this);
        defeatByAwakenedAncientOne.setOnCheckedChangeListener(this);
        gatesCount.addTextChangedListener(this);
        monstersCount.addTextChangedListener(this);
        curseCount.addTextChangedListener(this);
        rumorsCount.addTextChangedListener(this);
        cluesCount.addTextChangedListener(this);
        blessedCount.addTextChangedListener(this);
        doomCount.addTextChangedListener(this);
        gatesCountRow.setOnClickListener(this);
        monstersCountRow.setOnClickListener(this);
        curseCountRow.setOnClickListener(this);
        rumorsCountRow.setOnClickListener(this);
        cluesCountRow.setOnClickListener(this);
        blessedCountRow.setOnClickListener(this);
        doomCountRow.setOnClickListener(this);
    }

    private void initActivityElements() {
        winTable = (TableLayout) view.findViewById(R.id.winTable);
        defeatTable = (TableLayout) view.findViewById(R.id.defeatTable);
        resultGameText = (TextView) view.findViewById(R.id.resultGameText);
        resultGameSwitch = (Switch) view.findViewById(R.id.resultGameSwitch);
        defeatByElimination = (Switch) view.findViewById(R.id.defeatByElimination);
        defeatByMythosDeplition = (Switch) view.findViewById(R.id.defeatByMythosDeplition);
        defeatByAwakenedAncientOne = (Switch) view.findViewById(R.id.defeatByAwakenedAncientOne);
        gatesCount = (EditText) view.findViewById(R.id.gatesCount);
        monstersCount = (EditText) view.findViewById(R.id.monstersCount);
        curseCount = (EditText) view.findViewById(R.id.curseCount);
        rumorsCount = (EditText) view.findViewById(R.id.rumorsCount);
        cluesCount = (EditText) view.findViewById(R.id.cluesCount);
        blessedCount = (EditText) view.findViewById(R.id.blessedCount);
        doomCount = (EditText) view.findViewById(R.id.doomCount);
        winImage = (ImageView) getActivity().findViewById(R.id.win_image);
        score = (TextView) getActivity().findViewById(R.id.score_pager);
        gatesCountRow = (TableRow) view.findViewById(R.id.gatesCountRow);
        monstersCountRow = (TableRow) view.findViewById(R.id.monstersCountRow);
        curseCountRow = (TableRow) view.findViewById(R.id.curseCountRow);
        rumorsCountRow = (TableRow) view.findViewById(R.id.rumorsCountRow);
        cluesCountRow = (TableRow) view.findViewById(R.id.cluesCountRow);
        blessedCountRow = (TableRow) view.findViewById(R.id.blessedCountRow);
        doomCountRow = (TableRow) view.findViewById(R.id.doomCountRow);
    }

    public void addDataToGame() {
        if (view != null) {
            activity.getGame().isWinGame = resultGameSwitch.isChecked();
            addDefeatReasonsToGame();
            if (resultGameSwitch.isChecked()) {
                activity.getGame().gatesCount = getResultToField(gatesCount);
                activity.getGame().monstersCount = getResultToField(monstersCount);
                activity.getGame().curseCount = getResultToField(curseCount);
                activity.getGame().rumorsCount = getResultToField(rumorsCount);
                activity.getGame().cluesCount = getResultToField(cluesCount);
                activity.getGame().blessedCount = getResultToField(blessedCount);
                activity.getGame().doomCount = getResultToField(doomCount);
                activity.getGame().score = getScore();
            } else cleanEditText();
        }
    }

    private void addGameValuesToFields() {
        resultGameSwitch.setChecked(activity.getGame().isWinGame);
        defeatByElimination.setChecked(activity.getGame().isDefeatByElimination);
        defeatByMythosDeplition.setChecked(activity.getGame().isDefeatByMythosDepletion);
        defeatByAwakenedAncientOne.setChecked(activity.getGame().isDefeatByAwakenedAncientOne);

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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.resultGameSwitch:
                if (b) {
                    resultGameText.setText(R.string.win_header);
                    defeatTable.setVisibility(View.GONE);
                    winTable.setVisibility(View.VISIBLE);
                    winImage.setImageResource(R.drawable.stars);
                    score.setVisibility(View.VISIBLE);
                }
                else {
                    if (activity.getGame().isDefeatByAwakenedAncientOne) winImage.setImageResource(R.drawable.skull);
                    else if (activity.getGame().isDefeatByElimination) winImage.setImageResource(R.drawable.inestigators_out);
                    else if (activity.getGame().isDefeatByMythosDepletion) winImage.setImageResource(R.drawable.mythos_empty);
                    resultGameText.setText(R.string.defeat_header);
                    defeatTable.setVisibility(View.VISIBLE);
                    winTable.setVisibility(View.GONE);
                    score.setVisibility(View.GONE);

                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
                break;
            case R.id.defeatByElimination:
                if (b) {
                    defeatByMythosDeplition.setChecked(false);
                    defeatByAwakenedAncientOne.setChecked(false);
                    winImage.setImageResource(R.drawable.inestigators_out);
                    addDefeatReasonsToGame();
                }
                break;
            case R.id.defeatByMythosDeplition:
                if (b) {
                    defeatByElimination.setChecked(false);
                    defeatByAwakenedAncientOne.setChecked(false);
                    winImage.setImageResource(R.drawable.mythos_empty);
                    addDefeatReasonsToGame();
                }
                break;
            case R.id.defeatByAwakenedAncientOne:
                if (b) {
                    defeatByElimination.setChecked(false);
                    defeatByMythosDeplition.setChecked(false);
                    winImage.setImageResource(R.drawable.skull);
                    addDefeatReasonsToGame();
                }
                break;
            default:
                break;
        }
    }

    private void addDefeatReasonsToGame() {
        activity.getGame().isDefeatByElimination = defeatByElimination.isChecked();
        activity.getGame().isDefeatByMythosDepletion = defeatByMythosDeplition.isChecked();
        activity.getGame().isDefeatByAwakenedAncientOne = defeatByAwakenedAncientOne.isChecked();
    }

    private void cleanEditText() {
        activity.getGame().gatesCount = 0;
        activity.getGame().monstersCount = 0;
        activity.getGame().curseCount = 0;
        activity.getGame().rumorsCount = 0;
        activity.getGame().cluesCount = 0;
        activity.getGame().blessedCount = 0;
        activity.getGame().doomCount = 0;
        activity.getGame().score = 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gatesCountRow:
                showSoftKeyboardOnView(gatesCount);
                break;
            case R.id.monstersCountRow:
                showSoftKeyboardOnView(monstersCount);
                break;
            case R.id.curseCountRow:
                showSoftKeyboardOnView(curseCount);
                break;
            case R.id.rumorsCountRow:
                showSoftKeyboardOnView(rumorsCount);
                break;
            case R.id.cluesCountRow:
                showSoftKeyboardOnView(cluesCount);
                break;
            case R.id.blessedCountRow:
                showSoftKeyboardOnView(blessedCount);
                break;
            case R.id.doomCountRow:
                showSoftKeyboardOnView(doomCount);
                break;
            default:
                break;
        }
    }

    public static void showSoftKeyboardOnView(@NonNull final View view) {
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                view.requestFocus();
                // Yes, I know what you are thinking about that. If you knew something better by any chance it would be magnificent to have your idea here in code.
                view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
            }
        }, 200);
    }
}