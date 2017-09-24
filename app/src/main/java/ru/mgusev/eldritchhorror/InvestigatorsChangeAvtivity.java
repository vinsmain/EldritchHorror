package ru.mgusev.eldritchhorror;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvestigatorsChangeAvtivity extends AppCompatActivity {

    List<InvestigatorLocal> investigatorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investigators_change);

        GridView gridView = (GridView) findViewById(R.id.investigatorsGridView);
        /*gridView.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    return true;
                }
                return false;
            }

        });*/

        initInvestigatorList();

        GVAdapter adapter = new GVAdapter(this, investigatorList);
        gridView.setAdapter(adapter);
    }

    private void initInvestigatorList() {
        try {
            investigatorList = HelperFactory.getStaticHelper().getInvestigatorLocalDAO().getAllInvestigatorsLocal();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}