package com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.videoBean.play.Media;
import com.leon.biuvideo.beans.videoBean.play.Play;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.values.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediaParseUtils {
    private static final String TAG = "LeonLogCat-red";

    /**
     * 解析playUrl接口
     *
     * @param bvid  bvid
     * @param aid   aid
     * @param cid   cid
     * @return  返回选集视频信息
     */
    public static Play parseMedia(String bvid, long aid, long cid) {
        try {

            Map<String, String> params = new HashMap<>();
            params.put("avid", String.valueOf(aid));
            params.put("bvid", String.valueOf(bvid));
            params.put("cid", String.valueOf(cid));
            params.put("qn", "0");
            params.put("otype", "json");
            params.put("fourk", "1");
            params.put("fnver", "0");
            params.put("fnval", "80");

            JSONObject responseObject = HttpUtils.getResponse(Paths.playUrl, params);
            JSONObject data = responseObject.getJSONObject("data");

            if (data != null) {
                Play play = new Play();
                play.videoQualitys = new ArrayList<>();

                //获取清晰度
                JSONArray accept_description = data.getJSONArray("accept_description");
                for (Object o : accept_description) {
                    play.videoQualitys.add(o.toString());
                }

                //删除有大会员限制的视频清晰度
                play.videoQualitys.remove("超清 4K");
                play.videoQualitys.remove("高清 1080P+");
                play.videoQualitys.remove("高清 1080P60");

                JSONObject dash = data.getJSONObject("dash");

                //获取videos
                play.videos = new ArrayList<>();
                JSONArray video = dash.getJSONArray("video");
                play.videos = parseVideo(video);

                //获取audios
                play.audios = new ArrayList<>();
                JSONArray audio = dash.getJSONArray("audio");
                play.audios = parseAudio(audio);

                return play;
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "parseView: 数据解析出错");
        }

        return null;
    }

    /**
     * 解析videoJSONArray
     *
     * @param videoJsonArray json数组
     * @return  返回解析结果
     */
    private static List<Media> parseVideo(JSONArray videoJsonArray) {
        List<Media> videos = new ArrayList<>();

        /*
         * 由于响应体中可能出现有两个id相同的视频，但两个video的编码格式不同
         * avc格式默认为第一个，没有avc格式的话，则仅有一个hev格式的video
         * 先根据上面的规则对videoJsonArray中的数据进行处理
         * */
        Map<Integer, JSONObject> videoMap = new HashMap<>();
        for (Object o : videoJsonArray) {
            JSONObject videoObject = (JSONObject) o;

            //获取id将id相同的放到一个数组内
            int id = videoObject.getIntValue("id");

            if (id <= 80) {
                if (!videoMap.containsKey(id)) {
                    videoMap.put(id, videoObject);
                }
            }
        }

        //获取media对象
        for (Map.Entry<Integer, JSONObject> entry : videoMap.entrySet()) {
            JSONObject jsonObject = entry.getValue();

            Media media = new Media();
            media.backupUrl = new ArrayList<>();

            //获取视频/音频链接
            media.baseUrl = jsonObject.getString("baseUrl");

            //获取备用视频/音频链接
            JSONArray backupUrl = jsonObject.getJSONArray("backupUrl");
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

    /**
     * 解析audioJSONArray
     *
     * @param audioJsonArray json数组
     * @return  返回解析结果
     */
    private static List<Media> parseAudio(JSONArray audioJsonArray) {
        List<Media> audios = new ArrayList<>();

        for (int i = 0, videoSize = audioJsonArray.size(); i < videoSize; i++) {
            Object o = audioJsonArray.get(i);
            JSONObject videoObject = (JSONObject) o;

            Media media = new Media();
            media.backupUrl = new ArrayList<>();

            //获取视频/音频链接
            media.baseUrl = videoObject.getString("baseUrl");

            //获取备用视频/音频链接
            JSONArray backupUrl = videoObject.getJSONArray("backupUrl");
            for (Object value : backupUrl) {
                media.backupUrl.add(String.valueOf(value));
            }

            //获取视频编解码器
//                Object codecs = videoObject.get("codecs");

            //获取视频帧率
//                Object frameRate = videoObject.get("frameRate");

            audios.add(media);
        }

        return audios;
    }
}
