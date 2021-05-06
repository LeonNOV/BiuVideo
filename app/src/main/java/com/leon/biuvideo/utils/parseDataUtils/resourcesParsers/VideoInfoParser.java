package com.leon.biuvideo.utils.parseDataUtils.resourcesParsers;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoInfo;
import com.leon.biuvideo.greendao.dao.DaoBaseUtils;
import com.leon.biuvideo.greendao.dao.DownloadHistory;
import com.leon.biuvideo.greendao.daoutils.DownloadHistoryUtils;
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
public class VideoInfoParser {

    /**
     * 解析view接口响应的数据
     *
     * @param bvid  视频bvid
     * @return  返回viewPage对象
     */
    public static VideoInfo parseData(String bvid, Context context) {
        Map<String, String> params = new HashMap<>(1);
        params.put("bvid", bvid);

        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.VIDEO_DETAIL_INFO, Headers.of(HttpUtils.getAPIRequestHeader()), params);
        JSONObject dataObject = responseObject.getJSONObject("data");

        if (dataObject != null) {
            VideoInfo videoInfo = new VideoInfo();

            DownloadHistoryUtils downloadHistoryUtils = new DownloadHistoryUtils(context);
            DaoBaseUtils<DownloadHistory> downloadHistoryDaoUtils = downloadHistoryUtils.getDownloadHistoryDaoUtils();

            videoInfo.bvid = dataObject.getString("bvid");
            videoInfo.aid = dataObject.getString("aid");
            videoInfo.title = dataObject.getString("title");
            videoInfo.videos = dataObject.getIntValue("videos");
            videoInfo.isMultiAnthology = videoInfo.videos > 0;
            videoInfo.tagId = dataObject.getIntValue("tid");
            videoInfo.tagName = dataObject.getString("tname");
            videoInfo.cover = dataObject.getString("pic");
            videoInfo.pubTime = dataObject.getLong("pubdate");
            videoInfo.desc = dataObject.getString("desc");

            JSONObject owner = dataObject.getJSONObject("owner");
            videoInfo.userInfo = new VideoInfo.UserInfo();
            videoInfo.userInfo.userMid = owner.getString("mid");
            videoInfo.userInfo.userName = owner.getString("name");
            videoInfo.userInfo.userFace = owner.getString("face");

            JSONObject stat = dataObject.getJSONObject("stat");
            videoInfo.videoStat = new VideoInfo.VideoStat();
            videoInfo.videoStat.view = stat.getIntValue("view");
            videoInfo.videoStat.danmaku = stat.getIntValue("danmaku");
            videoInfo.videoStat.comment = stat.getIntValue("replay");
            videoInfo.videoStat.favorite = stat.getIntValue("favorite");
            videoInfo.videoStat.like = stat.getIntValue("like");
            videoInfo.videoStat.coin = stat.getIntValue("coin");
            videoInfo.videoStat.share = stat.getIntValue("share");

            JSONArray pages = dataObject.getJSONArray("pages");
            videoInfo.videoAnthologyList = new ArrayList<>(pages.size());
            for (Object object : pages) {
                VideoInfo.VideoAnthology videoAnthology = new VideoInfo.VideoAnthology();
                JSONObject jsonObject = (JSONObject) object;

                videoAnthology.mainId = bvid;
                videoAnthology.cid = jsonObject.getString("cid");



                videoAnthology.part = jsonObject.getString("part");
                videoAnthology.duration = ValueUtils.lengthGenerate(jsonObject.getIntValue("duration"));
                videoAnthology.cover = videoInfo.cover;
                videoInfo.videoAnthologyList.add(videoAnthology);
            }

            return videoInfo;
        }

        return null;
    }
}
