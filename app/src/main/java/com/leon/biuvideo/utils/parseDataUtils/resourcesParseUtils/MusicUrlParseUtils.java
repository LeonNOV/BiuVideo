package com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.values.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicUrlParseUtils {

    /**
     * 获取音频播放地址
     *
     * @param sid   音频ID
     * @return  返回第一个music链接
     */
    public static String parseMusicUrl(String sid) {
        Map<String, String> params = new HashMap<>();
        params.put("sid", sid);

        JSONObject responseObject = HttpUtils.getResponse(Paths.musicUrl, params);
        JSONObject dataObject = responseObject.getJSONObject("data");

        if (dataObject != null) {
            JSONArray cdns = dataObject.getJSONArray("cdns");

            //获取music文件地址
            List<String> urls = new ArrayList<>();
            for (Object cdn : cdns) {
                urls.add(cdn.toString());
            }

            //只返回第一个链接
            return urls.get(0);
        }

        Log.e(Fuck.red, "musicUrl接口数据获取失败", new NullPointerException("musicUrl接口数据获取失败"));
        return null;
    }
}
