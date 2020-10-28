package com.leon.biuvideo.utils.resourcesParseUtils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.musicBeans.MusicInfo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.LogTip;
import com.leon.biuvideo.utils.Paths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicParseUtils {
    /**
     * musicInfo接口数据的解析
     *
     * @param sid   musicID
     * @return  返回MusicInfo对象
     */
    public static MusicInfo parseMusic(long sid) {
        Map<String, Object> values = new HashMap<>();
        values.put("sid", sid);

        String response = HttpUtils.GETByParam(Paths.musicInfo, values);

        JSONObject jsonObject = JSON.parseObject(response);

        JSONObject dataObject = jsonObject.getJSONObject("data");

        if (dataObject != null) {
            MusicInfo musicInfo = new MusicInfo();

            //放入当前的sid
            musicInfo.sid = sid;

            //获取发布者id
            Long uid = dataObject.getLong("uid");
            musicInfo.uid = uid;

            //获取发布者昵称
            String uname = dataObject.getString("uname");
            musicInfo.uname = uname;

            //获取作者,可能存在多个作者的情况所以需要进行分割
            String authorStr = dataObject.getString("author");
            List<String> authors = new ArrayList<>();
            if (authorStr.contains(" · ")) {
                authors.addAll(Arrays.asList(authorStr.split(" · ")));
            } else {
                authors.add(authorStr);
            }

            musicInfo.authors = authors;

            //获取歌曲标题
            String title = dataObject.getString("title");
            musicInfo.title = title;

            //获取歌曲封面
            String cover = dataObject.getString("cover");
            musicInfo.cover = cover;

            //获取歌曲简介
            String intro = dataObject.getString("intro");
            musicInfo.intro = intro;

            //获取歌词链接
            String lyric = dataObject.getString("lyric");
            musicInfo.lyric = lyric;

            //获取歌曲长度（秒）
            int duration = dataObject.getIntValue("duration");
            musicInfo.duration = duration;

            //获取发布时间
            Long passtime = dataObject.getLong("passtime");
            musicInfo.passtime = passtime;

            //获取对应视频的aid
            Long aid = dataObject.getLong("aid");
            musicInfo.aid = aid;

            //获取对应视频bvid
            String bvid = dataObject.getString("bvid");
            musicInfo.bvid = bvid;

            //获取对应视频的cid
            Long cid = dataObject.getLong("cid");
            musicInfo.cid = cid;

            //获取投币数
            int coinNum = dataObject.getIntValue("coin_num");
            musicInfo.coinNum = coinNum;

            //statisticJSON对象
            JSONObject statistic = dataObject.getJSONObject("statistic");

            //获取播放量
            int play = statistic.getIntValue("play");
            musicInfo.play = play;

            //获取收藏数
            int collect = statistic.getIntValue("collect");
            musicInfo.collect = collect;

            //获取评论数
            int comment = statistic.getIntValue("comment");
            musicInfo.comment = comment;

            //获取分享数
            int share = statistic.getIntValue("share");
            musicInfo.share = share;

            return musicInfo;
        }

        Log.e(LogTip.red, "musicInfo接口数据获取失败", new NullPointerException("musicInfo接口数据获取失败"));
        return null;
    }
}
