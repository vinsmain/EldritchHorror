package ru.mgusev.eldritchhorror;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Party> partyList;
    FloatingActionButton addPartyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addPartyButton = (FloatingActionButton) findViewById(R.id.addPartyButton);
        addPartyButton.setOnClickListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.partyList);
        //TextView partyCount = (TextView) findViewById(R.id.partyCount);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        initPartyList();

        RVAdapter adapter = new RVAdapter(partyList);
        recyclerView.setAdapter(adapter);

        //partyCount.setText("Всего выигранных партий: " + adapter.getItemCount());
    }

    private void initPartyList() {
        partyList = new ArrayList<>();
        partyList.add(new Party("01.09.2017", "Азатот", 4, 12));
        partyList.add(new Party("02.09.2017", "Йиг", 4, 2));
        partyList.add(new Party("03.09.2017", "Ктулху", 2, -6));
        partyList.add(new Party("04.09.2017", "Азатот", 4, 12));
        partyList.add(new Party("05.09.2017", "Йиг", 4, 2));
        partyList.add(new Party("06.09.2017", "Ктулху", 2, -6));
        partyList.add(new Party("07.09.2017", "Азатот", 4, 12));
        partyList.add(new Party("08.09.2017", "Йиг", 4, 2));
        partyList.add(new Party("09.09.2017", "Ктулху", 2, -6));
        partyList.add(new Party("10.09.2017", "Азатот", 4, 12));
        partyList.add(new Party("11.09.2017", "Йиг", 4, 2));
        partyList.add(new Party("12.09.2017", "Ктулху", 2, -6));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addPartyButton:
                Intent intent = new Intent(this, AddPartyActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
