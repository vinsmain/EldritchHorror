package ru.mgusev.eldritchhorror.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Date;

public class DateHelper {

    private SharedPreferences sPref;
    private Context context;
    private static final String SETTINGS = "settings";
    private static final String SETTINGS_DATE = "settings_date";

    public DateHelper(Context context) {
        this.context = context;
    }

    public void saveDate() {
        sPref = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        Editor ed = sPref.edit();
        Date date = new Date();
        ed.putLong(SETTINGS_DATE, date.getTime());
        ed.apply();
    }

    public void loadDate() {
        sPref = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        Long dateLong = sPref.getLong(SETTINGS_DATE, 0);
        System.out.println(new Date(dateLong));
    }

    public boolean isAdvertisingShow() {
        return true;
    }
}
