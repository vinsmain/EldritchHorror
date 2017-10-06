package ru.mgusev.eldritchhorror;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    CardView openCardView;
    CardView startingDataContent;
    CardView startingDataHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initCollapseBlocks();
        initListeners();

    }

    private void initCollapseBlocks() {
        startingDataHeader = (CardView) findViewById(R.id.starting_data_header);
        startingDataContent = (CardView) findViewById(R.id.starting_data_content);
    }

    private void initListeners() {
        startingDataHeader.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.starting_data_header:
                if (openCardView == null) {
                    openCardView = startingDataContent;
                    startingDataContent.setVisibility(View.VISIBLE);
                } else startingDataContent.setVisibility(View.GONE);

                break;
            default:
                break;
        }
    }
}
