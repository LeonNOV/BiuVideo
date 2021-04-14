package com.leon.biuvideo.utils.parseDataUtils.resourcesParsers;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.resourcesBeans.Audio;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2021/4/14
 * @Desc 音频数据解析类
 */
public class AudioParser {
    public static Audio parseData (String sid) {
        Map<String, String> params = new HashMap<>(1);
        params.put("sid", sid);

        JSONObject audioInfoResponse = HttpUtils.getResponse(BiliBiliAPIs.AUDIO_INFO, Headers.of(HttpUtils.getAPIRequestHeader()), params);
        if (audioInfoResponse.getIntValue("code") == 0) {
            JSONObject data = audioInfoResponse.getJSONObject("data");
            Audio audio = new Audio();

            audio.title = data.getString("title");
            audio.cover = data.getString("cover");
            audio.duration = data.getIntValue("duration");

            String bvid = data.getString("bvid");
            audio.bvid = "".equals(bvid) ? null : bvid;

            // 获取音频数据流
            JSONObject audioStreamUrlResponse = HttpUtils.getResponse(BiliBiliAPIs.AUDIO_STREAM_URL, params);
            if (audioStreamUrlResponse.getIntValue("code") == 0) {
                audio.streamUrl = (String) audioStreamUrlResponse.getJSONObject("data").getJSONArray("cdns").get(0);

                return audio;
            }
        }
        return null;
    }
}
