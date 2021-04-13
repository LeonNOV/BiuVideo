package com.leon.biuvideo.utils;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.task.DownloadTask;

/**
 * @Author Leon
 * @Time 2021/4/13
 * @Desc
 */
public class ResourcesUtils {

    public static void savePicture (String ... pictureUrl) {
        int count = pictureUrl.length;

        for (int i = 0; i < pictureUrl.length; i++) {
            Aria.download(ResourcesUtils.class).load(pictureUrl[i]).setFilePath("").create();
        }
    }
}
