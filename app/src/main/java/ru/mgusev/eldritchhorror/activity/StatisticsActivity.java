package ru.mgusev.eldritchhorror.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.data.PieEntry;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.database.HelperFactory;
import ru.mgusev.eldritchhorror.model.Game;

public class StatisticsActivity extends AppCompatActivity {

    private static final int DEN = 100;

    private Toolbar toolbarStatistics;
    private List<Game> gameList;
    private int winCount;
    private int defeatCount;
    private Spinner ancientOneSpinner;
    private CardView ancientOneCard;
    private CardView scoreCard;
    private CardView defeatReasonCard;
    private ArrayAdapter<String> ancientOneAdapter;
    private List <String> ancientOneList;
    private List<String[]> scoreResults;
    private List<Float> defeatReasonResults;

    private TextView winDefeatHeader;
    private TextView ancientOneHeader;
    private TextView scoreHeader;
    private TextView defeatReasonHeader;

    private EHChart winDefeatChart;
    private List<Float> winDefeatValues;
    private List<String> winDefeatLabels;

    private EHChart ancientOneChart;
    private List<Float> ancienOneValues;
    private List<String> ancienOneLabels;

    private EHChart scoreChart;
    private List<Float> scoreValues;
    private List<String> scoreLabels;

    private EHChart defeatReasonChart;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        initToolbar();

        gameList = getIntent().getParcelableArrayListExtra("gameList");
        ancientOneCard = (CardView) findViewById(R.id.ancient_one_stat_card);
        scoreCard = (CardView) findViewById(R.id.score_stat_card);
        defeatReasonCard = (CardView) findViewById(R.id.defeat_reason_stat_card);

        winDefeatHeader = findViewById(R.id.win_defeat_stat_header);
        ancientOneHeader = findViewById(R.id.ancient_one_stat_header);
        scoreHeader = findViewById(R.id.score_stat_header);
        defeatReasonHeader = findViewById(R.id.defeat_reason_stat_header);

        try {
            scoreResults = HelperFactory.getHelper().getGameDAO().getScoreCount(0).getResults();
            defeatReasonResults = HelperFactory.getHelper().getGameDAO().getDefeatReasonCount(0);
            System.out.println("555555 " + defeatReasonResults);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        initAncientOneArray();
        initAncientOneSpinner();

        initCharts();
    }

    private void initCharts() {
        initWinDefeatChart();
        initAncientOneChart();
        initScoreChart();
        initDefeatReasonChart();
    }

    private void initToolbar() {
        toolbarStatistics = findViewById(R.id.toolbar_statistics);
        setSupportActionBar(toolbarStatistics);
        getSupportActionBar().setTitle(R.string.statistics_header);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarStatistics.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initWinDefeatChart() {
        winDefeatChart = findViewById(R.id.win_defeat_pie_chart);
        winDefeatChart.setOnChartValueSelectedListener(winDefeatChart);

        initWinDefeatValues();

        List<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < winDefeatValues.size(); i++) {
            entries.add(new PieEntry(getPercent(winDefeatValues.get(i), gameList.size(), DEN), winDefeatLabels.get(i)));
            //System.out.println(winDefeatValues.get(i) + " " + winDefeatLabels.get(i));
        }

        winDefeatChart.setData(entries, getResources().getString(R.string.win_defeat_stat_description), winDefeatLabels, winDefeatValues, gameList.size(), winDefeatHeader);
    }

    private void initWinDefeatValues() {
        winDefeatValues = new ArrayList<>();
        winDefeatLabels = new ArrayList<>();
        winCount = 0;
        defeatCount = 0;

        for (Game game : gameList) {
            if (game.isWinGame) winCount++;
            else defeatCount++;
        }

        if (winCount != 0) {
            winDefeatValues.add((float) winCount);
            winDefeatLabels.add(getResources().getString(R.string.win_stat_label));
        }
        if (defeatCount != 0) {
            winDefeatValues.add((float) defeatCount);
            winDefeatLabels.add(getResources().getString(R.string.defeat_stat_label));
        }
    }

    private float getPercent(float value, int size, int den) {
        return size == 0 ? 0 : value / size * den;
    }

