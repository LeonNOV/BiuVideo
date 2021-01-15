package com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.videoBean.view.AnthologyInfo;
import com.leon.biuvideo.beans.videoBean.view.UserInfo;
import com.leon.biuvideo.beans.videoBean.view.VideoInfo;
import com.leon.biuvideo.beans.videoBean.view.ViewPage;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.parseDataUtils.ParserUtils;
import com.leon.biuvideo.values.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * view接口解析工具
 */
public class ViewParser {
    private final Map<String, String> requestHeader;

    public ViewParser(Context context) {
        this.requestHeader = ParserUtils.getInterfaceRequestHeader(context);
    }

    /**
     * 解析view接口响应的数据
     *
     * @param bvid  视频bvid
     * @return  返回viewPage对象
     */
    public ViewPage parseView(String bvid) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("bvid", bvid);

            JSONObject responseObject = HttpUtils.getResponse(Paths.view, Headers.of(requestHeader), params);
            JSONObject dataObject = responseObject.getJSONObject("data");

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
                viewPage.userInfo = parseUpInfo(dataObject.getJSONObject("owner"));

                //获取视频信息
                viewPage.videoInfo = parseVideoInfo(dataObject.getJSONObject("stat"));

                //获取选集信息
                viewPage.anthologyInfoList = parseSingleVideoInfo(dataObject.getJSONArray("pages"));

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
    private List<AnthologyInfo> parseSingleVideoInfo(JSONArray pages) {
        List<AnthologyInfo> anthologyInfoList = new ArrayList<>();

        for (Object object : pages) {

            AnthologyInfo anthologyInfo = new AnthologyInfo();

            JSONObject jsonObject = (JSONObject) object;

            //选集cid
            anthologyInfo.cid = jsonObject.getLong("cid");

            //选集标题
            anthologyInfo.part = jsonObject.getString("part");

            //视频长度
            anthologyInfo.duration = jsonObject.getIntValue("duration");

            anthologyInfoList.add(anthologyInfo);
        }

        return anthologyInfoList;
    }

    /**+
     * 解析视频信息
     *
     * @param stat  json对象
     * @return  返回视频信息
     */
    private VideoInfo parseVideoInfo(JSONObject stat) {

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
    private UserInfo parseUpInfo(JSONObject owner) {
        UserInfo userInfo = new UserInfo();

        //up主mid（b站ID）
        userInfo.mid = owner.getLong("mid");

        //up主昵称
        userInfo.name = owner.getString("name");

        //up主头像
        userInfo.faceUrl = owner.getString("face");

        return userInfo;
    }
}
