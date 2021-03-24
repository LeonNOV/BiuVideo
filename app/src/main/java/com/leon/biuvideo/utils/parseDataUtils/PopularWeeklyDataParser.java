package com.leon.biuvideo.utils.parseDataUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.homeBeans.popularBeans.HotVideo;
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
 * @Desc 每周必看往期数据解析类<br/ >
 *      由于该接口的响应数据和{@link HotVideo}相同，所以该解析类返回的集合泛型使用HotVideo
 */
public class PopularWeeklyDataParser {

    public List<HotVideo> parseData(int number) {
        Map<String, String> params = new HashMap<>(1);
        params.put("number", String.valueOf(number));

        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.POPULAR_WEEKLY_DATA, Headers.of(HttpUtils.getAPIRequestHeader()), params);
        JSONArray list = response.getJSONObject("data").getJSONArray("list");

        List<HotVideo> hotVideoList = new ArrayList<>(list.size());
        for (Object o : list) {
            JSONObject jsonObject = (JSONObject) o;
            HotVideo hotVideo = new HotVideo();

            hotVideo.title = jsonObject.getString("title");
            hotVideo.pic = jsonObject.getString("pic");
            hotVideo.aid = jsonObject.getString("aid");
            hotVideo.bvid = jsonObject.getString("bvid");

            JSONObject owner = jsonObject.getJSONObject("owner");
            hotVideo.userName = owner.getString("name");

            JSONObject stat = jsonObject.getJSONObject("stat");
            hotVideo.view = stat.getIntValue("view");
            hotVideo.danmaku = stat.getIntValue("danmaku");

            String reason = stat.getString("rcmd_reason");
            hotVideo.reason = "".equals(reason) ? null : reason;

            hotVideo.duration = ValueUtils.lengthGenerate(jsonObject.getIntValue("duration"));

            hotVideoList.add(hotVideo);
        }

        return hotVideoList;
    }
}
