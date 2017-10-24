package ru.mgusev.eldritchhorror.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.adapter.InvRVAdapter;
import ru.mgusev.eldritchhorror.database.HelperFactory;
import ru.mgusev.eldritchhorror.model.Game;

public class GameDetailsActivity extends AppCompatActivity {

    Game game;

    Toolbar toolbar;
    TextView dateField;
    TextView ancientOne;
    TextView playersCount;
    TableRow isSimpleMyths;
    TableRow isNormalMyths;
    TableRow isHardMyths;
    TableRow isStartingRumor;
    TextView gatesCount;
    TextView monstersCount;
    TextView curseCount;
    TextView rumorsCount;
    TextView cluesCount;
    TextView blessedCount;
    TextView doomCount;
    TextView score;
    ImageView winImage;
    ImageView backgroundTop;
    ImageView expansionImage;
    RecyclerView invRecyclerViewDetail;
    TextView invNoneTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        game = (Game) getIntent().getParcelableExtra("game");

        initToolbar();

        dateField = (TextView) findViewById(R.id.dataDetail);
        ancientOne = (TextView) findViewById(R.id.ancientOneDetail);
        playersCount = (TextView) findViewById(R.id.playersCountDetail);
        isSimpleMyths = (TableRow) findViewById(R.id.simpleMythsDetail);
        isNormalMyths = (TableRow) findViewById(R.id.normalMythsDetail);
        isHardMyths = (TableRow) findViewById(R.id.hardMythsDetail);
        isStartingRumor = (TableRow) findViewById(R.id.startingRumorDetail);
        gatesCount = (TextView) findViewById(R.id.gatesCountDetail);
        monstersCount = (TextView) findViewById(R.id.monstersCountDetail);
        curseCount = (TextView) findViewById(R.id.curseCountDetail);
        rumorsCount = (TextView) findViewById(R.id.rumorsCountDetail);
        cluesCount = (TextView) findViewById(R.id.cluesCountDetail);
        blessedCount = (TextView) findViewById(R.id.blessedCountDetail);
        doomCount = (TextView) findViewById(R.id.doomCountDetail);
        score = (TextView) findViewById(R.id.totalScoreDetail);
        winImage = (ImageView) findViewById(R.id.win_image_details);
        backgroundTop = (ImageView) findViewById(R.id.backgroundDetail);
        expansionImage = (ImageView) findViewById(R.id.expansion_image_details);

        initPartyDetails();
        initInvRecycleView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_activity, menu);
        return true;
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarDetails);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.activity_party_details_header);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initInvRecycleView() {
        invNoneTV = (TextView) findViewById(R.id.invNoneTV);
        invRecyclerViewDetail = (RecyclerView) findViewById(R.id.invRecycleViewDetail);

        if (game.invList.size() != 0) {
            invNoneTV.setVisibility(View.GONE);

            LinearLayoutManager leanerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            invRecyclerViewDetail.setLayoutManager(leanerLayoutManager);
            invRecyclerViewDetail.setHasFixedSize(true);

            InvRVAdapter adapter = new InvRVAdapter(this.getApplicationContext(), game.invList);
            invRecyclerViewDetail.setAdapter(adapter);
        }
    }

    private void initPartyDetails() {
        dateField.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(game.date));
        try {
            ancientOne.setText(HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneNameByID(game.ancientOneID));
            final int resourceId;
            Resources resources = this.getResources();
            resourceId = resources.getIdentifier(HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneImageResourceByID(game.ancientOneID), "drawable", this.getPackageName());
            backgroundTop.setImageResource(resourceId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        playersCount.setText(String.valueOf(game.playersCount));
        if (!game.isSimpleMyths) isSimpleMyths.setVisibility(View.GONE);
        if (!game.isNormalMyths) isNormalMyths.setVisibility(View.GONE);
        if (!game.isHardMyths) isHardMyths.setVisibility(View.GONE);
        if (!game.isStartingRumor) isStartingRumor.setVisibility(View.GONE);
        gatesCount.setText(String.valueOf(game.gatesCount));
        monstersCount.setText(String.valueOf(game.monstersCount));
        curseCount.setText(String.valueOf(game.curseCount));
        rumorsCount.setText(String.valueOf(game.rumorsCount));
        cluesCount.setText(String.valueOf(game.cluesCount));
        blessedCount.setText(String.valueOf(game.blessedCount));
        doomCount.setText(String.valueOf(game.doomCount));
        if (game.isWinGame) {
            score.setText(String.valueOf(game.score));
            score.setVisibility(View.VISIBLE);
            winImage.setImageResource(R.drawable.stars);
        } else {
            score.setVisibility(View.GONE);
            winImage.setImageResource(R.drawable.skull);
        }
        try {
            if (HelperFactory.getStaticHelper().getExpansionDAO().getImageResourceByAncientOne(game.ancientOneID) != null) {
                int resourceId = getResources().getIdentifier(HelperFactory.getStaticHelper().getExpansionDAO().getImageResourceByAncientOne(game.ancientOneID), "drawable", this.getPackageName());
                expansionImage.setImageResource(resourceId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteDialog();
                return true;
            case R.id.action_edit:
                Intent intentEdit = new Intent(this, GamesPagerActivity.class);
                intentEdit.putExtra("editParty", game);
                startActivity(intentEdit);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.dialogAlert))
                .setMessage(getResources().getString(R.string.deleteDialogMessage))
                .setIcon(R.drawable.delete)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.messageOK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteParty();
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

    private void deleteParty() {
        try {
            HelperFactory.getHelper().getGameDAO().delete(game);
            HelperFactory.getHelper().getInvestigatorDAO().deleteInvestigatorsByGameID(game.id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Intent intentDelete = new Intent(this, MainActivity.class);
        intentDelete.putExtra("refreshPartyList", true);
        intentDelete.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentDelete);
        Toast.makeText(this, R.string.success_deleting_message, Toast.LENGTH_SHORT).show();
    }
}