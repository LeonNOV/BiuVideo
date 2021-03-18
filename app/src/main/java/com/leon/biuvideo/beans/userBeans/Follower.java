package com.leon.biuvideo.beans.userBeans;

import com.leon.biuvideo.values.Role;

/**
 * @Author Leon
 * @Time 2021/3/18
 * @Desc 粉丝数据
 */
public class Follower {
    public long followerMid;
    public String userName;
    public String userFace;
    public String sign;
    public Role role;

    /**
     * 关系状态；0：未关注，2：已关注，6：已互粉
     */
    public int userStatus;
    public boolean vipStatus;
}
