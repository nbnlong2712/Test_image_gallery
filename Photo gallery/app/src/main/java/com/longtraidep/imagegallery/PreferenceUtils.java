package com.longtraidep.imagegallery;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {
    public PreferenceUtils(){}

    public static boolean saveTheme(String theme, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("Themes", theme);
        prefsEditor.apply();
        return true;
    }

    public static String getTheme(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("Themes", "");
    }
}
