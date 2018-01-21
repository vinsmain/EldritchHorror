package ru.mgusev.eldritchhorror.fragment;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.activity.MainActivity;

public class SignOutDialogFragment extends DialogFragment {

    private MainActivity activity;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.rate_title)
                .setIcon(R.drawable.sign_out)
                .setMessage(R.string.rate_massage)
                .setPositiveButton(R.string.message_rate, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        activity.signOut();
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.message_later, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }
}