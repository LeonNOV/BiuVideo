package com.leon.biuvideo.greendao.daoutils;

import android.content.Context;

import com.leon.biuvideo.greendao.dao.DaoBaseUtils;
import com.leon.biuvideo.greendao.dao.DaoManager;
import com.leon.biuvideo.greendao.dao.DownloadHistory;
import com.leon.biuvideo.greendao.dao.DownloadHistoryDao;

/**
 * @Author Leon
 * @Time 2021/4/1
 * @Desc 下载历史数据工具类
 */
public class DownloadHistoryUtils {
    private final DaoBaseUtils<DownloadHistory> downloadHistoryDaoUtils;

    public DownloadHistoryUtils(Context context) {
        DaoManager daoManager = new DaoManager(context);
        DownloadHistoryDao downloadHistoryDao = daoManager.getDaoSession().getDownloadHistoryDao();
        downloadHistoryDaoUtils = new DaoBaseUtils<>(DownloadHistory.class, downloadHistoryDao, daoManager.getDaoSession());
    }

    public DaoBaseUtils<DownloadHistory> getDownloadHistoryDaoUtils() {
        return downloadHistoryDaoUtils;
    }
}
