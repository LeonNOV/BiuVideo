package com.leon.biuvideo.beans.videoBean.play;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取视频及音频url所用的实体类
 */
public class Play implements Serializable {
    // key为清晰度ID，可转到Qualitys类查看所有清晰度
    public Map<Integer, Media> videos;
    public Map<Integer, Media> audios;

    public List<Map.Entry<Integer, Media>> videoEntries () {
        return new ArrayList<>(videos.entrySet());
    }

    public List<Map.Entry<Integer, Media>> audioEntries () {
        return new ArrayList<>(audios.entrySet());
    }
}