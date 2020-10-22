package com.leon.biuvideo.utils.resourcesParseUtils;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.upMasterBean.UpAudio;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.LogTip;
import com.leon.biuvideo.utils.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 音频接口解析
 */
public class UpAudioParseUtils {

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
            List<UpAudio> upAudios = new ArrayList<>();

            JSONArray innerData = data.getJSONArray("data");
            for (Object innerDatum : innerData) {
                UpAudio upAudio = new UpAudio();

                JSONObject datumObject = (JSONObject) innerDatum;

                //获取音频id
                Long sid = datumObject.getLong("id");
                upAudio.sid = sid;

                //获取封面
                String cover = datumObject.getString("cover");
                upAudio.cover = cover;

                //获取音频时长
                int duration = datumObject.getIntValue("duration");
                upAudio.duration = duration;

                //获取标题
                String title = datumObject.getString("title");
                upAudio.title = title;

                //获取播放量
                Long play = datumObject.getJSONObject("statistic").getLong("play");
                upAudio.play = play;

                //获取发布时间
                Long ctime = datumObject.getLong("ctime");
                upAudio.ctime = ctime;

                upAudios.add(upAudio);
            }

            return upAudios;
        }

        Log.e(LogTip.red, "upper接口数据获取失败", new NullPointerException("upper接口数据获取失败"));
        return null;
    }
}
