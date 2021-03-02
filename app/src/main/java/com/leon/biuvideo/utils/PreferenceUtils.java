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
     * 设置经纬度
     *
     * @param context   context
     * @param longitude 经度
     * @param latitude  维度
     */
    public static void setLongAndLat(Context context, double longitude, double latitude) {
        getSharedPreferences(context);
        SharedPreferences.Editor editor = preference.edit();
        editor.putFloat("longitude", (float) longitude);
        editor.putFloat("latitude", (float) latitude);
        editor.apply();
    }

    /**
     * 获取经纬度
     *
     * @param context   context
     * @return  经纬度
     */
    public static double[] getLongAndLat(Context context) {
        getSharedPreferences(context);
        double[] longAndLat = new double[2];
        longAndLat[0] = preference.getFloat("longitude", 0);
        longAndLat[1] = preference.getFloat("latitude", 0);

        return longAndLat;
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

    /**
     * 获取天气模块开关状态
     *
     * @param context   context
     * @return  开关状态
     */
    public static boolean getWeatherModelStatus(Context context) {
        getSharedPreferences(context);
        return preference.getBoolean("weatherModel", false);
    }
}
