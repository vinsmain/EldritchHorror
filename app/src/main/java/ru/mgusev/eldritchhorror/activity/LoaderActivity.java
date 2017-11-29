package ru.mgusev.eldritchhorror.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import ru.mgusev.eldritchhorror.R;

public class LoaderActivity extends Activity {

    private AdColonyHelper helper;
    private AdColonyTask task;
    private boolean isLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        if (savedInstanceState!= null) {
            isLock = savedInstanceState.getBoolean("LOCK");
        }

        helper = AdColonyHelper.getInstance(this);
        if (!isLock) {
            task = new AdColonyTask();
            task.execute();
        }
    }

    private void showSuccessAlert() {
        Toast.makeText(getApplicationContext(), R.string.view_success, Toast.LENGTH_LONG).show();
    }

    private void showErrorAlert() {
        Toast.makeText(getApplicationContext(), R.string.view_error, Toast.LENGTH_LONG).show();
    }

    public void addGame() {
        Intent intent = new Intent(this, GamesPagerActivity.class);
        intent.putExtra("ORIENTATION", getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
        startActivity(intent);
        if (helper.isOnReward()) showSuccessAlert();
        else showErrorAlert();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }

    private void setLock(boolean value) {
        isLock = value;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("LOCK", isLock);
    }

    private class AdColonyTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            setLock(true);
            helper.startRequestInterstitial();
            int i = 0;
            while (!helper.isAdvertisingLoad() && !helper.isNotFilled() && i < 30) {
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            setLock(false);
            if (helper.isAdvertisingLoad()) helper.getAdc().show();
            else addGame();
            finish();
        }
    }
}