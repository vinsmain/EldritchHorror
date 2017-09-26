package ru.mgusev.eldritchhorror;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.sql.SQLException;
import java.util.List;

public class InvestigatorsChoiceAvtivity extends AppCompatActivity implements GVAdapter.OnItemClicked {

    List<Investigator> investigatorList;

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
            investigatorList = HelperFactory.getStaticHelper().getInvestigatorDAO().getAllInvestigatorsLocal();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intentInvestigator = new Intent(this, InvestigatorActivity.class);
        intentInvestigator.putExtra("investigator", investigatorList.get(position));
        startActivityForResult(intentInvestigator, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {

        }
    }
}