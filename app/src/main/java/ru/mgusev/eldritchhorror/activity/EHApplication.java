package ru.mgusev.eldritchhorror.activity;

import android.app.Application;

import ru.mgusev.eldritchhorror.database.HelperFactory;

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
