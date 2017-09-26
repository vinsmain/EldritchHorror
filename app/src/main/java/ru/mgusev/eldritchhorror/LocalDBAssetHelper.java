package ru.mgusev.eldritchhorror;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class LocalDBAssetHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "EHLocalDB.db";
    private static final int DATABASE_VERSION = 4;

    public LocalDBAssetHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
