package com.leon.biuvideo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.leon.biuvideo.values.FeaturesName;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 配置文件工具类
 */
public class PreferenceUtils {
    public static final String PREFERENCE_NAME = "preference";

    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String DISTRICT = "district";
    public static final String ADCODE = "adcode";
    public static final String IMG_ORIGINAL_MODEL = "imgOriginalModel";
    public static final String WEATHER_MODEL  = "weatherModel";
    public static final String COOKIE = "cookie";
    public static final String USER_ID = "userId";

    public static SharedPreferences PREFERENCE;

    /**
     * 设置位置信息
     *
     * @param address 位置信息
     */
    public static void setAddress(String[] address) {
        SharedPreferences.Editor editor = PREFERENCE.edit();
        editor.putString(PROVINCE, address[0]);
        editor.putString(CITY, address[1]);
        editor.putString(DISTRICT, address[2]);
        editor.apply();
    }

    /**
     * 获取位置信息
     *
     * @return  位置信息
     */
    public static String[] getAddress() {
        String[] address = new String[3];
        address[0] = PREFERENCE.getString(PROVINCE, null);
        address[1] = PREFERENCE.getString(CITY, null);
        address[2] = PREFERENCE.getString(DISTRICT, null);

        return address;
    }

    /**
     * 获取本地存放的adcode
     *
     * @return  adcode
     */
    public static String getAdcode() {
        return PREFERENCE.getString(ADCODE, null);
    }

    /**
     * 设置adcode
     */
    public static void setAdcode(String adcode) {
        SharedPreferences.Editor editor = PREFERENCE.edit();
        editor.putString(ADCODE, adcode).apply();
    }

    /**
     * 对设置界面对应功能的开关状态进行设置
     *
     * @param featuresName 功能名称
     * @param status    设置状态
     */
    public static void setFeaturesStatus(FeaturesName featuresName, boolean status) {
        SharedPreferences.Editor editor = PREFERENCE.edit();

        switch (featuresName) {
            case IMG_ORIGINAL_MODEL:
                editor.putBoolean(IMG_ORIGINAL_MODEL, status).apply();
                break;
            case WEATHER_MODEL:
                editor.putBoolean(WEATHER_MODEL, status).apply();
                break;
            default:
                break;
        }
    }

    /**
     * 获取设置界面中对应功能的开启状态
     *
     * @return  开关状态
     */
    public static boolean getFeaturesStatus(FeaturesName featuresName) {
        switch (featuresName) {
            case IMG_ORIGINAL_MODEL:
                return PREFERENCE.getBoolean(IMG_ORIGINAL_MODEL, false);
            case WEATHER_MODEL:
                return PREFERENCE.getBoolean(WEATHER_MODEL, false);
            default:
                return false;
        }
    }

    /**
     * 设置Cookie
     *
     * @param cookie    cookie内容
     */
    public static void setCookie(String cookie) {
        SharedPreferences.Editor editor = PREFERENCE.edit();
        editor.putString(COOKIE, cookie).apply();
    }

    /**
     * 获取Cookie
     */
    public static String getCookie() {
        return PREFERENCE.getString(COOKIE, null);
    }

    /**
     * 设置用户ID
     *
     * @param userId    用户ID
     */
    public static void setUserId(String userId) {
        SharedPreferences.Editor editor = PREFERENCE.edit();
        editor.putString(USER_ID, userId).apply();
    }

    /**
     * 获取用户ID
     *
     * @return  用户ID
     */
    public static String getUserId() {
        return PREFERENCE.getString(USER_ID, null);
    }
}
