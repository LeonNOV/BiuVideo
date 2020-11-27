package com.leon.biuvideo.beans.videoBean.play;

import java.io.Serializable;
import java.util.List;

/**
 * video和audio通用
 */
public class Media implements Serializable {
    public String baseUrl;
    public List<String> backupUrl;

    //暂不使用
    public String codecs;

    /**
     * 获取视频帧数，暂不使用
     */
    public int getFrameRate(String framerate) {
        String[] split = framerate.split("/");
        int temp1 = Integer.parseInt(split[0]);
        int temp2 = Integer.parseInt(split[1]);

        return temp1 / temp2;
    }
}