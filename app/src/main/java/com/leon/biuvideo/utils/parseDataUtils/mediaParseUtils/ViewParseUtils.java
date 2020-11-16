package com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.videoBean.view.SingleVideoInfo;
import com.leon.biuvideo.beans.videoBean.view.UpInfo;
import com.leon.biuvideo.beans.videoBean.view.VideoInfo;
import com.leon.biuvideo.beans.videoBean.view.ViewPage;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * view接口解析工具
 */
public class ViewParseUtils {

    /**
     * 解析view接口响应的数据
     *
     * @param bvid  视频bvid
     * @return  返回viewPage对象
     */
    public static ViewPage parseView(String bvid) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("bvid", bvid);

            HttpUtils httpUtils = new HttpUtils(Paths.view, params);
            String response = httpUtils.getData();

            JSONObject jsonObject = JSON.parseObject(response);
            JSONObject dataObject = jsonObject.getJSONObject("data");

            if (dataObject != null) {
                ViewPage viewPage = new ViewPage();

                //视频bvid
                viewPage.bvid = dataObject.getString("bvid");

                //视频aid
                viewPage.aid = dataObject.getLong("aid");

                //选集个数
                viewPage.videos = dataObject.getIntValue("videos");

                //视频分类id
                viewPage.tid = dataObject.getIntValue("tid");

                //分类名称
                viewPage.tname = dataObject.getString("tname");

                //视频封面
                viewPage.coverUrl = dataObject.getString("pic");

                //视频标题
                viewPage.title = dataObject.getString("title");

                //视频上传日期（秒）
                viewPage.upTime = dataObject.getLong("pubdate");

                //视频说明
                viewPage.desc = dataObject.getString("desc");

                //获取up主信息
                viewPage.upInfo = parseUpInfo(dataObject.getJSONObject("owner"));

                //获取视频信息
                viewPage.videoInfo = parseVideoInfo(dataObject.getJSONObject("stat"));

                //获取选集信息
                viewPage.singleVideoInfoList = parseSingleVideoInfo(dataObject.getJSONArray("pages"));

                return viewPage;
            }
        } catch (NullPointerException e) {
            Log.e(Fuck.red, "parseView: 数据解析出错");
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
            singleVideoInfo.cid = jsonObject.getLong("cid");

            //选集索引
            singleVideoInfo.page = jsonObject.getIntValue("page");

            //选集标题
            singleVideoInfo.part = jsonObject.getString("part");

            //视频长度
            singleVideoInfo.duration = jsonObject.getIntValue("duration");

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
        videoInfo.view = stat.getIntValue("view");

        //弹幕数
        videoInfo.danmaku = stat.getIntValue("danmaku");

        //评论数
        videoInfo.reply = stat.getIntValue("replay");

        //收藏数
        videoInfo.favorite = stat.getIntValue("favorite");

        //点赞数
        videoInfo.like = stat.getIntValue("like");

        //投币数
        videoInfo.coin = stat.getIntValue("coin");

        //分享数
        videoInfo.share = stat.getIntValue("share");

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
        upInfo.mid = owner.getLong("mid");

        //up主昵称
        upInfo.name = owner.getString("name");

        //up主头像
        upInfo.faceUrl = owner.getString("face");

        return upInfo;
    }
}
