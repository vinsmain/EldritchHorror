package ru.mgusev.eldritchhorror.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Localization {

    private static Localization instance;
    private static List<String> localeList;
    private static String locale;

    private Localization() {
        localeList = new ArrayList<>();
        localeList.add("ab");
        localeList.add("be");
        localeList.add("kk");
        localeList.add("ky");
        localeList.add("ru");
        localeList.add("uk");
    }

    public static Localization getInstance(){
        if (null == instance){
            instance = new Localization();
        }
        return instance;
    }

    public boolean isRusLocale() {
        locale = Locale.getDefault().getLanguage();
        return localeList.contains(locale);
    }
}