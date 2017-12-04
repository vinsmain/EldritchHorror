package ru.mgusev.eldritchhorror.database;

import android.content.Context;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class HelperFactory {
    private static DatabaseHelper databaseHelper;
    private static DatabaseLocalHelper databaseLocalHelper;

    public static DatabaseHelper getHelper(){
        return databaseHelper;
    }

    public static DatabaseLocalHelper getStaticHelper(){
        return databaseLocalHelper;
    }

    public static void setHelper(Context context){
        databaseHelper = DatabaseHelper.getHelper(context);
    }

    public static void setStaticHelper(Context context){
        databaseLocalHelper = DatabaseLocalHelper.getHelper(context);
    }

    public static void releaseHelper(){
        OpenHelperManager.releaseHelper();
        databaseHelper = null;
        databaseLocalHelper = null;
    }
}