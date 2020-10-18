package com.leon.biuvideo.beans.view;

import java.io.Serializable;

//单个视频选集信息
public class SingleVideoInfo implements Serializable {
    public long cid;
    public int page;
    public String part;

    @Override
    public String toString() {
        return "SingleVideoInfo{" +
                "cid=" + cid +
                ", page=" + page +
                ", part='" + part + '\'' +
                '}';
    }
}
