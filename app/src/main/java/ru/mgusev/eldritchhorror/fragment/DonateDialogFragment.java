package ru.mgusev.eldritchhorror.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.activity.AdColonyHelper;
import ru.mgusev.eldritchhorror.activity.MainActivity;

public class DonateDialogFragment extends DialogFragment {

    MainActivity mainActivity;
    AdColonyHelper helper;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        helper = new AdColonyHelper(mainActivity);
        //helper.startRequestInterstitial();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.view_question)
                .setIcon(R.drawable.video)
                .setMessage(R.string.donate_message)
                .setPositiveButton(R.string.messageOK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (helper.getAdc() == null) {
                            helper.startRequestInterstitial();
                            while (!helper.isAdvertisingLoad()) {
                                for (int i = 0; i < 1000; i++) {

                                        Toast.makeText(mainActivity, "Загрузка...", Toast.LENGTH_LONG).show();

                                }
                            }
                        }

                        if (helper.getAdc() != null) {
                            helper.getAdc().show();
                            while (true) {
                                if (helper.isAdvertisingLoad()) {
                                    mainActivity.addGame();
                                    break;
                                }
                            }
                        } else {
                            mainActivity.addGame();
                            showAlert();
                        }
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

    private void showAlert() {
        Toast.makeText(mainActivity, R.string.view_error, Toast.LENGTH_LONG).show();
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
