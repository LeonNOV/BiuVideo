package com.leon.biuvideo.utils.parseUtils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.play.Media;
import com.leon.biuvideo.beans.play.Play;

import java.util.ArrayList;
import java.util.List;

public class MediaParseUtils {
    private static final String TAG = "LeonLogCat-red";

    /**
     * 解析playUrl接口
     *
     * @param response  响应体
     * @return  返回解析结果
     */
    public static Play parseMedia(String response) {
        try {
            Play play = new Play();
            play.accept_description = new ArrayList<>();

            JSONObject jsonObject = JSON.parseObject(response);

            JSONObject data = jsonObject.getJSONObject("data");

            //获取清晰度
            JSONArray accept_description = data.getJSONArray("accept_description");
            for (Object o : accept_description) {
                play.accept_description.add(o.toString());
            }

            //删除超清4K
            play.accept_description.remove("超清 4K");

            //删除1080P+
            play.accept_description.remove("高清 1080P+");

            JSONObject dash = data.getJSONObject("dash");

            //获取video
            play.videos = new ArrayList<>();
            JSONArray video = dash.getJSONArray("video");
            List<Media> videos = parseVideo(video);
            play.videos = videos;

            //获取audios
            play.audios = new ArrayList<>();
            JSONArray audio = dash.getJSONArray("audio");
            List<Media> audios = parseAudio(audio);
            play.audios = audios;


            return play;
        } catch (NullPointerException e) {
            Log.e(TAG, "parseView: 数据解析出错");
        }

        return null;
    }

    /**
     * 解析videoJSONArray
     *
     * @param video json数组
     * @return  返回解析结果
     */
    private static List<Media> parseVideo(JSONArray video) {
        return getMedia(video);
    }

    /**
     * 解析audioJSONArray
     *
     * @param audio json数组
     * @return  返回解析结果
     */
    private static List<Media> parseAudio(JSONArray audio) {
        return getMedia(audio);
    }

    private static List<Media> getMedia(JSONArray video) {
        List<Media> videos = new ArrayList<>();

        //由于响应体中有两个id相同的视频，所以只获取两个中的第一个（索引0），即只获取索引为偶数的
        for (int i = 0, videoSize = video.size(); i < videoSize; i += 2) {
            Object o = video.get(i);
            JSONObject videoObject = (JSONObject) o;

            Media media = new Media();
            media.backupUrl = new ArrayList<>();

            //获取视频链接
            String baseUrl = videoObject.getString("baseUrl");
            media.baseUrl = baseUrl;

            //获取备用视频链接
            JSONArray backupUrl = videoObject.getJSONArray("backupUrl");
            for (Object value : backupUrl) {
                media.backupUrl.add(String.valueOf(value));
            }

            //获取视频编解码器
//                Object codecs = videoObject.get("codecs");

            //获取视频帧率
//                Object frameRate = videoObject.get("frameRate");

            videos.add(media);
        }

        return videos;
    }
}
