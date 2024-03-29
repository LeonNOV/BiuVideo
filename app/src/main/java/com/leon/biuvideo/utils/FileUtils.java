package com.leon.biuvideo.utils;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * @Author Leon
 * @Time 2020/10/28
 * @Desc 关于文件操作的工具类
 */
public class FileUtils {

    /**
     * 创建文件夹，默认在系统根目录中创建
     *
     * @param folderName 子文件夹名称
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
     * 创建文件夹，默认在系统根目录中创建
     *
     * @param folderName 子文件夹名称
     */
    public static String createFolder(String folderName) {
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BiuVideo";

        File file = new File(rootPath, folderName);

        if (!file.exists()) {
            file.mkdirs();
        }

        return file.getAbsolutePath();
    }

    /**
     * 生成一个随机的文件名
     *
     * @param baseName 基本名称
     * @return 返回一个文件名
     */
    public static String generateFileName(String baseName) {
        SimpleDateFormat ymd = new SimpleDateFormat("yyyyMMddHHmm", Locale.CHINA);
        String format = ymd.format(new Date(System.currentTimeMillis()));

        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 5);

        return baseName + "-" + uuid + "-" + format;
    }

    /**
     * 获取缓存文件大小
     *
     * @param cacheFile 应用cache文件夹路径
     * @return 返回cache文件夹大小(byte)
     */
    public static long getCacheSize(File cacheFile) {
        long size = 0;

        File[] files = cacheFile.listFiles();
        for (File file : files) {

            //判断是否还存在有文件夹
            if (file.isDirectory()) {
                size += getCacheSize(file);
            } else {
                size += file.length();
            }
        }

        return size;
    }

    /**
     * 删除缓存
     *
     * @param cacheFile 缓存路径
     */
    public static void cleanCache(File cacheFile) {
        if (cacheFile.isDirectory()) {
            String[] list = cacheFile.list();

            for (String s : list) {
                cleanCache(new File(cacheFile, s));
            }
        } else {
            cacheFile.delete();
        }
    }

    /**
     * 获取Assets文件内容
     *
     * @param context   context
     * @param assetsName    assets文件名称
     * @return  内容
     */
    public static String getAssetsContent(Context context, String assetsName) {
        try {
            InputStream inputStream = context.getAssets().open(assetsName);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String temp;
            StringBuilder stringBuilder = new StringBuilder();
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp);
            }

            bufferedReader.close();
            inputStream.close();

            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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
