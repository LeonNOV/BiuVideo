package com.leon.biuvideo.beans.upMasterBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 相簿
 */
public class Picture implements Serializable {
    public long docId;
    public long posterUid;//用户ID
//    public String title;
    public String description;
    public int count;//图片总数量
    public long view;
    public long like;
    public long ctime;

    //map只存储图片url和图片size
    public List<String> pictures;
}
