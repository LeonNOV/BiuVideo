package com.leon.biuvideo.beans.userBeans;

import com.leon.biuvideo.values.Role;

/**
 * @Author Leon
 * @Time 2020/12/8
 * @Desc 用户基本信息
 */
public class UserInfo {
    public String userName;
    public String userFace;
    public long mid;

    /**
     * 现经验值
     */
    public int currentExp;

    /**
     * 现等级
     */
    public int currentLevel;

    /**
     * 总需经验值
     */
    public int totalExp;

    /**
     * 硬币数
     */
    public double coins;

    /**
     * 节操值
     */
    public int moral;

    /**
     * B币数
     */
    public int bCoinBalance;

    /**
     * 是否为大会员
     */
    public boolean isVip;

    /**
     * vip种类
     */
    public String vipLabel;

    /**
     * vip到期时间
     */
    public String vipDueDate;

    /**
     * 认证类型
     */
    public Role role;

    /**
     * 认证说明
     */
    public String verifyDesc;

    /**
     * 顶部横幅
     */
    public String banner;

    /**
     * 关注数
     */
    public int follows;

    /**
     * 粉丝数
     */
    public int fans;

    /**
     * 获赞数
     */
    public int likes;

    /**
     * 动态数
     */
    public int dynamics;
    public String sex;
    public String sign;
}
