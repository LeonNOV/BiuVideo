package com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.musicBeans.MusicInfo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.values.Paths;

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
        Map<String, String> params = new HashMap<>();
        params.put("sid", String.valueOf(sid));

        JSONObject responseObject = HttpUtils.getResponse(Paths.musicInfo, params);
        JSONObject dataObject = responseObject.getJSONObject("data");

        if (dataObject != null) {
            MusicInfo musicInfo = new MusicInfo();

            //放入当前的sid
            musicInfo.sid = sid;

            //获取发布者id
            musicInfo.uid = dataObject.getLong("uid");

            //获取发布者昵称
            musicInfo.uname = dataObject.getString("uname");

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
            musicInfo.title = dataObject.getString("title");

            //获取歌曲封面
            musicInfo.cover = dataObject.getString("cover");

            //获取歌曲简介
//            musicInfo.intro = dataObject.getString("intro");

            //获取歌词链接
//            musicInfo.lyric = dataObject.getString("lyric");

            //获取歌曲长度（秒）
            musicInfo.duration = dataObject.getIntValue("duration");

            //获取发布时间
//            musicInfo.passtime = dataObject.getLong("passtime");

            //获取对应视频的aid
            musicInfo.aid = dataObject.getLong("aid");

            //获取对应视频bvid
            musicInfo.bvid = dataObject.getString("bvid");

            //获取对应视频的cid
            musicInfo.cid = dataObject.getLong("cid");

            //获取投币数
//            musicInfo.coinNum = dataObject.getIntValue("coin_num");

            //statisticJSON对象
//            JSONObject statistic = dataObject.getJSONObject("statistic");

            //获取播放量
//            musicInfo.play = statistic.getIntValue("play");

            //获取收藏数
//            musicInfo.collect = statistic.getIntValue("collect");

            //获取评论数
//            musicInfo.comment = statistic.getIntValue("comment");

            //获取分享数
//            musicInfo.share = statistic.getIntValue("share");

            return musicInfo;
        }

        Log.e(Fuck.red, "musicInfo接口数据获取失败", new NullPointerException("musicInfo接口数据获取失败"));
        return null;
    }
}
