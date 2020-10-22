package com.leon.biuvideo.utils.resourcesParseUtils;


import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.upMasterBean.UpVideo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.LogTip;
import com.leon.biuvideo.utils.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 视频接口解析
 */
public class UpVideoParseUtils {

    /**
     * 视频接口解析
     *
     * @param mid   up主id
     * @param pageNum   页码，从1开始
     * @return  返回UpVideo类型集合
     */
    public static List<UpVideo> parseVideo(long mid, int pageNum) {
        Map<String, Object> values = new HashMap<>();
        values.put("mid", mid);
        values.put("ps", 30);
        values.put("pn", pageNum);
        values.put("order", "pubdate");
        values.put("tid", 0);
        values.put("jsonp", "jsonp");

        String response = HttpUtils.GETByParam(Paths.videos, values);

        JSONObject jsonObject = JSON.parseObject(response);

        JSONObject dataObject = jsonObject.getJSONObject("data");
        if (dataObject != null) {
            JSONObject listObject = dataObject.getJSONObject("list");
            JSONArray vlistArray = listObject.getJSONArray("vlist");

            List<UpVideo> upVideos  = new ArrayList<>();
            for (Object o : vlistArray) {
                UpVideo upVideo = new UpVideo();

                JSONObject videoObject = (JSONObject) o;

                //获取视频作者mid
                Long vmid = videoObject.getLong("mid");
                upVideo.mid = vmid;

                //获取作者名称
                String author = videoObject.getString("author");
                upVideo.author = author;

                //获取封面地址
                String cover = videoObject.getString("pic");
                upVideo.cover = cover;

                //获取视频bvid
                String bvid = videoObject.getString("bvid");
                upVideo.bvid = bvid;

                //获取视频aid
                Long aid = videoObject.getLong("aid");
                upVideo.aid = aid;

                //获取视频播放量
                Long play = videoObject.getLong("play");
                upVideo.play = play;

                //获取视频上传日期
                Long created = videoObject.getLong("created");
                upVideo.create = created;

                //获取是否为合作视频
                int isUnionVideo = videoObject.getIntValue("is_union_video");
                upVideo.isUnionVideo = isUnionVideo;

                //获取视频长度
                String length = videoObject.getString("length");
                upVideo.length = length;

                //获取视频标题
                String title = videoObject.getString("title");
                upVideo.title = title;

                //获取视频说明
                String description = videoObject.getString("description");
                upVideo.description = description;

                upVideos.add(upVideo);
            }

            return upVideos;
        }

        Log.e(LogTip.red, "search接口数据获取失败", new NullPointerException("search接口数据获取失败"));
        return null;
    }
}
