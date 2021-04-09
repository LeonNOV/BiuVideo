package com.leon.biuvideo.beans.upMasterBean;

import com.leon.biuvideo.values.Role;

import java.io.Serializable;

/**
 * @Author Leon
 * @Time 2020/10/21
 * @Desc B站用户信息数据
 */
public class BiliUserInfo {
    public String userMid;
    public String userName;
    public String userFace;

    public int level;
    public String sign;
    public String gender;
    public String topPhoto;

    /**
     * true:当前用户已关注，false：未关注
     */
    public boolean attentionState;

    public Role role;
    public String verifyDesc;

    public boolean isVip;
    public String vipLabel;
}
