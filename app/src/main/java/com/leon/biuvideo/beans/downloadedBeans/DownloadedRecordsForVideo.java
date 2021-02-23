package com.leon.biuvideo.beans.downloadedBeans;

import java.util.List;

/**
 * 下载记录实体类
 */
public class DownloadedRecordsForVideo {
    public String cover;
    public String title;
    public String upName;
    public String mainId;
    public int count;
    public List<DownloadedDetailMedia> subVideos;
}
