package com.leon.biuvideo.utils.parseDataUtils.homeParseUtils.popularParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.homeBeans.popularBeans.PopularVideo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2021/3/24
 * @Desc 每周必看往期数据解析类
 */
public class PopularWeeklyDataParser {

    public List<PopularVideo> parseData(int number) {
        Map<String, String> params = new HashMap<>(1);
        params.put("number", String.valueOf(number));

        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.POPULAR_WEEKLY_DATA, Headers.of(HttpUtils.getAPIRequestHeader()), params);
        JSONArray list = response.getJSONObject("data").getJSONArray("list");

        List<PopularVideo> popularVideoList = new ArrayList<>(list.size());
        for (Object o : list) {
            JSONObject jsonObject = (JSONObject) o;
            PopularVideo popularVideo = new PopularVideo();

            popularVideo.title = jsonObject.getString("title");
            popularVideo.pic = jsonObject.getString("pic");
            popularVideo.aid = jsonObject.getString("aid");
            popularVideo.bvid = jsonObject.getString("bvid");

            JSONObject owner = jsonObject.getJSONObject("owner");
            popularVideo.userName = owner.getString("name");

            JSONObject stat = jsonObject.getJSONObject("stat");
            popularVideo.view = stat.getIntValue("view");
            popularVideo.danmaku = stat.getIntValue("danmaku");

            String reason = jsonObject.getString("rcmd_reason");
            popularVideo.reason = "".equals(reason) ? null : reason;

            popularVideo.duration = ValueUtils.lengthGenerate(jsonObject.getIntValue("duration"));

            popularVideoList.add(popularVideo);
        }

        return popularVideoList;
    }
}
