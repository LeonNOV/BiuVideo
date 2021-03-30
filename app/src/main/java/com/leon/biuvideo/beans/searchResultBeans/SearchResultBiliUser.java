package com.leon.biuvideo.beans.searchResultBeans;

import com.leon.biuvideo.values.Role;

/**
 * @Author Leon
 * @Time 2021/3/30
 * @Desc 用户搜索结果数据
 */
public class SearchResultBiliUser {
    public String mid;

    public String userName;
    public String userSign;
    public String userFace;

    public Role role;

    /**
     * 关系状态；0：未关注，2：已关注，6：已互粉
     */
    public int userStatus;
    public boolean isVip;
}
