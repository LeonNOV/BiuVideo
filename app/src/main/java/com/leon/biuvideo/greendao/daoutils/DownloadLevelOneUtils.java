package com.leon.biuvideo.greendao.daoutils;

import android.content.Context;

import com.leon.biuvideo.greendao.dao.DaoBaseUtils;
import com.leon.biuvideo.greendao.dao.DaoManager;
import com.leon.biuvideo.greendao.dao.DownloadLevelOne;
import com.leon.biuvideo.greendao.dao.DownloadLevelOneDao;

/**
 * @Author Leon
 * @Time 2021/5/4
 * @Desc 一级下载记录工具类
 */
public class DownloadLevelOneUtils {
    private final DaoBaseUtils<DownloadLevelOne> downloadLevelOneDaoBaseUtils;

    public DownloadLevelOneUtils(Context context) {
        DaoManager daoManager = new DaoManager(context);
        DownloadLevelOneDao downloadLevelOneDao = daoManager.getDaoSession().getDownloadLevelOneDao();
        downloadLevelOneDaoBaseUtils = new DaoBaseUtils<>(DownloadLevelOne.class, downloadLevelOneDao, daoManager.getDaoSession());
    }

    public DaoBaseUtils<DownloadLevelOne> getDownloadLevelOneDaoBaseUtils() {
        return downloadLevelOneDaoBaseUtils;
    }
}
