package ru.mgusev.eldritchhorror;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvestigatorsChoiceAvtivity extends AppCompatActivity implements GVAdapter.OnItemClicked, View.OnClickListener {

    final static int REQUEST_CODE_INVESTIGATOR = 1;

    List<Investigator> investigatorList;
    List<Investigator> invSavedList;
    Game game;
    GVAdapter adapter;
    Toolbar toolbar;
    FloatingActionButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investigators_choice);

        initToolbar();
        saveButton = (FloatingActionButton) findViewById(R.id.saveInvButton);
        saveButton.setOnClickListener(this);

        game = getIntent().getParcelableExtra("game");

        invSavedList = game.invList;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_investigator_choice_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clean:
                cleanDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cleanInvList() {
        for (int i = 0; i < investigatorList.size(); i++) {
            investigatorList.get(i).isStarting = false;
            investigatorList.get(i).isReplacement = false;
            investigatorList.get(i).isDead = false;
        }
        adapter.notifyDataSetChanged();
    }

    private void cleanDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle(getResources().getString(R.string.deleteDialogAlert))
        builder.setMessage(getResources().getString(R.string.cleanDialogMessage))
                .setIcon(android.R.drawable.ic_notification_clear_all)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.messageOK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                cleanInvList();
                                dialog.cancel();
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.messageCancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void initInvestigatorList() {
        try {
            investigatorList = HelperFactory.getStaticHelper().getInvestigatorDAO().getAllInvestigatorsLocal();
            if (invSavedList != null) {
                for (int i = 0; i < invSavedList.size(); i++) {
                    for (int j = 0; j < investigatorList.size(); j++) {
                        if (investigatorList.get(j).name.equals(invSavedList.get(i).name)) {
                            investigatorList.set(j, invSavedList.get(i));
                            break;
                        }
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
                if (investigatorList.get(i).name.equals(investigatorUpdate.name)) {
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
        game.invList = invUsedList;
        Intent intent = new Intent(this, ResultGameActivity.class);
        intent.putExtra("game", game);
        startActivity(intent);
    }
}