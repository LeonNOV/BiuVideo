package com.leon.biuvideo.utils.resourcesParseUtils;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.upMasterBean.UpAudio;
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
public class UpAudioParseUtils {
    public static int count;

    /**
     * 音频接口解析
     *
     * @param mid   up主id
     * @param pageNum   页码，从1开始
     * @return  返回UpAudio类型集合
     */
    public static List<UpAudio> parseAudio(long mid, int pageNum) {
        Map<String, Object> value = new HashMap<>();
        value.put("uid", mid);
        value.put("pn", pageNum);
        value.put("ps", 30);
        value.put("order", 1);
        value.put("jsonp", "jsonp");

        String response = HttpUtils.GETByParam(Paths.music, value);

        JSONObject jsonObject = JSON.parseObject(response);

        JSONObject data = jsonObject.getJSONObject("data");
        if (data != null) {
            //获取数据总数
            count = getAudioCount(data);

            List<UpAudio> upAudios = new ArrayList<>();

            JSONArray innerData = data.getJSONArray("data");

            if (innerData != null) {
                for (Object innerDatum : innerData) {
                    UpAudio upAudio = new UpAudio();

                    JSONObject datumObject = (JSONObject) innerDatum;

                    //获取音频id
                    upAudio.sid = datumObject.getLong("id");

                    //获取封面
                    upAudio.cover = datumObject.getString("cover");

                    //获取音频时长
                    upAudio.duration = datumObject.getIntValue("duration");

                    //获取标题
                    upAudio.title = datumObject.getString("title");

                    //获取播放量
                    upAudio.play = datumObject.getJSONObject("statistic").getLong("play");

                    //获取发布时间
                    upAudio.ctime = datumObject.getLong("ctime");

                    upAudios.add(upAudio);
                }
            }

            return upAudios;
        }

        Log.e(Fuck.red, "upper接口数据获取失败", new NullPointerException("upper接口数据获取失败"));
        return null;
    }

    private static int getAudioCount(JSONObject data) {
        return data.getIntValue("pageSize");
    }
}
