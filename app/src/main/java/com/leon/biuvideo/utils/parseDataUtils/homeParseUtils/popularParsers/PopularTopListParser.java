package com.leon.biuvideo.utils.parseDataUtils.homeParseUtils.popularParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.homeBeans.popularBeans.PopularTopList;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.apis.BiliBiliFullSiteAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/3/25
 * @Desc 全站排行榜数据解析类，该类的所有请求均不需要Cookie
 */
public class PopularTopListParser {
    public static List<PopularTopList> parseRankingV2 (BiliBiliFullSiteAPIs.FullSiteRids fullSiteRids, BiliBiliFullSiteAPIs.RANKING_V2_TYPE rankingV2Type) {
        Map<String, String> params = new HashMap<>(2);
        params.put("rid", String.valueOf(fullSiteRids.value));
        params.put("type", rankingV2Type.value);

        JSONObject response = HttpUtils.getResponse(BiliBiliFullSiteAPIs.RANKING_V2, params);
        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("list");

        List<PopularTopList> popularTopListList = new ArrayList<>(jsonArray.size());
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            PopularTopList popularTopList = new PopularTopList();

            popularTopList.aid = jsonObject.getString("aid");
            popularTopList.bvid = jsonObject.getString("bvid");

            popularTopList.title = jsonObject.getString("title");
            popularTopList.cover = jsonObject.getString("pic");

            JSONObject stat = jsonObject.getJSONObject("stat");
            popularTopList.extra1 = stat.getIntValue("danmaku") + "弹幕";

            popularTopList.score = jsonObject.getString("score");
            popularTopList.duration = ValueUtils.lengthGenerate(jsonObject.getIntValue("duration"));

            if (jsonObject.containsKey("others")) {
                JSONArray others = jsonObject.getJSONArray("others");

                popularTopList.otherVideoList = new ArrayList<>(others.size());

                for (Object o1 : others) {
                    JSONObject other = (JSONObject) o1;
                    PopularTopList.OtherVideo otherVideo = new PopularTopList.OtherVideo();

                    otherVideo.aid = other.getString("aid");
                    otherVideo.bvid = other.getString("bvid");
                    otherVideo.score = other.getString("score");
                    otherVideo.title = other.getString("title");

                    popularTopList.otherVideoList.add(otherVideo);
                }
            }

            popularTopListList.add(popularTopList);
        }

        return popularTopListList;
    }

    /**
     * 解析 BiliBiliFullSiteAPIs.WEB_LIST 接口数据<br/>
     * 注意：参数`day`默认为3
     *
     * @param seasonType    seasonType
     * @return  PopularTopList集合
     */
    public static List<PopularTopList> parseWebList (BiliBiliFullSiteAPIs.SeasonType seasonType) {
        return parseWebListAndBangumi(false, seasonType);
    }

    public static List<PopularTopList> parseBangumi () {
        return parseWebListAndBangumi(true, null);
    };

    private static List<PopularTopList> parseWebListAndBangumi(boolean isBangumi, BiliBiliFullSiteAPIs.SeasonType seasonType) {
        JSONObject response;

        if (isBangumi && seasonType == null) {
            response = HttpUtils.getResponse(BiliBiliFullSiteAPIs.BANGUMI, null);
        } else {
            Map<String, String> params = new HashMap<>(2);
            params.put("day", "3");
            params.put("season_type", String.valueOf(seasonType.value));
            response = HttpUtils.getResponse(BiliBiliFullSiteAPIs.WEB_LIST, params);
        }

        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("list");
        List<PopularTopList> popularTopListList = new ArrayList<>(jsonArray.size());

        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            PopularTopList popularTopList = new PopularTopList();

            popularTopList.seasonId = jsonObject.getString("season_id");
            popularTopList.title = jsonObject.getString("title");
            popularTopList.cover = jsonObject.getString("cover");
            popularTopList.badge = jsonObject.getString("badge");

            if (isBangumi) {
                JSONObject stat = jsonObject.getJSONObject("stat");
                popularTopList.extra1 = ValueUtils.lengthGenerate(stat.getIntValue("bangumi")) + "弹幕";
            } else {
                popularTopList.extra1 = jsonObject.getString("desc");
            }

            JSONObject newEp = jsonObject.getJSONObject("new_ep");
            popularTopList.extra2 = newEp.getString("index_show");
            popularTopList.score = jsonObject.getString("pts");

            popularTopListList.add(popularTopList);
        }

        return popularTopListList;
    }
}
