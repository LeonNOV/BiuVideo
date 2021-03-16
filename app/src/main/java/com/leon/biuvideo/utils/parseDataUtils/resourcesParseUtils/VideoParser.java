package com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.upMasterBean.Video;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Headers;

/**
 * 视频接口解析
 */
public class VideoParser {

    /**
     * 视频接口解析
     *
     * @param mid   up主id
     * @param pageNum   页码，从1开始
     * @return  返回UpVideo类型集合
     */
    public List<Video> parseVideo(long mid, int pageNum) {
        Map<String, String> params = new HashMap<>(6);
        params.put("mid", String.valueOf(mid));
        params.put("ps", String.valueOf(30));
        params.put("pn", String.valueOf(pageNum));
        params.put("order", "pubdate");
        params.put("tid", "0");
        params.put("jsonp", "jsonp");

        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.videos, Headers.of(HttpUtils.getAPIRequestHeader()), params);
        JSONObject dataObject = responseObject.getJSONObject("data");

        if (dataObject != null) {
            JSONObject listObject = dataObject.getJSONObject("list");
            JSONArray vlistArray = listObject.getJSONArray("vlist");

            List<Video> videos = new ArrayList<>();
            for (Object o : vlistArray) {
                Video video = new Video();

                JSONObject videoObject = (JSONObject) o;

                //获取视频作者mid
                video.mid = videoObject.getLongValue("mid");

                //获取作者名称
                video.author = videoObject.getString("author");

                //获取封面地址
                video.cover = "http://" + videoObject.getString("pic");

                //获取视频bvid
                video.bvid = videoObject.getString("bvid");

                //获取视频aid
                video.aid = videoObject.getLong("aid");

                //获取视频播放量
                //可能会出现play的数值为'--'的情况
                if (Objects.equals(videoObject.get("play"), "--")) {
                    video.play = 0;
                } else {
                    video.play = videoObject.getInteger("play");
                }

                //获取视频上传日期
                video.create = videoObject.getLongValue("created");

                //获取是否为合作视频
                video.isUnionVideo = videoObject.getIntValue("is_union_video");

                //获取视频长度
                video.length = videoObject.getString("length");

                //获取视频标题
                video.title = videoObject.getString("title");

                //获取视频说明
                video.description = videoObject.getString("description");

                videos.add(video);
            }

            return videos;
        }

        Log.e(Fuck.red, "search接口数据获取失败", new NullPointerException("search接口数据获取失败"));
        return null;
    }

    /**
     * 获取视频总数
     *
     * @param mid   用户ID
     * @return  返回视频总数
     */
    public int getVideoTotal(long mid) {
        Map<String, String> params = new HashMap<>(6);
        params.put("mid", String.valueOf(mid));
        params.put("ps", "30");
        params.put("pn", "1");
        params.put("order", "pubdate");
        params.put("tid", "0");
        params.put("jsonp", "jsonp");

        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.videos, Headers.of(HttpUtils.getAPIRequestHeader()), params);
        JSONObject dataObject = responseObject.getJSONObject("data");
        JSONObject page = dataObject.getJSONObject("page");

        return page.getIntValue("count");
    }
}
