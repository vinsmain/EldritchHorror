package ru.mgusev.eldritchhorror;

import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvestigatorsChoiceAvtivity extends AppCompatActivity implements GVAdapter.OnItemClicked, View.OnClickListener {

    final static int REQUEST_CODE_INVESTIGATOR = 1;

    List<Investigator> investigatorList;
    List<Investigator> invSavedList;
    GVAdapter adapter;
    Toolbar toolbar;
    FloatingActionButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investigators_change);

        initToolbar();
        saveButton = (FloatingActionButton) findViewById(R.id.saveInvButton);
        saveButton.setOnClickListener(this);

        invSavedList = getIntent().getParcelableArrayListExtra("invSavedList");

        RecyclerView invRecycleView = (RecyclerView) findViewById(R.id.invRecycleView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        invRecycleView.setLayoutManager(gridLayoutManager);
        invRecycleView.setHasFixedSize(true);

        initInvestigatorList();

        adapter = new GVAdapter(this, investigatorList);
        invRecycleView.setAdapter(adapter);
        adapter.setOnClick(this);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarInvChoice);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.activity_investigators_choice_header);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initInvestigatorList() {
        try {
            investigatorList = HelperFactory.getStaticHelper().getInvestigatorDAO().getAllInvestigatorsLocal();
            if (invSavedList != null) {
                for (int i = 0; i < invSavedList.size(); i++) {
                    for (int j = 0; j < investigatorList.size(); j++) {
                        if (investigatorList.get(j).id == invSavedList.get(i).id)
                            investigatorList.set(j, invSavedList.get(i));
                    }
                }
            }
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
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        ArrayList<Investigator> invUsedList = new ArrayList<>();
        for (int i = 0; i < investigatorList.size(); i++) {
            if (investigatorList.get(i).isStarting || investigatorList.get(i).isReplacement) invUsedList.add(investigatorList.get(i));
        }
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("invUsedList", invUsedList);
        setResult(RESULT_OK, intent);
        finish();
    }
}