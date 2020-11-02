package com.leon.biuvideo.beans.videoBean.play;

import java.io.Serializable;
import java.util.List;

/**
 * 获取视频及音频url所用的实体类
 */
public class Play implements Serializable {
    public List<String> accept_description;
    public List<Media> videos;
    public List<Media> audios;
}