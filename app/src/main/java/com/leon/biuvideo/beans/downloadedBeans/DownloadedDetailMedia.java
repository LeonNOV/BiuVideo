package com.leon.biuvideo.beans.downloadedBeans;

import java.io.Serializable;

/**
 * 下载列表中相同bvid的视频的实体类，音频条目也使用该类
 */
public class DownloadedDetailMedia implements Serializable {
    public String fileName;
    public String cover;
    public String title;
    public long size;
    public String mainId;
    public long subId;
    public String videoUrl;
    public String audioUrl;
    public int qualityId;   // 清晰度ID

    /**
     * 资源标记
     * 格式：
     *      视频：subId + "-" + qualityId
     *      音频：mainId
     */
    public String resourceMark;
    public boolean isVideo;

    /**
     * 下载状态：0：未下载，1：正在下载(如果第一次打开APP为此状态，则表示下载失败)， 2：下载完成
     */
    public int downloadState;
}
