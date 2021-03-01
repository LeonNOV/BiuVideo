package com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.videoBean.play.Media;
import com.leon.biuvideo.beans.videoBean.play.Play;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;
import com.leon.biuvideo.values.Qualitys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

public class MediaParser {
    private final Map<String, String> requestHeader;

    public MediaParser(Context context) {
        this.requestHeader = ParserUtils.getInterfaceRequestHeader(context);
    }

    /**
     * 解析playUrl接口
     *
     * @param cid   cid
     * @return  返回选集视频信息
     */
    public Play parseMedia(String bvid, long cid, boolean isBangumi) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("bvid", String.valueOf(bvid));
            params.put("cid", String.valueOf(cid));
            params.put("qn", "0");
            params.put("otype", "json");
            params.put("fourk", "1");
            params.put("fnver", "0");
            params.put("fnval", "80");

            JSONObject responseObject = HttpUtils.getResponse(isBangumi ? BiliBiliAPIs.playUrlForBangumi : BiliBiliAPIs.playUrl, Headers.of(requestHeader), params);

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
     * @param jsonArray json数组
     * @return  返回解析结果
     */
    private Map<Integer, Media> parseJSONArray(JSONArray jsonArray) {
        Map<Integer, Media> mediaMap = new LinkedHashMap<>();

        for (Object o : jsonArray) {
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
