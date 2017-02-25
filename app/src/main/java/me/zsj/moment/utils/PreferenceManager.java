package me.zsj.moment.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author zsj
 */

public class PreferenceManager {

    private static final String PREFS_NAME = "moment";

    private static SharedPreferences preferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor edit(Context context) {
        return preferences(context).edit();
    }

    public static void putBoolean(Context context, String key, boolean value) {
        edit(context).putBoolean(key, value).apply();
    }

    public static boolean getBooleanValue(Context context, String key) {
        return getBooleanValue(context, key, false);
    }

    public static boolean getBooleanValue(Context context, String key, boolean defaultValue) {
        return preferences(context).getBoolean(key, defaultValue);
    }

    public static void putInt(Context context, String key, int value) {
        edit(context).putInt(key, value).apply();
    }

    public static int getIntValue(Context context, String key, int defaultValue) {
        return preferences(context).getInt(key, defaultValue);
    }

}
