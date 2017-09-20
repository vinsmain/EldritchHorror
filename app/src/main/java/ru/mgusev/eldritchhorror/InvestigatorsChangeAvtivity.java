package ru.mgusev.eldritchhorror;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class InvestigatorsChangeAvtivity extends AppCompatActivity {

    List<Investigator> investigatorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investigators_change);

        GridView gridView = (GridView) findViewById(R.id.investigatorsGridView);

        initInvestigatorList();

        GVAdapter adapter = new GVAdapter(this, investigatorList);
        gridView.setAdapter(adapter);
    }

    private void initInvestigatorList() {
        investigatorList = new ArrayList<>();
        investigatorList.add(new Investigator());
        investigatorList.add(new Investigator());
        investigatorList.add(new Investigator());
        investigatorList.add(new Investigator());
        investigatorList.add(new Investigator());
        investigatorList.add(new Investigator());
        investigatorList.add(new Investigator());
        investigatorList.add(new Investigator());
        investigatorList.add(new Investigator());
        investigatorList.add(new Investigator());
        investigatorList.add(new Investigator());
        investigatorList.add(new Investigator());
        investigatorList.add(new Investigator());
        investigatorList.add(new Investigator());
    }
}
