package com.leon.biuvideo.utils.mediaParseUtils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.videoBean.view.SingleVideoInfo;
import com.leon.biuvideo.beans.videoBean.view.UpInfo;
import com.leon.biuvideo.beans.videoBean.view.VideoInfo;
import com.leon.biuvideo.beans.videoBean.view.ViewPage;

import java.util.ArrayList;
import java.util.List;

/**
 * view接口解析工具
 */
public class ViewParseUtils {

    private static final String TAG = "LeonLogCat-red";

    /**
     * 解析view接口响应的数据
     *
     * @param response
     * @return
     */
    public static ViewPage parseView(String response) {
        try {
            JSONObject jsonObject = JSON.parseObject(response);
            JSONObject dataObject = jsonObject.getJSONObject("data");

            ViewPage viewPage = new ViewPage();

            //视频bvid
            String bvid = dataObject.getString("bvid");
            viewPage.bvid = bvid;

            //视频aid
            long aid = dataObject.getLong("aid");
            viewPage.aid = aid;

            //选集个数
            int videos = dataObject.getIntValue("videos");
            viewPage.videos = videos;

            //视频分类id
            int tid = dataObject.getIntValue("tid");
            viewPage.tid = tid;

            //分类名称
            String tname = dataObject.getString("tname");
            viewPage.tname = tname;

            //视频封面
            String pic = dataObject.getString("pic");
            viewPage.coverUrl = pic;

            //视频标题
            String title = dataObject.getString("title");
            viewPage.title = title;

            //视频上传日期（秒）
            Long pubdate = dataObject.getLong("pubdate");
            viewPage.upTime = pubdate;

            //视频说明
            String desc = dataObject.getString("desc");
            viewPage.desc = desc;

            //获取up主信息
            UpInfo owner = parseUpInfo(dataObject.getJSONObject("owner"));
            viewPage.upInfo = owner;

            //获取视频信息
            VideoInfo videoInfo = parseVideoInfo(dataObject.getJSONObject("stat"));
            viewPage.videoInfo = videoInfo;

            //获取选集信息
            List<SingleVideoInfo> singleVideoInfoList = parseSingleVideoInfo(dataObject.getJSONArray("pages"));
            viewPage.singleVideoInfoList = singleVideoInfoList;

            return viewPage;
        } catch (NullPointerException e) {
            Log.e(TAG, "parseView: 数据解析出错");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解析选集视频信息
     *
     * @param pages json数组
     * @return  返回所有选集视频信息
     */
    private static List<SingleVideoInfo> parseSingleVideoInfo(JSONArray pages) {
        List<SingleVideoInfo> singleVideoInfoList = new ArrayList<>();

        for (Object object : pages) {

            SingleVideoInfo singleVideoInfo = new SingleVideoInfo();

            JSONObject jsonObject = (JSONObject) object;

            //选集cid
            Long cid = jsonObject.getLong("cid");
            singleVideoInfo.cid = cid;

            //选集索引
            int page = jsonObject.getIntValue("page");
            singleVideoInfo.page = page;

            //选集标题
            String part = jsonObject.getString("part");
            singleVideoInfo.part = part;

            singleVideoInfoList.add(singleVideoInfo);
        }

        return singleVideoInfoList;
    }

    /**+
     * 解析视频信息
     *
     * @param stat  json对象
     * @return  返回视频信息
     */
    private static VideoInfo parseVideoInfo(JSONObject stat) {

        VideoInfo videoInfo = new VideoInfo();

        //观看数
        int view = stat.getIntValue("view");
        videoInfo.view = view;

        //弹幕数
        int danmaku = stat.getIntValue("danmaku");
        videoInfo.danmaku = danmaku;

        //评论数
        int replay = stat.getIntValue("replay");
        videoInfo.reply = replay;

        //收藏数
        int favorite = stat.getIntValue("favorite");
        videoInfo.favorite = favorite;

        //点赞数
        int like = stat.getIntValue("like");
        videoInfo.like = like;

        //投币数
        int coin = stat.getIntValue("coin");
        videoInfo.coin = coin;

        //分享数
        int share = stat.getIntValue("share");
        videoInfo.share = share;

        return videoInfo;
    }

    /**
     * 解析up主信息
     *
     * @param owner json对象
     * @return  返回up主信息
     */
    private static UpInfo parseUpInfo(JSONObject owner) {
        UpInfo upInfo = new UpInfo();

        //up主mid（b站ID）
        Long mid = owner.getLong("mid");
        upInfo.mid = mid;

        //up主昵称
        String name = owner.getString("name");
        upInfo.name = name;

        //up主头像
        String face = owner.getString("face");
        upInfo.faceUrl = face;

        return upInfo;
    }
}
