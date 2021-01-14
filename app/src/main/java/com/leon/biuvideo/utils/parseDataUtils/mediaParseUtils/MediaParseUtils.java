package com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.videoBean.play.Media;
import com.leon.biuvideo.beans.videoBean.play.Play;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.values.Paths;
import com.leon.biuvideo.values.Qualitys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MediaParseUtils {

    /**
     * 解析playUrl接口
     *
     * @param cid   cid
     * @return  返回选集视频信息
     */
    public static Play parseMedia(long cid, boolean isBangumi) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("cid", String.valueOf(cid));
            params.put("qn", "0");
            params.put("otype", "json");
            params.put("fourk", "1");
            params.put("fnver", "0");
            params.put("fnval", "80");

            JSONObject responseObject = HttpUtils.getResponse(isBangumi ? Paths.playUrlForBangumi : Paths.playUrl, params);

            JSONObject data = responseObject.getJSONObject(isBangumi ? "result" : "data");

            if (data != null) {
                Play play = new Play();

                JSONObject dash = data.getJSONObject("dash");

                //获取videos
                JSONArray video = dash.getJSONArray("video");
                play.videos = parseJSONArray(video);

                //获取audios
                JSONArray audio = dash.getJSONArray("audio");
                play.audios = parseJSONArray(audio);

                return play;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解析videoJSONArray
     *
     * @param videoJsonArray json数组
     * @return  返回解析结果
     */
    private static Map<Integer, Media> parseJSONArray(JSONArray videoJsonArray) {
        Map<Integer, Media> mediaMap = new LinkedHashMap<>();

        for (Object o : videoJsonArray) {
            JSONObject jsonObject = (JSONObject) o;

            Media media = new Media();

            int id = jsonObject.getIntValue("id");

            // 根据ID获取清晰度字符串
            media.quality = Qualitys.getQualityStr(id, jsonObject.getString("frameRate"));

            media.baseUrl = jsonObject.getString("baseUrl");

            List<String> list = new ArrayList<>();
            for (Object backupUrl : jsonObject.getJSONArray("backupUrl")) {
                list.add(backupUrl.toString());
            }

            media.backupUrl = list;
            mediaMap.put(id, media);
        }

        return mediaMap;
    }
}
