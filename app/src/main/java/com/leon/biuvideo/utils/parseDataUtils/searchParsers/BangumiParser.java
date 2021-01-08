package com.leon.biuvideo.utils.parseDataUtils.searchParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.searchBean.bangumi.Bangumi;
import com.leon.biuvideo.beans.searchBean.bangumi.Ep;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.values.Paths;
import com.leon.biuvideo.values.SearchType;
import com.leon.biuvideo.values.SortType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * 解析搜索结果-番剧
 */
public class BangumiParser {
    /**
     * 获取番剧
     *
     * @param keyword   番剧关键字
     * @param pn    页码，从1开始
     * @param sortType  排序方式
     * @return  返回Bangumi集合
     */
    public List<Bangumi> bangumiParse(String keyword, int pn, SortType sortType) {
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("search_type", SearchType.BANGUMI.value);
        params.put("page", String.valueOf(pn));
        params.put("order", sortType.value);

        JSONObject responseObject = HttpUtils.getResponse(Paths.search, Headers.of("Referer", "https://search.bilibili.com"), params);
        JSONObject data = responseObject.getJSONObject("data");

        if (data != null) {
            JSONArray result = data.getJSONArray("result");

            List<Bangumi> bangumiList = new ArrayList<>();
            for (Object o : result) {
                JSONObject jsonObject = (JSONObject) o;
                Bangumi bangumi = new Bangumi();

                bangumi.mediaId = jsonObject.getLongValue("media_id");
                bangumi.seasonId = jsonObject.getLongValue("season_id");
                bangumi.title = jsonObject.getString("title").replaceAll("<em class=\"keyword\">", "").replaceAll("</em>", "");
                bangumi.cover = "http:" + jsonObject.getString("cover");
                bangumi.styles = jsonObject.getString("styles");
                bangumi.area = jsonObject.getString("areas");
                bangumi.playTime = jsonObject.getLongValue("pubtime");
                bangumi.voiceActors = jsonObject.getString("cv");
                bangumi.desc = jsonObject.getString("desc");
                bangumi.epSize = jsonObject.getIntValue("ep_size");
                bangumi.angleTitle = jsonObject.getString("angle_title");

                JSONObject mediaScore = jsonObject.getJSONObject("media_score");
                bangumi.score = mediaScore.getDoubleValue("score");
                bangumi.reviewNum = mediaScore.getIntValue("user_count");

                JSONArray eps = jsonObject.getJSONArray("eps");
                List<Ep> epList = new ArrayList<>();
                for (Object epo : eps) {
                    JSONObject epObject = (JSONObject) epo;
                    Ep ep = new Ep();
                    ep.id = epObject.getLongValue("id");
                    ep.title = epObject.getString("title");
                    ep.longTitle = epObject.getString("long_title");
                    ep.url = epObject.getString("url");

                    JSONArray badges = epObject.getJSONArray("badges");
                    if (badges.size() != 0) {
                        JSONObject badgeObject = (JSONObject) badges.get(0);
                        ep.isVIP = badgeObject.getString("text").equals("会员");
                    } else {
                        ep.isVIP = false;
                    }

                    epList.add(ep);
                }

                bangumi.eps = epList;

                bangumiList.add(bangumi);
            }

            return bangumiList;
        }

        return null;
    }

    /**
     * 获取结果个数
     *
     * @param keyword   搜索关键字
     * @return  返回结果个数
     */
    public int getSearchBangumiCount(String keyword) {
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("search_type", SearchType.BANGUMI.value);
        params.put("page", "1");
        params.put("order", SortType.DEFAULT.value);

        JSONObject responseObject = HttpUtils.getResponse(Paths.search, Headers.of("Referer", "https://search.bilibili.com"), params);
        JSONObject data = responseObject.getJSONObject("data");

        if (data != null) {
            return data.getIntValue("numResults");
        }

        return 0;
    }
}
