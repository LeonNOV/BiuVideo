package com.leon.biuvideo.beans.resourcesBeans.bangumiBeans;

import java.io.Serializable;

/**
 * @Author Leon
 * @Time 2021/3/21
 * @Desc 番剧单集数据
 */
public class BangumiAnthology implements Serializable {
    public String seasonId;
    public String aid;
    public String bvid;
    public String cid;
    public String id;

    public String badge;
    public String cover;
    public String mainTitle;
    public String subTitle;

    public long pubTime;
    public String shortLink;

    public boolean isDownloading;
    public boolean isDownloaded;
}
