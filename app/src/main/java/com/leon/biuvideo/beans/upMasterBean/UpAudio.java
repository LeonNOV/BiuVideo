package com.leon.biuvideo.beans.upMasterBean;

import java.io.Serializable;

/**
 * 音频基本信息
 */
public class UpAudio implements Serializable {
    public long sid;//音频id
    public String cover;
    public int duration;//音频时长（秒）
    public String title;
    public long play;
    public long ctime;//发布时间（毫秒）
}