    private void initAncientOneChart() {
        ancientOneChart = findViewById(R.id.ancient_one_pie_chart);
        ancientOneChart.setOnChartValueSelectedListener(ancientOneChart);

        ancienOneValues = new ArrayList<>();
        ancienOneLabels = new ArrayList<>();
        try {
            for (String[] array : HelperFactory.getHelper().getGameDAO().getAncientOneCount().getResults()) {
                ancienOneLabels.add(HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneNameByID(Integer.parseInt(array[0])));
                ancienOneValues.add(Float.valueOf(array[1]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < ancienOneValues.size(); i++) {
            entries.add(new PieEntry(getPercent(ancienOneValues.get(i), gameList.size(), DEN), ancienOneLabels.get(i)));
            System.out.println(ancienOneValues.get(i) + " " + ancienOneLabels.get(i));
        }

        ancientOneChart.setData(entries, getResources().getString(R.string.ancientOne), ancienOneLabels, ancienOneValues, gameList.size(), ancientOneHeader);
    }

    private void initScoreChart() {
        scoreChart = findViewById(R.id.score_pie_chart);
        scoreChart.setOnChartValueSelectedListener(scoreChart);

        scoreValues = new ArrayList<>();
        scoreLabels = new ArrayList<>();
        for (String[] array : scoreResults) {
            System.out.println("45454 " + array[1] + " " + array[0] + " " + Float.valueOf(array[1]));
            scoreLabels.add(array[0]);
            scoreValues.add(Float.valueOf(array[1]));
        }

        List<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < scoreValues.size(); i++) {
            entries.add(new PieEntry(getPercent(scoreValues.get(i), winCount, DEN), scoreLabels.get(i)));
            //System.out.println("123 " + scoreValues.get(i) + " " + scoreLabels.get(i));
        }

        scoreChart.setData(entries, getResources().getString(R.string.totalScore), scoreLabels, scoreValues, winCount, scoreHeader);
    }

    public void initAncientOneArray() {
        try {
            if (ancientOneList == null) ancientOneList = new ArrayList<>();

            List<Integer> allAncientOnes = new ArrayList<>();
            for (Game game : gameList) {
                if (!allAncientOnes.contains(game.ancientOneID)) {
                    allAncientOnes.add(game.ancientOneID);
                    ancientOneList.add(HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneNameByID(game.ancientOneID));
                }
            }
            Collections.sort(ancientOneList);
            ancientOneList.add(0, getResources().getString(R.string.all_results));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initDefeatReasonChart() {
        defeatReasonChart = findViewById(R.id.defeat_reason_pie_chart);
        defeatReasonChart.setOnChartValueSelectedListener(defeatReasonChart);

        scoreValues = defeatReasonResults;
        scoreLabels = new ArrayList<>();

        scoreLabels.add(getResources().getString(R.string.defeat_by_awakened_ancient_one));
        scoreLabels.add(getResources().getString(R.string.defeat_by_elimination));
        scoreLabels.add(getResources().getString(R.string.defeat_by_mythos_depletion));

        int defeatSum = Math.round(scoreValues.get(0));
        scoreValues.remove(0);


        List<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < scoreValues.size();) {
            if (scoreValues.get(i) == 0) {
                scoreValues.remove(i);
                scoreLabels.remove(i);
            } else {
                entries.add(new PieEntry(getPercent(scoreValues.get(i), defeatSum, DEN), scoreLabels.get(i)));
                i++;
            }
        }

        defeatReasonChart.setData(entries, getResources().getString(R.string.defeat_header), scoreLabels, scoreValues, defeatSum, defeatReasonHeader);
        if (scoreValues.isEmpty()) setDefeatReasonCardVisibility(false);
        else setDefeatReasonCardVisibility(true);
    }

    public void initAncientOneSpinner() {
        ancientOneAdapter = new ArrayAdapter<String>(this, R.layout.spinner, ancientOneList);
        ancientOneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ancientOneSpinner = (Spinner) findViewById(R.id.ancientOneStatSpinner);
        ancientOneSpinner.setAdapter(ancientOneAdapter);

        ancientOneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if (i == 0) {
                        gameList = HelperFactory.getHelper().getGameDAO().getGamesSortAncientOne();
                        scoreResults = HelperFactory.getHelper().getGameDAO().getScoreCount(0).getResults();
                        defeatReasonResults = HelperFactory.getHelper().getGameDAO().getDefeatReasonCount(0);
                        setAncientOneCardVisibility(true);
                        setScoreCardVisibility(true);
                        initCharts();
                    } else {
                        gameList = HelperFactory.getHelper().getGameDAO().getGamesByAncientOne(ancientOneList.get(i));
                        scoreResults = HelperFactory.getHelper().getGameDAO().getScoreCount(HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneIDByName(ancientOneList.get(i))).getResults();
                        defeatReasonResults = HelperFactory.getHelper().getGameDAO().getDefeatReasonCount(HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneIDByName(ancientOneList.get(i)));
                        if (scoreResults.isEmpty()) setScoreCardVisibility(false);
                        else setScoreCardVisibility(true);
                        setAncientOneCardVisibility(false);
                        initCharts();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setAncientOneCardVisibility(boolean value) {
        if (value)ancientOneCard.setVisibility(View.VISIBLE);
        else ancientOneCard.setVisibility(View.GONE);
    }

    private void setScoreCardVisibility(boolean value) {
        if (value)scoreCard.setVisibility(View.VISIBLE);
        else scoreCard.setVisibility(View.GONE);
    }

    private void setDefeatReasonCardVisibility(boolean value) {
        if (value)defeatReasonCard.setVisibility(View.VISIBLE);
        else defeatReasonCard.setVisibility(View.GONE);
    }
}