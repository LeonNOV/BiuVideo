package com.leon.biuvideo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.leon.biuvideo.values.FeaturesName;

public class PreferenceUtils {
    public static final String PREFERENCE_NAME = "preference";

    private static SharedPreferences preference;

    private static void getSharedPreferences (Context context) {
        if (preference == null) {
            preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
    }

    /**
     * 设置位置信息
     *
     * @param context   context
     * @param address 位置信息
     */
    public static void setAddress(Context context, String[] address) {
        getSharedPreferences(context);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString("province", address[0]);
        editor.putString("city", address[1]);
        editor.putString("district", address[2]);
        editor.apply();
    }

    /**
     * 获取位置信息
     *
     * @param context   context
     * @return  位置信息
     */
    public static String[] getAddress(Context context) {
        getSharedPreferences(context);
        String[] address = new String[3];
        address[0] = preference.getString("province", null);
        address[1] = preference.getString("city", null);
        address[2] = preference.getString("district", null);

        return address;
    }

    /**
     * 获取本地存放的adcode
     *
     * @return  adcode
     */
    public static String getAdcode(Context context) {
        getSharedPreferences(context);
        return preference.getString("adcode", null);
    }

    /**
     * 设置adcode
     */
    public static void setAdcode(Context context, String adcode) {
        getSharedPreferences(context);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString("adcode", adcode).apply();
    }

    /**
     * 对设置界面对应功能的开关状态进行设置
     *
     * @param context   context
     * @param featuresName 功能名称
     * @param status    设置状态
     */
    public static void setFeaturesStatus(Context context, FeaturesName featuresName, boolean status) {
        getSharedPreferences(context);
        SharedPreferences.Editor editor = preference.edit();

        switch (featuresName) {
            case IMG_ORIGINAL_MODEL:
                editor.putBoolean("imgOriginalModel", status).apply();
                break;
            case WEATHER_MODEL:
                editor.putBoolean("weatherModel", status).apply();
                break;
            default:
                break;
        }
    }

    /**
     * 获取设置界面中对应功能的开启状态
     *
     * @param context   context
     * @return  开关状态
     */
    public static boolean getFeaturesStatus(Context context, FeaturesName featuresName) {
        getSharedPreferences(context);

        switch (featuresName) {
            case IMG_ORIGINAL_MODEL:
                return preference.getBoolean("imgOriginalModel", false);
            case WEATHER_MODEL:
                return preference.getBoolean("weatherModel", false);
            default:
                return false;
        }
    }
}
