package com.leon.biuvideo.beans.videoBean.view;

import java.io.Serializable;

/**
 * 单个视频选集信息
 */
public class AnthologyInfo implements Serializable {
    public long cid;
    public int page;
    public String part;
    public int duration;//视频长度

    @Override
    public String toString() {
        return "SingleVideoInfo{" +
                "cid=" + cid +
                ", page=" + page +
                ", part='" + part + '\'' +
                '}';
    }
}
