package com.leon.biuvideo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.leon.biuvideo.ui.dialogs.WarnDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * 关于文件操作的工具类
 */
public class FileUtils {

    /**
     * 创建文件夹，默认在系统根目录中创建
     *
     * @param folderName    子文件夹名称
     */
    public static String createFolder(ResourcesFolder folderName) {
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BiuVideo";

        String resourcesPath = folderName.value;
        File file = new File(rootPath, resourcesPath);

        if (!file.exists()) {
            file.mkdirs();
        }

        return file.getAbsolutePath();
    }

    /**
     * 每次保存需要提前检查读写权限
     *
     * @param activity  申请权限的Activity
     */
    public static void verifyPermissions(Activity activity) {
        //检查权限有没有获取
        int permissionCheck = ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                WarnDialog warnDialog = new WarnDialog(activity, "读写权限", "由于保存资源文件时需要用到\"读写权限\"，否则将无法正常使用");
                warnDialog.setOnConfirmListener(new WarnDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        warnDialog.dismiss();
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1024);
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
     * 生成一个随机的文件名
     *
     * @param baseName  基本名称
     * @return  返回一个文件名
     */
    public static String generateFileName(String baseName) {
        SimpleDateFormat ymd = new SimpleDateFormat("yyyyMMddHHmm", Locale.CHINA);
        String format = ymd.format(new Date(System.currentTimeMillis()));

        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 5);

        return baseName + "-" + uuid + "-" + format;
    }

    public enum ResourcesFolder {
        VIDEOS("Videos"),
        PICTURES("Pictures"),
        MUSIC("Music"),
        TEMP("Temp");

        public String value;

        ResourcesFolder(String value) {
            this.value = value;
        }
    }
}
