package com.leon.biuvideo.greendao.daoutils;

import android.content.Context;

import com.leon.biuvideo.greendao.dao.DaoBaseUtils;
import com.leon.biuvideo.greendao.dao.DaoManager;
import com.leon.biuvideo.greendao.dao.SearchHistory;
import com.leon.biuvideo.greendao.dao.SearchHistoryDao;

/**
 * @Author Leon
 * @Time 2021/4/1
 * @Desc 搜索历史数据工具类
 */
public class SearchHistoryUtils {
    private final DaoBaseUtils<SearchHistory> searchHistoryDaoUtils;

    public SearchHistoryUtils(Context context) {
        DaoManager daoManager = new DaoManager(context);
        SearchHistoryDao searchHistoryDao = daoManager.getDaoSession().getSearchHistoryDao();
        searchHistoryDaoUtils = new DaoBaseUtils<>(SearchHistory.class, searchHistoryDao, daoManager.getDaoSession());
    }

    public DaoBaseUtils<SearchHistory> getSearchHistoryDaoUtils () {
        return searchHistoryDaoUtils;
    }
}
