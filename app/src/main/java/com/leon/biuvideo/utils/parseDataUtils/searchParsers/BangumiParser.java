package com.leon.biuvideo.utils.parseDataUtils.searchParsers;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.searchBean.bangumi.Bangumi;
import com.leon.biuvideo.beans.searchBean.bangumi.Ep;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserUtils;
import com.leon.biuvideo.values.Paths;
import com.leon.biuvideo.values.SearchType;
import com.leon.biuvideo.values.SortType;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * 解析搜索结果-番剧
 */
public class BangumiParser {
    private final Map<String, String> requestHeader;

    public BangumiParser(Context context) {
        this.requestHeader = ParserUtils.getInterfaceRequestHeader(context);
        for (Map.Entry<String, String> entry : this.requestHeader.entrySet()) {
            if (entry.getKey().equals("Referer")) {
                entry.setValue("https://search.bilibili.com");
                break;
            }
        }
    }

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

        JSONObject responseObject = HttpUtils.getResponse(Paths.search, Headers.of(requestHeader), params);
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
                bangumi.originalTitle = jsonObject.getString("org_title");
                bangumi.cover = "http:" + jsonObject.getString("cover");
                bangumi.styles = jsonObject.getString("styles");
                bangumi.area = jsonObject.getString("areas");
                bangumi.playTime = jsonObject.getLongValue("pubtime");
                bangumi.voiceActors = jsonObject.getString("cv");
                bangumi.otherInfo = jsonObject.getString("staff");
                bangumi.desc = jsonObject.getString("desc");
                bangumi.epSize = jsonObject.getIntValue("ep_size");
                bangumi.angleTitle = jsonObject.getString("angle_title");

                JSONObject mediaScore = jsonObject.getJSONObject("media_score");
                if (mediaScore != null) {
                    bangumi.score = mediaScore.getFloatValue("score");
                    bangumi.reviewNum = mediaScore.getIntValue("user_count");
                } else {
                    bangumi.score = 0;
                    bangumi.reviewNum = 0;
                }

                // 获取番剧更新信息
                bangumi.bangumiState = getBangumiState(bangumi.mediaId);

                JSONArray eps = jsonObject.getJSONArray("eps");
                List<Ep> epList = new ArrayList<>();
                for (Object epo : eps) {
                    JSONObject epObject = (JSONObject) epo;
                    Ep ep = new Ep();
                    ep.id = epObject.getLongValue("id");
                    ep.title = epObject.getString("title");
                    ep.longTitle = epObject.getString("long_title");
                    ep.url = epObject.getString("url");
                    ep.cover = jsonObject.getString("cover");

                    JSONArray badges = epObject.getJSONArray("badges");
                    if (badges.size() > 0) {
                        JSONObject badgeObject = (JSONObject) badges.get(0);
                        String text = badgeObject.getString("text");
                        ep.badge = text.equals("") ? null : text;
                        ep.isVIP = ep.badge.equals("会员");
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
     * 获取番剧状态
     *
     * @param mediaId   mid
     * @return  返回番剧状态
     */
    private String getBangumiState(long mediaId) {
        HttpUtils httpUtils = new HttpUtils(Paths.bangumiStateWhiteMid + mediaId, null);
        String data = httpUtils.getData();

        Document document = Jsoup.parse(data);

        Elements elements = document.getElementsByClass("media-info-time");
        Element first = elements.first();
        Elements spans = first.getElementsByTag("span");

        // 只获取第二个span中的内容
        if (spans.size() > 1) {
            return spans.get(1).text();
        } else {
            return "未知状态";
        }
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

        JSONObject responseObject = HttpUtils.getResponse(Paths.search, Headers.of(requestHeader), params);
        JSONObject data = responseObject.getJSONObject("data");

        if (data != null) {
            return data.getIntValue("numResults");
        }

        return 0;
    }

    /**
     * 获取番剧各选集cid
     *
     * @param seasonId  sid/seasonId
     * @param eps   eps
     * @return  返回eps
     */
    public List<Ep> getEpCids(long seasonId, List<Ep> eps) {
        Map<String, String> params = new HashMap<>();
        params.put("season_id", String.valueOf(seasonId));

        JSONObject responseObject = HttpUtils.getResponse(Paths.bangumiEpCid, Headers.of(requestHeader), params);
        JSONObject result = responseObject.getJSONObject("result");
        JSONObject mainSection = result.getJSONObject("main_section");
        JSONArray episodes = mainSection.getJSONArray("episodes");

        for (int i = 0; i < episodes.size(); i++) {
            JSONObject jsonObject = (JSONObject) episodes.get(i);

            eps.get(i).aid = jsonObject.getLongValue("aid");
            eps.get(i).cid = jsonObject.getLongValue("cid");
        }

        return eps;
    }
}
