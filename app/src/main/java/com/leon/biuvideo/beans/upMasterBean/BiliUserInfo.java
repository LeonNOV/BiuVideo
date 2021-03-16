package com.leon.biuvideo.beans.upMasterBean;

import com.leon.biuvideo.values.Role;

import java.io.Serializable;

/**
 * @Author Leon
 * @Time 2020/10/21
 * @Desc B站用户信息数据
 */
public class BiliUserInfo implements Serializable {
    public String name;
    public String face;
    public String sign;
    public String topPhoto;
    public Role role;
    public String verifyDesc;
}
