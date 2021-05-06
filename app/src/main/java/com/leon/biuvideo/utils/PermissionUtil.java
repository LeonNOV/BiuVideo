package com.leon.biuvideo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.leon.biuvideo.ui.views.WarnDialog;

/**
 * 权限申请类
 */
public class PermissionUtil {
    private final Fragment fragment;
    private final Context context;

    public PermissionUtil(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    /**
     * 申请权限
     *
     * @param permission {@link Permission}
     */
    public void verifyPermission(Permission permission) {
        switch (permission) {
            case RW:
                verifyIOPermission();
                break;
            case LOCATION:
                verifyLocationPermission();
                break;
            default:
                break;
        }
    }

    /**
     * 申请读写权限
     */
    private void verifyIOPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            fragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1025);
        }
    }

    /**
     * 申请定位权限
     */
    private void verifyLocationPermission() {
        int accessCoarseLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        int accessFineLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

        if (accessCoarseLocation != PackageManager.PERMISSION_GRANTED || accessFineLocation != PackageManager.PERMISSION_GRANTED) {
            fragment.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1025);
        }
    }

    /**
     * 验证是否已授予指定权限
     *
     * @param context   context
     * @param permission    {@link Permission}
     * @return  true:已授予，false:未授予
     */
    public static boolean verifyPermission(Context context, Permission permission) {
        switch (permission) {
            // 验证定位权限
            case LOCATION:
                return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            // 验证读写权限
            case RW:
                return ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            default:
                return false;
        }
    }

    public enum Permission {
        // 读写权限
        RW(0),

        // 定位权限
        LOCATION(1);

        public int value;

        Permission(int value) {
            this.value = value;
        }
    }
}
