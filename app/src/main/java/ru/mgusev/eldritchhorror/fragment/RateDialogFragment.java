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

public class RateDialogFragment extends DialogFragment {

    private MainActivity activity;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.rate_title)
                .setIcon(R.drawable.rate_star)
                .setMessage(R.string.rate_massage)
                .setPositiveButton(R.string.messageOK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName()); // Go to Android market
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(goToMarket);
                            System.out.println("123414");
                            activity.getDateHelper().saveIsRate(true);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(activity, R.string.rate_error, Toast.LENGTH_LONG).show();
                        }
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.messageCancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        activity.getDateHelper().saveIsRate(false);
                        dialog.cancel();
                    }
                });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        // Create the AlertDialog object and return it
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        activity.getDateHelper().saveIsRate(false);
        super.onDismiss(dialog);
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }
}