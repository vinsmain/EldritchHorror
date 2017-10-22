package ru.mgusev.eldritchhorror.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ru.mgusev.eldritchhorror.database.HelperFactory;
import ru.mgusev.eldritchhorror.activity.Investigator;
import ru.mgusev.eldritchhorror.activity.InvestigatorActivity;
import ru.mgusev.eldritchhorror.eh_interface.PassMeLinkOnObject;
import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.adapter.GVAdapter;
import ru.mgusev.eldritchhorror.eh_interface.OnItemClicked;

import static android.app.Activity.RESULT_OK;

public class InvestigatorsChoiceFragment extends Fragment implements OnItemClicked {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    final static int REQUEST_CODE_INVESTIGATOR = 1;

    int pageNumber;

    View view;
    PassMeLinkOnObject activity;

    List<Investigator> investigatorList;
    List<Investigator> invSavedList;
    GVAdapter adapter;

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

        invSavedList = activity.getGame().invList;

        RecyclerView invRecycleView = (RecyclerView) view.findViewById(R.id.invRecycleView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 3);
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
        Intent intentInvestigator = new Intent(getContext(), InvestigatorActivity.class);
        intentInvestigator.putExtra("investigator", investigatorList.get(position));
        startActivityForResult(intentInvestigator, REQUEST_CODE_INVESTIGATOR);
    }

    @Override
    public void onEditClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
}