package com.leon.biuvideo.utils.parseDataUtils.homeParseUtils.popularParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.homeBeans.popularBeans.PopularVideo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserInterface;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2021/3/24
 * @Desc 入站必刷解析类
 */
public class PopularPreciousParser extends ParserInterface<PopularVideo> {
    @Override
    public List<PopularVideo> parseData() {
        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.POPULAR_PRECIOUS, Headers.of(HttpUtils.getAPIRequestHeader()), null);
        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("list");
        List<PopularVideo> popularVideoList = new ArrayList<>(jsonArray.size());

        for (Object o : jsonArray) {
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

            popularVideo.duration = ValueUtils.lengthGenerate(jsonObject.getIntValue("duration"));
            String achievement = jsonObject.getString("achievement");
            popularVideo.reason = "".equals(achievement) ? null : achievement;

            popularVideoList.add(popularVideo);
        }

        return popularVideoList;
    }
}
