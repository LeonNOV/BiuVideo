package com.leon.biuvideo.service;

import android.content.Context;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.task.DownloadTask;
import com.leon.biuvideo.greendao.dao.DaoBaseUtils;
import com.leon.biuvideo.greendao.dao.DownloadHistory;
import com.leon.biuvideo.greendao.daoutils.DownloadHistoryUtils;
import com.leon.biuvideo.utils.downloadUtils.ResourceDownloadTask;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/5/10
 * @Desc 在MainActivity创建时进行创建，用于获取下载任务的状态，对数据库进行更新
 */
public class DownloadWatcher {
    private static final List<ResourceDownloadTask> RESOURCE_DOWNLOAD_TASK_LIST = new ArrayList<>();
    private DaoBaseUtils<DownloadHistory> downloadHistoryDaoUtils;

    public void init(Context applicationContext) {
        downloadHistoryDaoUtils = new DownloadHistoryUtils(applicationContext).getDownloadHistoryDaoUtils();

        Aria.download(this).register();
    }

    /**
     * 取消下载
     */
    @Download.onTaskCancel
    void onCancel (DownloadTask downloadTask) {
        if (downloadTask != null) {
            ResourceDownloadTask thisTask = findThisTask(downloadTask);

            if (thisTask != null) {
                updateStat(downloadTask, thisTask, false);
            }
        }
    }

    /**
     * 下载失败
     */
    @Download.onTaskFail
    void onFailed(DownloadTask downloadTask) {
        if (downloadTask != null) {
            ResourceDownloadTask thisTask = findThisTask(downloadTask);

            if (thisTask != null) {
                updateStat(downloadTask, thisTask, false);
            }
        }
    }

    /**
     * 下载完成
     */
    @Download.onTaskComplete
    void onCompleted(DownloadTask downloadTask) {
        if (downloadTask != null) {
            ResourceDownloadTask thisTask = findThisTask(downloadTask);

            if (thisTask != null) {
                updateStat(downloadTask, thisTask, true);
            }
        }
    }

    private void updateStat (DownloadTask downloadTask, ResourceDownloadTask resourceDownloadTask, boolean isComplete) {
        DownloadHistory downloadHistory = resourceDownloadTask.getDownloadHistory();
        if (isComplete) {
            downloadHistory.setFileSize(downloadTask.getFileSize());
            downloadHistory.setIsFailed(false);
            downloadHistoryDaoUtils.update(downloadHistory);
        }

        downloadHistory.setIsCompleted(isComplete);
        downloadHistoryDaoUtils.update(downloadHistory);

        remove(resourceDownloadTask);
    }

    public static ResourceDownloadTask findThisTask (DownloadTask downloadTask) {
        for (ResourceDownloadTask resourceDownloadTask : RESOURCE_DOWNLOAD_TASK_LIST) {
            if (resourceDownloadTask.getTaskId() == downloadTask.getEntity().getId()) {
                return resourceDownloadTask;
            }
        }

        return null;
    }

    public static void addTask (ResourceDownloadTask resourceDownloadTask) {
        RESOURCE_DOWNLOAD_TASK_LIST.add(resourceDownloadTask);
    }

    public static void remove (ResourceDownloadTask resourceDownloadTask) {
        RESOURCE_DOWNLOAD_TASK_LIST.remove(resourceDownloadTask);
    }

    public void release() {
        Aria.download(this).unRegister();
        RESOURCE_DOWNLOAD_TASK_LIST.clear();
    }
}
