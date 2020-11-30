package com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.upMasterBean.Audio;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 音频接口解析
 */
public class AudioParseUtils {
    /**
     * 音频接口解析
     *
     * @param mid   up主id
     * @param pageNum   页码，从1开始
     * @return  返回UpAudio类型集合
     */
    public static List<Audio> parseAudio(long mid, int pageNum) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", String.valueOf(mid));
        params.put("pn", String.valueOf(pageNum));
        params.put("ps", String.valueOf(30));
        params.put("order", "1");
        params.put("jsonp", "jsonp");

        JSONObject responseObject = HttpUtils.getResponse(Paths.music, params);
        JSONObject data = responseObject.getJSONObject("data");

        if (data != null) {
            List<Audio> audios = new ArrayList<>();

            JSONArray innerData = data.getJSONArray("data");

            if (innerData != null) {
                for (Object innerDatum : innerData) {
                    Audio audio = new Audio();

                    JSONObject datumObject = (JSONObject) innerDatum;

                    //获取音频id
                    audio.sid = datumObject.getLong("id");

                    //获取封面
                    audio.cover = datumObject.getString("cover");

                    //获取音频时长
                    audio.duration = datumObject.getIntValue("duration");

                    //获取标题
                    audio.title = datumObject.getString("title");

                    //获取播放量
                    audio.play = datumObject.getJSONObject("statistic").getLong("play");

                    //获取发布时间
                    audio.ctime = datumObject.getLong("ctime");

                    audios.add(audio);
                }
            }

            return audios;
        }

        Log.e(Fuck.red, "upper接口数据获取失败", new NullPointerException("upper接口数据获取失败"));
        return null;
    }

    /**
     * 获取歌曲总数
     *
     * @param mid   用户ID
     * @return  返回歌曲总数
     */
    public static int getAudioTotal(long mid) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", String.valueOf(mid));
        params.put("pn", "1");
        params.put("ps", "30");
        params.put("order", "1");
        params.put("jsonp", "jsonp");

        JSONObject responseObject = HttpUtils.getResponse(Paths.music, params);
        JSONObject data = responseObject.getJSONObject("data");

        return data.getIntValue("totalSize");
    }
}
