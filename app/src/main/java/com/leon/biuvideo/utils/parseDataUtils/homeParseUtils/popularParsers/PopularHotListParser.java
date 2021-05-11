package com.leon.biuvideo.utils.parseDataUtils.homeParseUtils.popularParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.homeBeans.popularBeans.PopularVideo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserInterface;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2021/3/23
 * @Desc 获取综合热门数据
 */
public class PopularHotListParser extends ParserInterface<PopularVideo> {
    private static final int PAGE_SIZE = 20;
    private int pageNum = 1;

    @Override
    public List<PopularVideo> parseData() {
        Map<String, String> params = new HashMap<>(2);
        params.put("pn", String.valueOf(pageNum));
        params.put("ps", String.valueOf(PAGE_SIZE));

        if (dataStatus) {
            JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.HOT_LIST, Headers.of(HttpUtils.getAPIRequestHeader()), params);
            JSONObject data = response.getJSONObject("data");

            dataStatus = !(data.getBoolean("no_more"));

            JSONArray list = data.getJSONArray("list");
            List<PopularVideo> popularVideos = new ArrayList<>(list.size());

            for (Object o : list) {
                JSONObject jsonObject = (JSONObject) o;
                PopularVideo popularVideo = new PopularVideo();

                popularVideo.title = jsonObject.getString("title");
                popularVideo.pic = jsonObject.getString("pic");
                popularVideo.aid = jsonObject.getString("aid");
                popularVideo.bvid = jsonObject.getString("bvid");

                popularVideo.userName = jsonObject.getJSONObject("owner").getString("name");

                JSONObject stat = jsonObject.getJSONObject("stat");
                popularVideo.view = stat.getIntValue("view");
                popularVideo.danmaku = stat.getIntValue("danmaku");

                String reasonContent = jsonObject.getJSONObject("rcmd_reason").getString("content");
                popularVideo.reason = "".equals(reasonContent) ? null : reasonContent;

                popularVideo.duration = ValueUtils.lengthGenerate(jsonObject.getIntValue("duration"));

                popularVideos.add(popularVideo);
            }

            pageNum ++;

            return popularVideos;
        }

        return null;
    }
}
