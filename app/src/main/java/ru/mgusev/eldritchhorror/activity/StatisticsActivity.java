package ru.mgusev.eldritchhorror.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.data.PieEntry;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.database.HelperFactory;
import ru.mgusev.eldritchhorror.model.Game;
import ru.mgusev.eldritchhorror.model.Investigator;

public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int DEN = 100;
    private static final int MAX_VALUES_IN_CHART = 12;

    private Toolbar toolbarStatistics;
    private List<Game> gameList;
    private int winCount;
    private int defeatCount;
    private int currentAncientOneID = 0; // 0 - все Древние
    private int sum;
    private Spinner ancientOneSpinner;
    private ImageView statBackground;
    private CardView baseStatCard;
    private CardView filterCard;
    private CardView ancientOneCard;
    private CardView scoreCard;
    private CardView defeatReasonCard;
    private CardView investigatorsCard;
    private ArrayAdapter<String> ancientOneAdapter;
    private List <String> ancientOneList;
    private List<Float> chartValues;
    private List<String> chartLabels;
    private List<String[]> scoreResults;
    private List<String[]> investigatorsResults;
    private List<Float> defeatReasonResults;

    private TextView winDefeatHeader;
    private TextView ancientOneHeader;
    private TextView scoreHeader;
    private TextView defeatReasonHeader;
    private TextView investigatorsHeader;

    private TextView lastGameTV;
    private TextView bestScoreTV;
    private ImageView popularInvPhoto1;
    private ImageView popularInvPhoto2;
    private ImageView popularInvPhoto3;
    private TextView popularInvName1;
    private TextView popularInvName2;
    private TextView popularInvName3;
    private TextView popularInvPercent1;
    private TextView popularInvPercent2;
    private TextView popularInvPercent3;


    private EHChart winDefeatChart;
    private EHChart ancientOneChart;
    private EHChart scoreChart;
    private EHChart defeatReasonChart;
    private EHChart investigatorsChart;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        initToolbar();

        gameList = getIntent().getParcelableArrayListExtra("gameList");

        statBackground = findViewById(R.id.stat_background);
        baseStatCard = (CardView) findViewById(R.id.base_stat_card);
        filterCard = (CardView) findViewById(R.id.filter_stat_card);
        filterCard.setOnClickListener(this);
        ancientOneCard = (CardView) findViewById(R.id.ancient_one_stat_card);
        scoreCard = (CardView) findViewById(R.id.score_stat_card);
        defeatReasonCard = (CardView) findViewById(R.id.defeat_reason_stat_card);
        investigatorsCard = (CardView) findViewById(R.id.investigators_stat_card);

        winDefeatHeader = findViewById(R.id.win_defeat_stat_header);
        ancientOneHeader = findViewById(R.id.ancient_one_stat_header);
        scoreHeader = findViewById(R.id.score_stat_header);
        defeatReasonHeader = findViewById(R.id.defeat_reason_stat_header);
        investigatorsHeader = findViewById(R.id.investigators_stat_header);

        lastGameTV = findViewById(R.id.last_game_value);
        bestScoreTV = findViewById(R.id.best_score_value);
        popularInvPhoto1 = findViewById(R.id.popular_inv_1_photo);
        popularInvPhoto2 = findViewById(R.id.popular_inv_2_photo);
        popularInvPhoto3 = findViewById(R.id.popular_inv_3_photo);
        popularInvName1 = findViewById(R.id.popular_inv_1_name);
        popularInvName2 = findViewById(R.id.popular_inv_2_name);
        popularInvName3 = findViewById(R.id.popular_inv_3_name);
        popularInvPercent1 = findViewById(R.id.popular_inv_1_percent);
        popularInvPercent2 = findViewById(R.id.popular_inv_2_percent);
        popularInvPercent3 = findViewById(R.id.popular_inv_3_percent);

        try {
            scoreResults = HelperFactory.getHelper().getGameDAO().getScoreCount(0).getResults();
            defeatReasonResults = HelperFactory.getHelper().getGameDAO().getDefeatReasonCount(0);
            investigatorsResults = HelperFactory.getHelper().getInvestigatorDAO().getInvestigatorsCount(0).getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        initAncientOneArray();
        initAncientOneSpinner();

        initCharts();
    }

    private void initBaseStatistic() {
        Game bestGame = null;
        Game lastGame = null;
        String bestScoreValue = "0";
        String lastScoreValue = "0";
        try {
            bestGame = HelperFactory.getHelper().getGameDAO().getTopGameToSort(true, currentAncientOneID);
            lastGame = HelperFactory.getHelper().getGameDAO().getLastGame(currentAncientOneID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (bestGame != null) {
            bestScoreValue = bestGame.score + " (" + new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(bestGame.date) + ")";
        }
        if (lastGame != null) {
            lastScoreValue = lastGame.score + " (" + new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(lastGame.date) + ")";
        }
        bestScoreTV.setText(bestScoreValue);
        lastGameTV.setText(lastScoreValue);

        Investigator popularInv;
        int resourceId;
        Resources resources = this.getResources();
        try {
            if (chartLabels.size() > 0) {
                popularInv = HelperFactory.getStaticHelper().getInvestigatorDAO().getInvestigatorByName(chartLabels.get(0));

                resourceId = resources.getIdentifier(popularInv.imageResource, "drawable", this.getPackageName());
                popularInvPhoto1.setImageResource(resourceId);
                popularInvName1.setText(popularInv.getName());
                popularInvPercent1.setText(String.valueOf(new DecimalFormat("#0.0").format(getPercent(chartValues.get(0), sum, DEN))) + "%");
            }
            if (chartLabels.size() > 1) {
                popularInv = HelperFactory.getStaticHelper().getInvestigatorDAO().getInvestigatorByName(chartLabels.get(1));

                resourceId = resources.getIdentifier(popularInv.imageResource, "drawable", this.getPackageName());
                popularInvPhoto2.setImageResource(resourceId);
                popularInvName2.setText(popularInv.getName());
                popularInvPercent2.setText(String.valueOf(new DecimalFormat("#0.0").format(getPercent(chartValues.get(1), sum, DEN))) + "%");
            }
            if (chartLabels.size() > 2) {
                popularInv = HelperFactory.getStaticHelper().getInvestigatorDAO().getInvestigatorByName(chartLabels.get(2));

                resourceId = resources.getIdentifier(popularInv.imageResource, "drawable", this.getPackageName());
                popularInvPhoto3.setImageResource(resourceId);
                popularInvName3.setText(popularInv.getName());
                popularInvPercent3.setText(String.valueOf(new DecimalFormat("#0.0").format(getPercent(chartValues.get(2), sum, DEN))) + "%");
            }
            if (currentAncientOneID != 0) {
                resourceId = resources.getIdentifier(HelperFactory.getStaticHelper().getAncientOneDAO().getAncienOneByID(currentAncientOneID).imageResource, "drawable", this.getPackageName());
                //baseStatCard.setBackground(getDrawable(resourceId));
                statBackground.setImageResource(resourceId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initCharts() {
        initWinDefeatChart();
        initAncientOneChart();
        initScoreChart();
        initDefeatReasonChart();
        initInvestigatorsChart();

        initBaseStatistic();
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

        for (int i = 0; i < chartValues.size(); i++) {
            entries.add(new PieEntry(getPercent(chartValues.get(i), gameList.size(), DEN), chartLabels.get(i)));
        }

        winDefeatChart.setData(entries, getResources().getString(R.string.win_defeat_stat_description), chartLabels, chartValues, gameList.size(), winDefeatHeader);
    }

    private void initWinDefeatValues() {
        chartValues = new ArrayList<>();
        chartLabels = new ArrayList<>();
        winCount = 0;
        defeatCount = 0;

        for (Game game : gameList) {
            if (game.isWinGame) winCount++;
            else defeatCount++;
        }

        if (winCount != 0) {
            chartValues.add((float) winCount);
            chartLabels.add(getResources().getString(R.string.win_stat_label));
        }
        if (defeatCount != 0) {
            chartValues.add((float) defeatCount);
            chartLabels.add(getResources().getString(R.string.defeat_stat_label));
        }
    }

    private float getPercent(float value, int size, int den) {
        return size == 0 ? 0 : value / size * den;
    }

    private void initAncientOneChart() {
        ancientOneChart = findViewById(R.id.ancient_one_pie_chart);
        ancientOneChart.setOnChartValueSelectedListener(ancientOneChart);

        chartValues = new ArrayList<>();
        chartLabels = new ArrayList<>();
        try {
            for (String[] array : HelperFactory.getHelper().getGameDAO().getAncientOneCount().getResults()) {
                chartLabels.add(HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneNameByID(Integer.parseInt(array[0])));
                chartValues.add(Float.valueOf(array[1]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < chartValues.size(); i++) {
            entries.add(new PieEntry(getPercent(chartValues.get(i), gameList.size(), DEN), chartLabels.get(i)));
        }

        ancientOneChart.setData(entries, getResources().getString(R.string.ancientOne), chartLabels, chartValues, gameList.size(), ancientOneHeader);
    }

    private void initScoreChart() {
        scoreChart = findViewById(R.id.score_pie_chart);
        scoreChart.setOnChartValueSelectedListener(scoreChart);

        chartValues = new ArrayList<>();
        chartLabels = new ArrayList<>();
        Float otherSum = 0f;
        for (int i = 0; i < scoreResults.size(); i++) {
            if (i < MAX_VALUES_IN_CHART) {
                chartLabels.add(scoreResults.get(i)[0]);
                chartValues.add(Float.valueOf(scoreResults.get(i)[1]));
            } else {
                otherSum += Float.valueOf(scoreResults.get(i)[1]);
            }
        }

        if (scoreResults.size() > MAX_VALUES_IN_CHART) {
            chartLabels.add(getResources().getString(R.string.other_results));
            chartValues.add(otherSum);
        }

        List<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < chartValues.size(); i++) {
            entries.add(new PieEntry(getPercent(chartValues.get(i), winCount, DEN), chartLabels.get(i)));
        }

        scoreChart.setData(entries, getResources().getString(R.string.totalScore), chartLabels, chartValues, winCount, scoreHeader);
    }

    private void initDefeatReasonChart() {
        defeatReasonChart = findViewById(R.id.defeat_reason_pie_chart);
        defeatReasonChart.setOnChartValueSelectedListener(defeatReasonChart);

        chartValues = new ArrayList<>();
        chartLabels = new ArrayList<>();
        chartValues = defeatReasonResults;

        chartLabels.add(getResources().getString(R.string.defeat_by_awakened_ancient_one));
        chartLabels.add(getResources().getString(R.string.defeat_by_elimination));
        chartLabels.add(getResources().getString(R.string.defeat_by_mythos_depletion));

        int defeatSum = Math.round(chartValues.get(0) + chartValues.get(1) + chartValues.get(2));

        List<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < chartValues.size(); ) {
            if (chartValues.get(i) == 0) {
                chartValues.remove(i);
                chartLabels.remove(i);
            } else {
                entries.add(new PieEntry(getPercent(chartValues.get(i), defeatSum, DEN), chartLabels.get(i)));
                i++;
            }
        }

        defeatReasonChart.setData(entries, getResources().getString(R.string.defeat_table_header), chartLabels, chartValues, defeatSum, defeatReasonHeader);
        if (chartValues.isEmpty()) setCardVisibility(defeatReasonCard, false);
        else setCardVisibility(defeatReasonCard, true);
    }

    private void initInvestigatorsChart() {
        investigatorsChart = findViewById(R.id.investigators_pie_chart);
        investigatorsChart.setOnChartValueSelectedListener(investigatorsChart);

        chartValues = new ArrayList<>();
        chartLabels = new ArrayList<>();
        sum = 0;
        Float otherSum = 0f;
        for (int i = 0; i < investigatorsResults.size(); i++) {
            if (i < MAX_VALUES_IN_CHART) {
                chartLabels.add(investigatorsResults.get(i)[0]);
                chartValues.add(Float.valueOf(investigatorsResults.get(i)[1]));
            } else {
                otherSum += Float.valueOf(investigatorsResults.get(i)[1]);
            }
            sum += Integer.parseInt(investigatorsResults.get(i)[1]);
        }

        if (investigatorsResults.size() > MAX_VALUES_IN_CHART) {
            chartLabels.add(getResources().getString(R.string.other_results));
            chartValues.add(otherSum);
        }

        List<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < chartValues.size(); i++) {
            entries.add(new PieEntry(getPercent(chartValues.get(i), sum, DEN), chartLabels.get(i)));
        }

        investigatorsChart.setData(entries, getResources().getString(R.string.investigators), chartLabels, chartValues, sum, investigatorsHeader);
    }

    private void initAncientOneArray() {
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

    public void initAncientOneSpinner() {
        ancientOneAdapter = new ArrayAdapter<>(this, R.layout.spinner, ancientOneList);
        ancientOneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ancientOneSpinner = findViewById(R.id.ancientOneStatSpinner);
        ancientOneSpinner.setAdapter(ancientOneAdapter);

        ancientOneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if (i == 0) {
                        currentAncientOneID = 0;
                        gameList = HelperFactory.getHelper().getGameDAO().getGamesSortAncientOne();
                        scoreResults = HelperFactory.getHelper().getGameDAO().getScoreCount(currentAncientOneID).getResults();
                        defeatReasonResults = HelperFactory.getHelper().getGameDAO().getDefeatReasonCount(currentAncientOneID);
                        investigatorsResults = HelperFactory.getHelper().getInvestigatorDAO().getInvestigatorsCount(currentAncientOneID).getResults();
                        setCardVisibility(ancientOneCard, true);
                    } else {
                        currentAncientOneID = HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneIDByName(ancientOneList.get(i));
                        gameList = HelperFactory.getHelper().getGameDAO().getGamesByAncientOne(ancientOneList.get(i));
                        scoreResults = HelperFactory.getHelper().getGameDAO().getScoreCount(currentAncientOneID).getResults();
                        defeatReasonResults = HelperFactory.getHelper().getGameDAO().getDefeatReasonCount(currentAncientOneID);
                        investigatorsResults = HelperFactory.getHelper().getInvestigatorDAO().getInvestigatorsCount(currentAncientOneID).getResults();
                        setCardVisibility(ancientOneCard, false);
                    }
                    if (scoreResults.isEmpty()) setCardVisibility(scoreCard, false);
                    else setCardVisibility(scoreCard, true);
                    if (investigatorsResults.isEmpty()) setCardVisibility(investigatorsCard, false);
                    else setCardVisibility(investigatorsCard, true);
                    initCharts();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setCardVisibility(CardView card, boolean value) {
        if (value) card.setVisibility(View.VISIBLE);
        else card.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.filter_stat_card:
                ancientOneSpinner.performClick();
                break;
            default:
                break;
        }
    }
}