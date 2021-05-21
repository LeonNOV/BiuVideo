package com.leon.biuvideo.greendao.dao;

import android.content.Context;

/**
 * @Author Leon
 * @Time 2021/4/1
 * @Desc Dao管理器
 */
public class DaoManager {
    private static final String DB_NAME = "biuvideo";
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private final Context context;
    private DaoMaster.DevOpenHelper devOpenHelper;

    public DaoManager(Context context) {
        this.context = context;
    }

    /**
     * 判断是否存在数据库，如果没有就创建
     */
    public DaoMaster getDaoMaster () {
        if (daoMaster == null) {
            devOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME);
            daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        }

        return daoMaster;
    }

    /**
     * 获取Session对象
     *
     * @return  DaoSession
     */
    public DaoSession getDaoSession () {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }

            daoSession = daoMaster.newSession();
        }

        return daoSession;
    }

    /**
     * 关闭与数据库的链接
     */
    public void closeConnection () {
        closeHelper();
        closeDaoSession();
    }

    private void closeHelper () {
        if (devOpenHelper != null) {
            devOpenHelper.close();
            devOpenHelper = null;
        }
    }

    private void closeDaoSession () {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
    }
}
