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
    private static final long TWENTY_FOUR_HOURS = 8640000;

    public DateHelper(Context context) {
        this.context = context;
    }

    public void saveDate(long date) {
        sPref = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.putLong(SETTINGS_DATE, date);
        ed.apply();
    }

    private long loadDate() {
        sPref = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        return sPref.getLong(SETTINGS_DATE, 0);
    }

    public boolean isAdvertisingShow() {
        Date currentDate = new Date();
        if (loadDate() == 0 || loadDate() > currentDate.getTime()) saveDate(currentDate.getTime() - TWENTY_FOUR_HOURS);
        return currentDate.getTime() - loadDate() >= TWENTY_FOUR_HOURS;
    }
}
