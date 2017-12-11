package ru.mgusev.eldritchhorror.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ru.mgusev.eldritchhorror.activity.DialogKeeper;
import ru.mgusev.eldritchhorror.activity.GamesPagerActivity;
import ru.mgusev.eldritchhorror.database.HelperFactory;
import ru.mgusev.eldritchhorror.model.Investigator;
import ru.mgusev.eldritchhorror.activity.InvestigatorActivity;
import ru.mgusev.eldritchhorror.eh_interface.PassMeLinkOnObject;
import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.adapter.GVAdapter;
import ru.mgusev.eldritchhorror.eh_interface.OnItemClicked;

import static android.app.Activity.RESULT_OK;

public class InvestigatorsChoiceFragment extends Fragment implements OnItemClicked {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    public final static int REQUEST_CODE_INVESTIGATOR = 1;
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";

    int pageNumber;
    private int columnsCount = 3;

    View view;
    PassMeLinkOnObject activity;

    List<Investigator> investigatorList;
    List<Investigator> invSavedList;
    RecyclerView invRecycleView;
    public GVAdapter adapter;
    AlertDialog alert;

    public void setActivity(PassMeLinkOnObject activity) {
        this.activity = activity;
    }

    public static InvestigatorsChoiceFragment newInstance(int page, PassMeLinkOnObject activity) {
        InvestigatorsChoiceFragment fragment = new InvestigatorsChoiceFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        fragment.setActivity(activity);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_investigators_choice, null);

        invRecycleView = (RecyclerView) view.findViewById(R.id.invRecycleView);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) columnsCount = 5;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), columnsCount);
        invRecycleView.setLayoutManager(gridLayoutManager);
        invRecycleView.setHasFixedSize(true);

        initInvestigatorList();

        adapter = new GVAdapter(view.getContext(), investigatorList);
        invRecycleView.setAdapter(adapter);
        adapter.setOnClick(this);

        return view;
    }

    private void cleanInvList() {
        for (int i = 0; i < investigatorList.size(); i++) {
            investigatorList.get(i).isStarting = false;
            investigatorList.get(i).isReplacement = false;
            investigatorList.get(i).isDead = false;
        }
        adapter.notifyDataSetChanged();
    }

    public void cleanDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.dialogAlert))
                .setMessage(getResources().getString(R.string.cleanDialogMessage))
                .setIcon(R.drawable.clear)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.messageOK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                cleanInvList();
                                ((GamesPagerActivity) activity).refreshInvestigatorsList();
                                dialog.cancel();
                                ((GamesPagerActivity) activity).setAlert(false);
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.messageCancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                ((GamesPagerActivity) activity).setAlert(false);
                            }
                        });
        alert = builder.create();
        alert.show();
    }

    public void initInvestigatorList() {
        try {
            if (investigatorList == null) {
                investigatorList = new ArrayList<>();
            }

            investigatorList.clear();
            investigatorList.addAll(HelperFactory.getStaticHelper().getInvestigatorDAO().getAllInvestigatorsLocal());
            invSavedList = activity.getGame().invList;
            if (invSavedList != null) {
                for (int i = 0; i < invSavedList.size(); i++) {
                    for (int j = 0; j < investigatorList.size(); j++) {
                        if (investigatorList.get(j).getName().equals(invSavedList.get(i).getName())) {
                            investigatorList.set(j, invSavedList.get(i));
                            break;
                        }
                    }
                    if (!investigatorList.contains(invSavedList.get(i))) investigatorList.add(invSavedList.get(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intentInvestigator = new Intent(getContext(), InvestigatorActivity.class);
        intentInvestigator.putExtra("investigator", investigatorList.get(position));
        getActivity().startActivityForResult(intentInvestigator, REQUEST_CODE_INVESTIGATOR);
    }

    @Override
    public void onEditClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {

    }

    private int getStartingInvCount() {
        int count = 0;
        for (Investigator investigator : investigatorList) {
            if (investigator.isStarting) count++;
        }
        return count;
    }

    public boolean isStartingInvCountCorrect() {
        return getStartingInvCount() <= activity.getGame().playersCount;
    }

    public void showStartingInvCountAlert() {
        Toast.makeText(getContext(), "Превышено число стартовых сыщиков!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Investigator investigatorUpdate = data.getParcelableExtra("investigator");
            if (!investigatorUpdate.isStarting || getStartingInvCount() < activity.getGame().playersCount) {
                for (int i = 0; i < investigatorList.size(); i++) {
                    if (investigatorList.get(i).getName().equals(investigatorUpdate.getName())) {
                        investigatorList.set(i, investigatorUpdate);
                        addDataToGame();

                        initInvestigatorList();
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            } else {
                showStartingInvCountAlert();
            }
        }
    }

    public void addDataToGame() {
        if (view != null) {
            ArrayList<Investigator> invUsedList = new ArrayList<>();
            for (int i = 0; i < investigatorList.size(); i++) {
                if (investigatorList.get(i).isStarting || investigatorList.get(i).isReplacement)
                    invUsedList.add(investigatorList.get(i));
            }
            activity.getGame().invList = invUsedList;
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            invRecycleView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, invRecycleView.getLayoutManager().onSaveInstanceState());
    }

    public AlertDialog getAlert() {
        return alert;
    }

    public void selectRandomInvestigators() {
        cleanInvList();
        int j = 0;
        for (int i = 0; i < activity.getGame().playersCount; i++) {
            try {
                do {
                    j = (int) (Math.random() * investigatorList.size());
                } while (investigatorList.get(j).isStarting || !HelperFactory.getStaticHelper().getExpansionDAO().isEnableByID(investigatorList.get(j).expansionID));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            investigatorList.get(j).isStarting = true;
        }
        addDataToGame();

        initInvestigatorList();
        adapter.notifyDataSetChanged();
    }
}