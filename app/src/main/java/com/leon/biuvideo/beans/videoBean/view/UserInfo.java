package com.leon.biuvideo.beans.videoBean.view;

import java.io.Serializable;

/**
 * 用户信息
 */
public class UserInfo implements Serializable {
    public long mid;
    public String name;
    public String faceUrl;

    @Override
    public String toString() {
        return "UpInfo{" +
                "mid=" + mid +
                ", name='" + name + '\'' +
                ", faceUrl='" + faceUrl + '\'' +
                '}';
    }
}
