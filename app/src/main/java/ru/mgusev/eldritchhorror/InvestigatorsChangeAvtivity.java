package ru.mgusev.eldritchhorror;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvestigatorsChangeAvtivity extends AppCompatActivity implements GVAdapter.OnItemClicked {

    List<InvestigatorLocal> investigatorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investigators_change);

        RecyclerView invRecycleView = (RecyclerView) findViewById(R.id.invRecycleView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        invRecycleView.setLayoutManager(gridLayoutManager);
        invRecycleView.setHasFixedSize(true);

        initInvestigatorList();

        GVAdapter adapter = new GVAdapter(this, investigatorList);
        invRecycleView.setAdapter(adapter);
        adapter.setOnClick(this);
    }

    private void initInvestigatorList() {
        try {
            investigatorList = HelperFactory.getStaticHelper().getInvestigatorLocalDAO().getAllInvestigatorsLocal();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intentInvestigator = new Intent(this, InvestigatorActivity.class);
        intentInvestigator.putExtra("investigator", investigatorList.get(position));
        startActivity(intentInvestigator);
    }
}