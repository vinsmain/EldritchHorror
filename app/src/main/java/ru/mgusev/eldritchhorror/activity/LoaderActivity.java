package ru.mgusev.eldritchhorror.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import ru.mgusev.eldritchhorror.R;
import ru.mgusev.eldritchhorror.fragment.DonateDialogFragment;

public class LoaderActivity extends Activity {

    MainActivity mainActivity;
    AdColonyHelper helper;
    AdColonyTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        helper = new AdColonyHelper(this);
        task = new AdColonyTask();
        task.execute();
    }

    private void showAlert() {
        Toast.makeText(getBaseContext(), R.string.view_error, Toast.LENGTH_LONG).show();
    }

    private void showAdvertising() {
        helper.getAdc().show();
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    private class AdColonyTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            if (helper.getAdc() == null) {
                helper.startRequestInterstitial();
                int i = 0;
                while (!helper.isAdvertisingLoad() && !helper.isNotFolled()) {
                    /*i++;
                    try {
                        TimeUnit.MILLISECONDS.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i == 10) break;*/
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (helper.getAdc() != null) {
                showAdvertising();
                finish();
                /*while (true) {
                    if (helper.isAdvertisingLoad()) {
                        //((DonateDialogFragment)getCallingActivity())..addGame();
                        break;
                    }
                }*/
            } else {
                //mainActivity.addGame();
                showAlert();
                finish();
            }
        }
    }
}
