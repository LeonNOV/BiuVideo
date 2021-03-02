package com.leon.biuvideo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.leon.biuvideo.ui.dialogs.WarnDialog;

/**
 * 权限申请类
 */
public class PermissionUtil {
    private Fragment fragment;
    private Activity activity;
    private final Context context;

    public PermissionUtil(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

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
        //检查权限有没有获取
        int permissionCheck = ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                WarnDialog warnDialog = new WarnDialog(activity, "读写权限", "由于保存资源文件时需要用到\"读写权限\"，否则将无法正常使用");
                warnDialog.setOnWarnActionListener(new WarnDialog.OnWarnActionListener() {
                    @Override
                    public void onConfirm() {
                        warnDialog.dismiss();
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1024);
                    }

                    @Override
                    public void onCancel() {
                        warnDialog.dismiss();
                    }
                });
                warnDialog.show();
            } else {
                //申请读写权限
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1024);
            }
        }
    }

    /**
     * 申请定位权限
     */
    private void verifyLocationPermission() {
        int access_coarse_location = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        int access_fine_location = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

        if (access_coarse_location != PackageManager.PERMISSION_GRANTED || access_fine_location != PackageManager.PERMISSION_GRANTED) {
            fragment.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1025);
        }
    }

    public enum Permission {
        RW(0),  // 读写权限
        LOCATION(1);    // 定位权限

        public int value;

        Permission(int value) {
            this.value = value;
        }
    }
}
