package com.leon.biuvideo.utils.resourcesParseUtils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.LogTip;
import com.leon.biuvideo.utils.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicUrlParseUtils {

    /**
     * 获取音频播放地址
     *
     * @param sid   音频ID
     * @return  返回一个结果集
     */
    public static Map<String, Object> parseMusicUrl(long sid) {
        Map<String, Object> values = new HashMap<>();
        values.put("sid", sid);

        String response = HttpUtils.GETByParam(Paths.musicUrl, values);

        JSONObject jsonObject = JSON.parseObject(response);

        JSONObject dataObject = jsonObject.getJSONObject("data");

        if (dataObject != null) {
            Map<String, Object> map = new HashMap<>();

            //获取文件大小
            long size = dataObject.getLongValue("size");
            map.put("size", size);

            JSONArray cdns = dataObject.getJSONArray("cdns");

            //获取music文件地址
            List<String> urls = new ArrayList<>();
            for (Object cdn : cdns) {
                urls.add(cdn.toString());
            }
            map.put("urls", urls);

            return map;
        }

        Log.e(LogTip.red, "musicUrl接口数据获取失败", new NullPointerException("musicUrl接口数据获取失败"));
        return null;
    }
}
