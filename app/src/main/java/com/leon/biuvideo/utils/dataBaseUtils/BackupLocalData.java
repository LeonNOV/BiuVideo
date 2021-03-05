package com.leon.biuvideo.utils.dataBaseUtils;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.Follow;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedDetailMedia;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedRecordsForVideo;
import com.leon.biuvideo.beans.orderBeans.LocalOrder;
import com.leon.biuvideo.beans.orderBeans.LocalVideoFolder;
import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.values.LocalOrderType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class BackupLocalData {
    private final Context context;
    private LocalOrdersDatabaseUtils localOrdersDatabaseUtils;
    private DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils;

    public BackupLocalData(Context context) {
        this.context = context;
    }

    public void execute() {
        JSONObject jsonObject = new JSONObject();
        JSONObject localOrders = backUpLocalOrders();
        jsonObject.put("localOrders", localOrders);

        JSONObject downloaded = backUpDownloaded();
        jsonObject.put("downloaded", downloaded);

        JSONObject favorites = backUpFav();
        jsonObject.put("favorites", favorites);

        String jsonString = jsonObject.toString();

        String backUp = FileUtils.createFolder("BackUp");

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(new File(backUp, "backup_" + System.currentTimeMillis()));
            fileWriter.write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private JSONObject backUpDownloaded() {
        JSONObject downloadedJSONObject = new JSONObject();

        try {
            downloadRecordsDatabaseUtils = new DownloadRecordsDatabaseUtils(context);
            // 获取所有视频下载记录
            List<DownloadedRecordsForVideo> downloadedRecordsForVideos = downloadRecordsDatabaseUtils.queryAllSubVideo();
            downloadedJSONObject.put("downloadedVideos", downloadedRecordsForVideos);

            // 获取音频下载记录
            List<DownloadedDetailMedia> downloadedDetailMedia = downloadRecordsDatabaseUtils.queryAllMusic();
            downloadedJSONObject.put("downloadedAudios", downloadedDetailMedia);
        } finally {
            if (downloadRecordsDatabaseUtils != null) {
                downloadRecordsDatabaseUtils.close();
            }
        }

        return downloadedJSONObject;
    }

    private JSONObject backUpFav() {
        JSONObject favoritesJSONObject = new JSONObject();

        // 获取所有关注数据
        FavoriteUserDatabaseUtils favoriteUserDatabaseUtils = new FavoriteUserDatabaseUtils(context);
        List<Follow> follows = favoriteUserDatabaseUtils.queryFavorites(false);

        try {
            favoritesJSONObject.put("favorite", follows);
        } finally {
            if (favoriteUserDatabaseUtils != null) {
                favoriteUserDatabaseUtils.close();
            }
        }

        return favoritesJSONObject;
    }

    private JSONObject backUpLocalOrders() {
        JSONObject localOrderJSONObject = new JSONObject();

        // 获取本地订阅数据
        try {
            localOrdersDatabaseUtils = new LocalOrdersDatabaseUtils(context);
            List<LocalVideoFolder> localVideoFolderList = localOrdersDatabaseUtils.queryAllLocalVideoFolder();
            localOrderJSONObject.put("localVideoFolders", localVideoFolderList);

            List<LocalOrder> localVideos = localOrdersDatabaseUtils.queryLocalOrder(LocalOrderType.VIDEO);
            localOrderJSONObject.put("localOrderVideos",  localVideos);
            
            List<LocalOrder> bangumis = localOrdersDatabaseUtils.queryLocalOrder(LocalOrderType.BANGUMI);
            localOrderJSONObject.put("localOrderBangumis",  bangumis);
            
            List<LocalOrder> audios = localOrdersDatabaseUtils.queryLocalOrder(LocalOrderType.AUDIO);
            localOrderJSONObject.put("localOrderAudios",  audios);
            
            List<LocalOrder> articles = localOrdersDatabaseUtils.queryLocalOrder(LocalOrderType.ARTICLE);
            localOrderJSONObject.put("localOrderArticles",  articles);
        } finally {
            if (localOrdersDatabaseUtils != null) {
                localOrdersDatabaseUtils.close();
            }
        }

        return localOrderJSONObject;
    }
}
