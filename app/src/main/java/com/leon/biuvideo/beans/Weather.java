package com.leon.biuvideo.beans;

import androidx.annotation.DrawableRes;

import java.io.Serializable;
import java.util.List;

public class Weather implements Serializable {
    public int status;  // 1：成功，0：失败
    public String infocode;

    public String province; // 省份
    public String city; // 城市
    public String adcode;  // 邮政编码

    public String weather;  // 天气（文字描述）
    public @DrawableRes int weatherIconId;
    public String temperature;  // 实时气温，单位：摄氏度
    public String humidity; // 空气湿度

    public String reporttime;    // 预报发布时间

    public List<Cast> casts;  // 预报数据list结构，元素cast,按顺序为当天、第二天、第三天的预报数据

    public static class Cast {
        public String date; // 日期
        public String week; // 星期几
        public String dayweather;   // 白天天气
        public String nightweather; // 晚上天气
        public String daytemp;  // 白天温度
        public String nighttemp;    // 晚上温度
        public String daywind;  // 白天风向
        public String nightwind;    // 晚上风向
        public String daypower; // 白天风力
        public String nightpower;   // 晚上风力
    }
}
