package com.leon.biuvideo.beans.videoBean.view;

import java.io.Serializable;

//视频信息
public class VideoInfo implements Serializable {
    public int view;
    public int danmaku;
    public int reply;
    public int favorite;
    public int like;
    public int coin;
    public int share;

    @Override
    public String toString() {
        return "VideoInfo{" +
                "view=" + view +
                ", danmaku=" + danmaku +
                ", reply=" + reply +
                ", favorite=" + favorite +
                ", like=" + like +
                ", coin=" + coin +
                ", share=" + share +
                '}';
    }
}
