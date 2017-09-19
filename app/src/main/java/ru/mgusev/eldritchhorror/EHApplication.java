package ru.mgusev.eldritchhorror;

import android.app.Application;

public class EHApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HelperFactory.setHelper(getApplicationContext());
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        HelperFactory.releaseHelper();
    }
}
