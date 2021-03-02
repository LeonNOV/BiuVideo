package com.leon.biuvideo.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationUtil {
    private final Context context;
    private LocationManager locationManager;
    private SimpleLocationListener simpleLocationListener;

    public LocationUtil(Context context) {
        this.context = context;
    }

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
        String bestProvider = locationManager.getBestProvider(criteria, true);

        // 检查权限
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(context, "未授予定位权限，定位失败", Toast.LENGTH_SHORT).show();
            return;
        }

        simpleLocationListener = new SimpleLocationListener();
        locationManager.requestLocationUpdates(bestProvider, 0, 0, simpleLocationListener);
    }

    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        return criteria;
    }

    public void removeListener() {
        if (locationManager != null) {
            locationManager.removeUpdates(simpleLocationListener);
        }
    }

    public static class SimpleLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            // 获取经纬度
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            Fuck.blue("经纬度：" + longitude + "," + latitude);
        }
    }
}
