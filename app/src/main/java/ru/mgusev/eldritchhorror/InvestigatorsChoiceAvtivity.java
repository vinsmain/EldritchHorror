package ru.mgusev.eldritchhorror;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.sql.SQLException;
import java.util.List;

public class InvestigatorsChoiceAvtivity extends AppCompatActivity implements GVAdapter.OnItemClicked {

    final static int REQUEST_CODE_INVESTIGATOR = 1;

    List<Investigator> investigatorList;
    GVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investigators_change);

        RecyclerView invRecycleView = (RecyclerView) findViewById(R.id.invRecycleView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        invRecycleView.setLayoutManager(gridLayoutManager);
        invRecycleView.setHasFixedSize(true);

        initInvestigatorList();

        adapter = new GVAdapter(this, investigatorList);
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
        startActivityForResult(intentInvestigator, REQUEST_CODE_INVESTIGATOR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Investigator investigatorUpdate = data.getParcelableExtra("investigator");
            for (int i = 0; i < investigatorList.size(); i++) {
                if (investigatorList.get(i).id == investigatorUpdate.id) {
                    investigatorList.set(i, investigatorUpdate);
                    //investigatorList.get(i).isReplacement = investigatorUpdate.isReplacement;
                    //investigatorList.get(i).isDead = investigatorUpdate.isDead;
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }
}