package com.leon.biuvideo.beans.resourcesBeans;

import com.leon.biuvideo.values.Role;

import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/4/12
 * @Desc 相簿数据
 */
public class PictureDetail {
    public String view;
    public String comment;
    public String like;

    public String pubTime;

    public String userName;
    public String userMid;
    public String userFace;
    public boolean isVip;
    public Role role;

    public String title;
    public String desc;

    public boolean isLiked;
    public boolean isFollow;

    /**
     * 索引， 图片链接，图片大小
     */
    public Map<Integer, String[]> pictures;
    public int picturesCount;
}
