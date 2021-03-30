package com.leon.biuvideo.beans.searchResultBeans.bangumi;

import java.io.Serializable;

/**
 * 番剧选集实体类
 */
public class Ep implements Serializable {
    public long id;     // 选集ID
    public String title;    // 选集标题
    public String longTitle;    // 选集长标题
    public String badge;
    public boolean isVIP;    // 是否需要大会员
    public String url;  // 直达链

    public long aid;
    public long cid;
    public String cover;

}
