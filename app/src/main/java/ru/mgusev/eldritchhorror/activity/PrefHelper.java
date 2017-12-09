package ru.mgusev.eldritchhorror.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.Date;

public class PrefHelper {

    private SharedPreferences sPref;
    private MainActivity activity;
    private static final String SETTINGS = "settings";
    private static final String SETTINGS_DATE = "settings_date";
    private static final String SETTINGS_IS_RATE = "settings_is_rate";
    private static final String SETTINGS_GAMES_COUNT = "settings_games_count";
    private static final long TWENTY_FOUR_HOURS = 86400000; //milliseconds

    public PrefHelper(MainActivity activity) {
        this.activity = activity;
    }

    public void saveDate(long date) {
        sPref = activity.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.putLong(SETTINGS_DATE, date);
        ed.apply();
    }

    private long loadDate() {
        sPref = activity.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        return sPref.getLong(SETTINGS_DATE, 0);
    }

    public boolean isAdvertisingShow() {
        Date currentDate = new Date();
        if (loadDate() == 0 || loadDate() > currentDate.getTime()) saveDate(currentDate.getTime() - TWENTY_FOUR_HOURS);
        return currentDate.getTime() - loadDate() >= TWENTY_FOUR_HOURS;
    }

    public void saveIsRate(boolean value) {
        sPref = activity.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.putBoolean(SETTINGS_IS_RATE, value);
        ed.putInt(SETTINGS_GAMES_COUNT, activity.getGamesCount() + 10);
        ed.apply();
    }

    private boolean loadIsRate() {
        sPref = activity.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        return sPref.getBoolean(SETTINGS_IS_RATE, false);
    }

    private int loadGamesCount() {
        sPref = activity.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        return sPref.getInt(SETTINGS_GAMES_COUNT, 10);
    }

    public boolean isRate() {
        System.out.println(!loadIsRate() + " " + (activity.getGamesCount() >= loadGamesCount()));
        return !loadIsRate() && activity.getGamesCount() >= loadGamesCount();
    }
}