package ru.mgusev.eldritchhorror.activity;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ru.mgusev.eldritchhorror.R;

public class EHChart extends PieChart implements OnChartValueSelectedListener {

    private List<Integer> colors;
    private Legend legend;
    private PieDataSet dataSet;
    private PieData data;
    private Description description;
    private List<String> labels;
    private List<Float> values;
    private int count;
    private int width;
    private TextView header;

    public EHChart(Context context) {
        super(context);
    }

    public EHChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EHChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void initColors() {
        colors = new ArrayList<Integer>();

        /*for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);*/

        //for (int c : ColorTemplate.LIBERTY_COLORS)
        //    colors.add(c);

        //for (int c : ColorTemplate.PASTEL_COLORS)
         //   colors.add(c);

        //colors.add(ColorTemplate.getHoloBlue());
        colors.add(Color.rgb(96,125,139));
        colors.add(Color.rgb(63,81,181));
        colors.add(Color.rgb(139,195,74));
        colors.add(Color.rgb(33,150,243));
        colors.add(Color.rgb(0,150,136));
        colors.add(Color.rgb(76,175,80));
        colors.add(Color.rgb(0,188,212));
        colors.add(Color.rgb(121,134,203));
        colors.add(Color.rgb(174,213,129));
        colors.add(Color.rgb(100,181,246));
        colors.add(Color.rgb(129,199,132));
        colors.add(Color.rgb(77,208,225));
        colors.add(Color.rgb(77,182,172));
        colors.add(Color.rgb(144,164,174));
        /*colors.add(Color.rgb(100, 40, 9));
        colors.add(Color.rgb(100, 40, 9));
        colors.add(Color.rgb(100, 40, 9));*/
    }

    private void initLegend() {
        legend = getLegend();
        legend.setFormSize(18f); // set the size of the legend forms/shapes
        legend.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setTextSize(12f);
        legend.setTextColor(Color.BLACK);
        legend.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
        legend.setYEntrySpace(5f);
        legend.setWordWrapEnabled(true);
        System.out.println("size " + legend.mNeededHeight);
        //legend.setEnabled(false);
    }

    private void initDescription(String desc) {
        description = new Description();
        description.setText("");
        header.setText(desc);
        //description.setPosition(width / 2 - 20,50f);
        description.setTextSize(18);
        //description.setTextAlign(Paint.Align.CENTER);
    }

    private void initPieData() {
        data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(14);
        data.setValueTextColor(Color.BLACK);
    }

    private void initPieChart() {
        setData(data);
        setNoDataText(getResources().getString(R.string.statistics_none));
        setNoDataTextColor(Color.BLACK);
        setDescription(description);
        setEntryLabelColor(Color.BLACK);
    }

    public void setData(List<PieEntry> entries, String description, List<String> labels, List<Float> values, int sum, TextView header) {
        this.labels = labels;
        this.values = values;
        this.count = sum;
        this.header = header;
        initColors();
        initLegend();
        initDescription(description);
        dataSet = new PieDataSet(entries, "");
        // add a lot of colors
        dataSet.setColors(colors);
        initPieData();
        initPieChart();
        setCenterTextSize(18);
        setDrawEntryLabels(false);
        if (!entries.isEmpty()) highlightValue(0f, 0, true);
        this.setMinimumHeight((int) (1000 + legend.mNeededHeight));
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        setCenterText(labels.get((int) h.getX()) + "\n" + new DecimalFormat("#0.0").format(e.getY()) + "%\n" + Math.round(values.get((int) h.getX())) + "/" + count);
    }

    @Override
    public void onNothingSelected() {
        this.setCenterText("");
    }
}