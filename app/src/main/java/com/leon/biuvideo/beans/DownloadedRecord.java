package com.leon.biuvideo.beans;

import com.leon.biuvideo.greendao.dao.DownloadHistory;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/5/14
 * @Desc 已完成下载记录
 */
public class DownloadedRecord {
    public int count;

    public String cover;
    public String title;
    public String fileSize;

    /**
     * 如果LevelOnId的子记录只有一条,则该变量不为null
     */
    public DownloadHistory downloadHistory;

    /**
     * 如果LevelOnId的子记录有多条,则该变量不为null
     */
    public List<DownloadHistory> downloadHistoryList;
}
