package ru.mgusev.eldritchhorror.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.adapter.InvRVAdapter;
import ru.mgusev.eldritchhorror.database.FirebaseHelper;
import ru.mgusev.eldritchhorror.database.HelperFactory;
import ru.mgusev.eldritchhorror.model.Game;

public class GameDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    Game game;

    Toolbar toolbar;
    TextView dateField;
    TextView ancientOne;
    TextView prelude;
    TextView playersCount;
    TableRow isSimpleMyths;
    TableRow isNormalMyths;
    TableRow isHardMyths;
    TableRow isStartingRumor;
    TextView mysteriesCount;
    TextView mysteriesCountDefeat;
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
    CardView winCardView;
    CardView defeatCardView;
    TableRow defeatByEliminationDetail;
    TableRow defeatByMythosDeplitionDetail;
    TableRow defeatByAwakenedAncientOneDetail;

    ImageButton editStartingDataButton;
    ImageButton editInvestigatorsButton;
    ImageButton editWinButton;
    ImageButton editDefeatButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        game = (Game) getIntent().getParcelableExtra("game");

        initToolbar();

        dateField = (TextView) findViewById(R.id.dataDetail);
        ancientOne = (TextView) findViewById(R.id.ancientOneDetail);
        prelude = (TextView) findViewById(R.id.preludeDetail);
        playersCount = (TextView) findViewById(R.id.playersCountDetail);
        isSimpleMyths = (TableRow) findViewById(R.id.simpleMythsDetail);
        isNormalMyths = (TableRow) findViewById(R.id.normalMythsDetail);
        isHardMyths = (TableRow) findViewById(R.id.hardMythsDetail);
        isStartingRumor = (TableRow) findViewById(R.id.startingRumorDetail);
        mysteriesCount = (TextView) findViewById(R.id.mysteriesCountDetail);
        mysteriesCountDefeat = (TextView) findViewById(R.id.mysteriesCountDefeatDetail);
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
        winCardView = (CardView) findViewById(R.id.winCardView);
        defeatCardView = (CardView) findViewById(R.id.defeatCardView);
        defeatByEliminationDetail = (TableRow) findViewById(R.id.defeatByEliminationDetail);
        defeatByMythosDeplitionDetail = (TableRow) findViewById(R.id.defeatByMythosDeplitionDetail);
        defeatByAwakenedAncientOneDetail = (TableRow) findViewById(R.id.defeatByAwakenedAncientOneDetail);

        initPartyDetails();
        initInvRecycleView();
        initEditButtons();
        initListeners();
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

    private void initEditButtons() {
        editStartingDataButton = (ImageButton) findViewById(R.id.editStartingDataButton);
        editInvestigatorsButton = (ImageButton) findViewById(R.id.editInvestigatorsButton);
        editWinButton = (ImageButton) findViewById(R.id.editWinButton);
        editDefeatButton = (ImageButton) findViewById(R.id.editDefeatButton);
    }

    private void initListeners() {
        editStartingDataButton.setOnClickListener(this);
        editInvestigatorsButton.setOnClickListener(this);
        editWinButton.setOnClickListener(this);
        editDefeatButton.setOnClickListener(this);
    }

    private void initPartyDetails() {
        dateField.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(game.date));
        try {
            ancientOne.setText(HelperFactory.getStaticHelper().getAncientOneDAO().getAncientOneNameByID(game.ancientOneID));
            prelude.setText(HelperFactory.getStaticHelper().getPreludeDAO().getPreludeNameByID(game.preludeID));
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
        mysteriesCount.setText(String.valueOf(game.solvedMysteriesCount));
        mysteriesCountDefeat.setText(String.valueOf(game.solvedMysteriesCount));
        gatesCount.setText(String.valueOf(game.gatesCount));
        monstersCount.setText(String.valueOf(game.monstersCount));
        curseCount.setText(String.valueOf(game.curseCount));
        rumorsCount.setText(String.valueOf(game.rumorsCount));
        cluesCount.setText(String.valueOf(game.cluesCount));
        blessedCount.setText(String.valueOf(game.blessedCount));
        doomCount.setText(String.valueOf(game.doomCount));

        if (!game.isDefeatByElimination) defeatByEliminationDetail.setVisibility(View.GONE);
        if (!game.isDefeatByMythosDepletion) defeatByMythosDeplitionDetail.setVisibility(View.GONE);
        if (!game.isDefeatByAwakenedAncientOne) defeatByAwakenedAncientOneDetail.setVisibility(View.GONE);

        if (game.isWinGame) {
            score.setText(String.valueOf(game.score));

            winImage.setImageResource(R.drawable.stars);
            winCardView.setVisibility(View.VISIBLE);
            defeatCardView.setVisibility(View.GONE);
        } else {
            if (game.isDefeatByAwakenedAncientOne) winImage.setImageResource(R.drawable.skull);
            else if (game.isDefeatByElimination) winImage.setImageResource(R.drawable.inestigators_out);
            else if (game.isDefeatByMythosDepletion) winImage.setImageResource(R.drawable.mythos_empty);
            score.setVisibility(View.GONE);
            defeatCardView.setVisibility(View.VISIBLE);
            winCardView.setVisibility(View.GONE);
        }
        try {
            if (HelperFactory.getStaticHelper().getExpansionDAO().getImageResourceByAncientOneID(game.ancientOneID) != null) {
                int resourceId = getResources().getIdentifier(HelperFactory.getStaticHelper().getExpansionDAO().getImageResourceByAncientOneID(game.ancientOneID), "drawable", this.getPackageName());
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
                startIntent(0);
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

        DialogKeeper.doKeepDialog(alert);
    }

    private void deleteParty() {
        try {
            HelperFactory.getHelper().getGameDAO().delete(game);
            HelperFactory.getHelper().getInvestigatorDAO().deleteInvestigatorsByGameID(game.id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FirebaseHelper.removeGame(game);

        Intent intentDelete = new Intent(this, MainActivity.class);
        intentDelete.putExtra("refreshPartyList", true);
        intentDelete.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentDelete);
        Toast.makeText(this, R.string.success_deleting_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editStartingDataButton:
                startIntent(0);
                break;
            case R.id.editInvestigatorsButton:
                startIntent(1);
                break;
            case R.id.editWinButton:
                startIntent(2);
                break;
            case R.id.editDefeatButton:
                startIntent(2);
                break;
            default:
                break;
        }
    }

    private void startIntent(int position) {
        Intent intentEdit = new Intent(this, GamesPagerActivity.class);
        intentEdit.putExtra("editParty", game);
        intentEdit.putExtra("setPosition", position);
        startActivity(intentEdit);
    }
}