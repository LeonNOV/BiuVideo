package com.leon.biuvideo.utils;

import android.os.Environment;

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
     * 写入文件前，检查文件夹状态的方法
     *
     * @return
     */
    public static String folderState(ResourcesFolder resourcesFolder ) {
        //获取根路径
        File rootDirectory = Environment.getExternalStorageDirectory();

        //在根目录创建名为BiuVideo的文件夹
        File biuVideoFolder = new File(rootDirectory, "BiuVideo");
        if (!biuVideoFolder.exists()) {
            biuVideoFolder.mkdirs();
        }

        String biuVideoDirectory = biuVideoFolder.getAbsolutePath();

        //创建对应文件夹并返回其绝对路径
        return createFolder(biuVideoDirectory, resourcesFolder);
    }

    /**
     * 创建文件夹
     *
     * @param parent    父路径
     * @param folderName    子文件夹名称
     */
    private static String createFolder(String parent, ResourcesFolder folderName) {
        File file = new File(parent, folderName.value);
        if (!file.exists()) {
            file.mkdirs();
        }

        return file.getAbsolutePath();
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
        SONGS("Songs");

        public String value;

        ResourcesFolder(String value) {
            this.value = value;
        }
    }
}
