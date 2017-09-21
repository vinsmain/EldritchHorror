package ru.mgusev.eldritchhorror;

import android.content.Context;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class HelperFactory {
    private static DatabaseHelper databaseHelper;
    private static DatabaseStaticHelper databaseStaticHelper;

    public static DatabaseHelper getHelper(){
        return databaseHelper;
    }

    public static DatabaseStaticHelper getStaticHelper(){
        return databaseStaticHelper;
    }

    public static void setHelper(Context context){
        databaseHelper = DatabaseHelper.getHelper(context);
    }

    public static void setStaticHelper(Context context){
        databaseStaticHelper = DatabaseStaticHelper.getHelper(context);
    }

    public static void releaseHelper(){
        OpenHelperManager.releaseHelper();
        databaseHelper = null;
        databaseStaticHelper = null;
    }
}