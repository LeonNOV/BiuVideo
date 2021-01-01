package com.leon.biuvideo.beans.videoBean.view;

import java.io.Serializable;
import java.util.List;

/**
 * view接口实体类
 */
public class ViewPage implements Serializable {
    public String bvid;
    public long aid;
    public int videos;
    public int tid;
    public String tname;
    public String coverUrl;
    public String title;
    public long upTime;
    public String desc;
    public UserInfo userInfo;
    public VideoInfo videoInfo;
    public List<AnthologyInfo> anthologyInfoList;

    @Override
    public String toString() {
        return "ViewPage{" +
                "bvid='" + bvid + '\'' +
                ", aid=" + aid +
                ", videos=" + videos +
                ", tid=" + tid +
                ", tname='" + tname + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", title='" + title + '\'' +
                ", upTime=" + upTime +
                ", desc='" + desc + '\'' +
                ", upInfo=" + userInfo +
                ", videoInfo=" + videoInfo +
                ", singleVideoInfoList=" + anthologyInfoList +
                '}';
    }
}
