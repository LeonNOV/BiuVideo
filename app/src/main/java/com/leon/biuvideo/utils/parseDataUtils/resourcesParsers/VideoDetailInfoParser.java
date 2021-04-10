package com.leon.biuvideo.utils.parseDataUtils.resourcesParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.mediaBeans.videoBeans.VideoDetailInfo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2020/10/18
 * @Desc 视频详细信息解析类
 */
public class VideoDetailInfoParser {

    /**
     * 解析view接口响应的数据
     *
     * @param bvid  视频bvid
     * @return  返回viewPage对象
     */
    public static VideoDetailInfo parseData(String bvid) {
        Map<String, String> params = new HashMap<>(1);
        params.put("bvid", bvid);

        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.VIDEO_DETAIL_INFO, Headers.of(HttpUtils.getAPIRequestHeader()), params);
        JSONObject dataObject = responseObject.getJSONObject("data");

        if (dataObject != null) {
            VideoDetailInfo videoDetailInfo = new VideoDetailInfo();

            videoDetailInfo.bvid = dataObject.getString("bvid");
            videoDetailInfo.aid = dataObject.getString("aid");
            videoDetailInfo.title = dataObject.getString("title");
            videoDetailInfo.videos = dataObject.getIntValue("videos");
            videoDetailInfo.tagId = dataObject.getIntValue("tid");
            videoDetailInfo.tagName = dataObject.getString("tname");
            videoDetailInfo.cover = dataObject.getString("pic");
            videoDetailInfo.pubTime = dataObject.getLong("pubdate");
            videoDetailInfo.desc = dataObject.getString("desc");

            JSONObject owner = dataObject.getJSONObject("owner");
            videoDetailInfo.userInfo = new VideoDetailInfo.UserInfo();
            videoDetailInfo.userInfo.userMid = owner.getString("mid");
            videoDetailInfo.userInfo.userName = owner.getString("name");
            videoDetailInfo.userInfo.userFace = owner.getString("face");

            JSONObject stat = dataObject.getJSONObject("stat");
            videoDetailInfo.videoInfo = new VideoDetailInfo.VideoInfo();
            videoDetailInfo.videoInfo.view = stat.getIntValue("view");
            videoDetailInfo.videoInfo.danmaku = stat.getIntValue("danmaku");
            videoDetailInfo.videoInfo.comment = stat.getIntValue("replay");
            videoDetailInfo.videoInfo.favorite = stat.getIntValue("favorite");
            videoDetailInfo.videoInfo.like = stat.getIntValue("like");
            videoDetailInfo.videoInfo.coin = stat.getIntValue("coin");
            videoDetailInfo.videoInfo.share = stat.getIntValue("share");

            JSONArray pages = dataObject.getJSONArray("pages");
            videoDetailInfo.anthologyInfoList = new ArrayList<>(pages.size());
            for (Object object : pages) {
                VideoDetailInfo.AnthologyInfo anthologyInfo = new VideoDetailInfo.AnthologyInfo();
                JSONObject jsonObject = (JSONObject) object;

                anthologyInfo.mainId = bvid;
                anthologyInfo.cid = jsonObject.getString("cid");
                anthologyInfo.part = jsonObject.getString("part");
                anthologyInfo.duration = ValueUtils.lengthGenerate(jsonObject.getIntValue("duration"));

                videoDetailInfo.anthologyInfoList.add(anthologyInfo);
            }

            return videoDetailInfo;
        }

        return null;
    }
}
