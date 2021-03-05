package com.leon.biuvideo.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * 定位工具类
 */
public class LocationUtil {
    private final Context context;
    private LocationManager locationManager;
    private Location lastKnownLocation;
    private String bestProvider;

    public LocationUtil(Context context) {
        this.context = context;
    }

    /**
     * 初始化LocationManager
     */
    private void initLocationManager() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 未开启GPS则跳转到开启GPS页面
        if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            // 转到手机设置界面，用户设置GPS
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public void location() {
        initLocationManager();

        Criteria criteria = getCriteria();

        // 获取最佳服务对象
        // GPS和NetWork二选一
        bestProvider = locationManager.getBestProvider(criteria, true);

        // 检查权限
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
    }

    /**
     * 获取定位标准
     *
     * @return  criteria
     */
    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        return criteria;
    }

    /**
     * 更新location
     */
    private void updateLocation() {
        // 检查权限
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);
    }

    /**
     * 将经纬度转换成中文地址
     *
     * @param location  location
     * @return  位置数组
     */
    private String[] getLocationAddress(Location location) {
        String[] addressStrings = new String[3];
        Geocoder geoCoder = new Geocoder(context, Locale.CHINESE);
        try {
            List<Address> addresses = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Address address = addresses.get(0);

            addressStrings[0] = address.getAdminArea(); // province
            addressStrings[1]  = address.getLocality();  // city
            addressStrings[2]  = address.getSubLocality();  // district
//            addressStrings[3]  = address.getSubAdminArea(); // street
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressStrings;
    }

    /**
     * 获取当前的位置信息
     * <br/>
     * 该方法会每次刷新一次位置
     *
     * @return  [0]:province(省份),[1]:city(城市),[2]:district(区县),[3]:street(街道)
     */
    public String[] getAddress() {
        updateLocation();

        return getLocationAddress(lastKnownLocation);
    }
}