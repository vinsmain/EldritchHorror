package ru.mgusev.eldritchhorror;

import android.app.Application;

public class EHApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HelperFactory.setHelper(getApplicationContext());

        HelperFactory.setStaticHelper(getApplicationContext());
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        HelperFactory.releaseHelper();
    }
}
