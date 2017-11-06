package ru.mgusev.eldritchhorror.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.adapter.ExpansionListAdapter;
import ru.mgusev.eldritchhorror.database.HelperFactory;
import ru.mgusev.eldritchhorror.model.Expansion;

public class ExpansionChoiceActivity extends Activity {

    private int columnsCount = 1;

    private List<Expansion> expansionList;
    RecyclerView recyclerView;
    ExpansionListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expansion_choice);

        recyclerView = (RecyclerView) findViewById(R.id.expansion_rv_list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, columnsCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        initExpansionList();
        adapter = new ExpansionListAdapter(this.getApplicationContext(), expansionList);
        recyclerView.setAdapter(adapter);
    }

    private void initExpansionList() {
        expansionList = new ArrayList<>();
        try {
            expansionList = HelperFactory.getStaticHelper().getExpansionDAO().getAllExpansion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        expansionList = adapter.getListStorage();
        for (int i = 0; i < expansionList.size(); i++) {
            try {
                HelperFactory.getStaticHelper().getExpansionDAO().writeExpansionToDB(expansionList.get(i));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
