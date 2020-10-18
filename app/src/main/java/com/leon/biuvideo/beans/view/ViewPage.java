package com.leon.biuvideo.beans.view;

import java.io.Serializable;
import java.util.List;

//view接口bean
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
    public UpInfo upInfo;
    public VideoInfo videoInfo;
    public List<SingleVideoInfo> singleVideoInfoList;

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
                ", upInfo=" + upInfo +
                ", videoInfo=" + videoInfo +
                ", singleVideoInfoList=" + singleVideoInfoList +
                '}';
    }
}
