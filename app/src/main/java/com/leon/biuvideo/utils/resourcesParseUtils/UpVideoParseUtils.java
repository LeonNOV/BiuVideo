package com.leon.biuvideo.utils.resourcesParseUtils;


import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.upMasterBean.PageInfo;
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
    public static int count;

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
            //获取数据总数
            count = getPageInfo(dataObject);

            JSONObject listObject = dataObject.getJSONObject("list");
            JSONArray vlistArray = listObject.getJSONArray("vlist");

            List<UpVideo> upVideos  = new ArrayList<>();
            for (Object o : vlistArray) {
                UpVideo upVideo = new UpVideo();

                JSONObject videoObject = (JSONObject) o;

                //获取视频作者mid
                upVideo.mid = videoObject.getLong("mid");

                //获取作者名称
                upVideo.author = videoObject.getString("author");

                //获取封面地址
                upVideo.cover = videoObject.getString("pic");

                //获取视频bvid
                upVideo.bvid = videoObject.getString("bvid");

                //获取视频aid
                upVideo.aid = videoObject.getLong("aid");

                //获取视频播放量
                upVideo.play = videoObject.getLong("play");

                //获取视频上传日期
                upVideo.create = videoObject.getLong("created");

                //获取是否为合作视频
                upVideo.isUnionVideo = videoObject.getIntValue("is_union_video");

                //获取视频长度
                upVideo.length = videoObject.getString("length");

                //获取视频标题
                upVideo.title = videoObject.getString("title");

                //获取视频说明
                upVideo.description = videoObject.getString("description");

                upVideos.add(upVideo);
            }

            return upVideos;
        }

        Log.e(LogTip.red, "search接口数据获取失败", new NullPointerException("search接口数据获取失败"));
        return null;
    }

    //获取数据总数
    private static int getPageInfo(JSONObject dataObject) {
        JSONObject page = dataObject.getJSONObject("page");

        return page.getIntValue("count");
    }
}
