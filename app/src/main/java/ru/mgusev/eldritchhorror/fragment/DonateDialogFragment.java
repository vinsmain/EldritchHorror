package ru.mgusev.eldritchhorror.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.activity.MainActivity;

public class DonateDialogFragment extends DialogFragment {

    MainActivity mainActivity;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.view_question)
                .setIcon(R.drawable.video)
                .setMessage(R.string.donate_message)
                .setPositiveButton(R.string.messageOK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mainActivity.getAdc() != null) mainActivity.getAdc().show();
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.messageCancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
