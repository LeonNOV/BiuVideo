package com.leon.biuvideo.beans.view;

import java.io.Serializable;

//up主信息
public class UpInfo implements Serializable {
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
