package com.leon.biuvideo.utils;

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
    public static final String VIP_STATUS = "vipStatus";
    public static final String LOGIN_STATUS = "loginStatus";
    public static final String LOCATION_SERVICE = "locationServiceStatus";
    public static final String MANUAL_SET_LOCATION = "manualSetLocationStatus";
    public static final String RECOMMEND_COLUMNS = "recommendColumns";
    public static final String EASTER_RGG_STAT = "easterEggStat";
    public static final String PLAY_QUALITY = "playQuality";
    public static final String DOWNLOAD_QUALITY = "downloadQuality";
    public static final String FIRST_OPEN_STATUS = "firstOpenStatus";

    public static SharedPreferences PREFERENCE;
    private static final int DEF_QUALITY = 80;

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
        address[0] = PREFERENCE.getString(PROVINCE, "?");
        address[1] = PREFERENCE.getString(CITY, "?");
        address[2] = PREFERENCE.getString(DISTRICT, "?");

        return address;
    }

    /**
     * 获取adcode
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

    /**
     * 设置VIP状态
     *
     * @param status   true：大会员；false：普通会员
     */
    public static void setVipStatus (boolean status) {
        SharedPreferences.Editor edit = PREFERENCE.edit();
        edit.putBoolean(VIP_STATUS, status).apply();
    }

    /**
     * 获取VIP状态
     *
     * @return  true：大会员；false：普通会员
     */
    public static boolean getVipStatus() {
        return PREFERENCE.getBoolean(VIP_STATUS, false);
    }

    /**
     * 设置VIP状态
     *
     * @param status   true：已登录；false：未登录
     */
    public static void setLoginStatus (boolean status) {
        SharedPreferences.Editor edit = PREFERENCE.edit();
        edit.putBoolean(LOGIN_STATUS, status).apply();
    }

    /**
     * 获取登录状态
     *
     * @return  true：已登录；false：未登录
     */
    public static boolean getLoginStatus() {
        return PREFERENCE.getBoolean(LOGIN_STATUS, false);
    }

    /**
     * 设置定位服务开启状态
     *
     * @param status    开启状态
     */
    public static void setLocationServiceStatus(boolean status) {
        SharedPreferences.Editor editor = PREFERENCE.edit();
        editor.putBoolean(LOCATION_SERVICE, status).apply();
    }

    /**
     * 获取定位服务开启状态
     *
     * @return  开启状态
     */
    public static boolean getLocationServiceStatus() {
        return PREFERENCE.getBoolean(LOCATION_SERVICE, false);
    }

    /**
     * 设置手动设置位置状态
     *
     * @param status    开启状态
     */
    public static void setManualSetLocationStatus(boolean status) {
        SharedPreferences.Editor editor = PREFERENCE.edit();
        editor.putBoolean(MANUAL_SET_LOCATION, status).apply();
    }

    /**
     * 获取手动设置位置状态
     *
     * @return  开启状态
     */
    public static boolean getManualSetLocationStatus() {
        return PREFERENCE.getBoolean(MANUAL_SET_LOCATION, false);
    }

    /**
     * 设置推荐视图显示样式
     *
     * @param columns   1：单列；2：双列
     */
    public static void setRecommendColumns (int columns) {
        SharedPreferences.Editor edit = PREFERENCE.edit();
        edit.putInt(RECOMMEND_COLUMNS, columns).apply();
    }

    /**
     * 获取推荐视图样式
     *
     * @return  样式，默认为双列
     */
    public static int getRecommendColumns() {
        int anInt = PREFERENCE.getInt(RECOMMEND_COLUMNS, 2);
        return anInt >= 2 ? 2 : 1;
    }

    /**
     * @return  获取默认播放清晰度
     */
    public static int getPlayQuality () {
        return PREFERENCE.getInt(PLAY_QUALITY, DEF_QUALITY);
    }

    /**
     * 设置默认播放清晰度
     *
     * @param qualityCode   清晰度
     */
    public static void setPlayQuality (int qualityCode) {
        SharedPreferences.Editor editor = PREFERENCE.edit();
        editor.putInt(PLAY_QUALITY, qualityCode).apply();
    }

    /**
     * @return  获取默认下载清晰度
     */
    public static int getDownloadQuality () {
        return PREFERENCE.getInt(DOWNLOAD_QUALITY, DEF_QUALITY);
    }

    /**
     * 设置默认下载清晰度
     *
     * @param qualityCode   清晰度
     */
    public static void setDownloadQuality (int qualityCode) {
        SharedPreferences.Editor editor = PREFERENCE.edit();
        editor.putInt(DOWNLOAD_QUALITY, qualityCode).apply();
    }

    public static boolean getEasterEggStat () {
        return PREFERENCE.getBoolean(EASTER_RGG_STAT, false);
    }

    public static void setEasterEggStat () {
        SharedPreferences.Editor editor = PREFERENCE.edit();
        editor.putBoolean(EASTER_RGG_STAT, true).apply();
    }

    /**
     * 首次打开状态
     *
     * @return  true：首次；false：不是首次
     */
    public static boolean getFirstOpenStatus() {
        boolean status = PREFERENCE.getBoolean(FIRST_OPEN_STATUS, true);
        if (status) {
            SharedPreferences.Editor editor = PREFERENCE.edit();
            editor.putBoolean(FIRST_OPEN_STATUS, false).apply();
        }

        return status;
    }
}
