package com.leon.biuvideo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {
    public static final String PREFERENCE_NAME = "preference";

    private static SharedPreferences preference;

    public static SharedPreferences getPreference (Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    private static void getSharedPreferences (Context context) {
        if (preference == null) {
            preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
    }

    /**
     * 获取本地存放的adcode
     *
     * @return  adcode
     */
    public static String getAdcode(Context context) {
        getSharedPreferences(context);
        return preference.getString("adcode", "");
    }

    /**
     * 获取本地存放的省份和城市字符串
     *
     * @return  省份 + "," + 城市
     */
    public static String getLocation(Context context) {
        getSharedPreferences(context);
        return preference.getString("province", "") + "," + preference.getString("city", "");
    }
}
