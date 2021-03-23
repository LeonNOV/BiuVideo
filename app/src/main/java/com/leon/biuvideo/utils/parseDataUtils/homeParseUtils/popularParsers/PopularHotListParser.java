package com.leon.biuvideo.utils.parseDataUtils.homeParseUtils.popularParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.homeBeans.popularBeans.HotVideo;
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
public class PopularHotListParser implements ParserInterface<HotVideo> {
    private static final int PAGE_SIZE = 20;
    private int pageNum = 1;
    public boolean dataStatus = true;

    @Override
    public List<HotVideo> parseData() {
        Map<String, String> params = new HashMap<>(2);
        params.put("pn", String.valueOf(pageNum));
        params.put("ps", String.valueOf(PAGE_SIZE));

        if (dataStatus) {
            JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.HOT_LIST, Headers.of(HttpUtils.getAPIRequestHeader()), params);
            JSONObject data = response.getJSONObject("data");

            dataStatus = !(data.getBoolean("no_more"));

            JSONArray list = data.getJSONArray("list");
            List<HotVideo> hotVideos = new ArrayList<>(list.size());

            for (Object o : list) {
                JSONObject jsonObject = (JSONObject) o;
                HotVideo hotVideo = new HotVideo();

                hotVideo.title = jsonObject.getString("title");
                hotVideo.pic = jsonObject.getString("pic");
                hotVideo.aid = jsonObject.getString("aid");
                hotVideo.bvid = jsonObject.getString("bvid");

                hotVideo.userName = jsonObject.getJSONObject("owner").getString("name");

                JSONObject stat = jsonObject.getJSONObject("stat");
                hotVideo.view = stat.getIntValue("view");
                hotVideo.danmaku = stat.getIntValue("danmaku");

                String reasonContent = jsonObject.getJSONObject("rcmd_reason").getString("content");
                hotVideo.reason = "".equals(reasonContent) ? null : reasonContent;

                hotVideo.duration = ValueUtils.lengthGenerate(jsonObject.getIntValue("duration"));

                hotVideos.add(hotVideo);
            }

            pageNum ++;

            return hotVideos;
        }

        return null;
    }
}
