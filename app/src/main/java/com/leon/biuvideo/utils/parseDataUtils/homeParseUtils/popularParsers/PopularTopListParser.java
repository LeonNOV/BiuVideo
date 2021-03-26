package com.leon.biuvideo.utils.parseDataUtils.homeParseUtils.popularParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.homeBeans.popularBeans.PopularTopList;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.ValueUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/25
 * @Desc 全站排行榜数据解析类，该类的所有请求均不需要Cookie
 */
public class PopularTopListParser {
    public static List<PopularTopList> parseData(String type, String url) {
        switch (type) {
            case "0":
                return parseRankingV2(url);
            case "1":
                return parseWebListAndBangumi(url, false);
            case "2":
                return parseWebListAndBangumi(url, true);
            default:
                return null;
        }
    }

    private static List<PopularTopList> parseRankingV2 (String rankingV2) {
        JSONObject response = HttpUtils.getResponse(rankingV2, null);
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

    private static List<PopularTopList> parseWebListAndBangumi(String url, boolean isBangumi) {
        JSONObject response = HttpUtils.getResponse(url, null);

        String key = isBangumi ? "result" : "data";
        JSONArray jsonArray = response.getJSONObject(key).getJSONArray("list");
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
                popularTopList.extra1 = ValueUtils.generateCN(stat.getIntValue("bangumi")) + "弹幕";
            } else {
                popularTopList.extra1 = jsonObject.getString("desc");
            }

            JSONObject newEp = jsonObject.getJSONObject("new_ep");
            popularTopList.extra2 = newEp.getString("index_show");
            if (popularTopList.extra1.equals(popularTopList.extra2)) {
                popularTopList.extra2 = null;
            }

            popularTopList.score = jsonObject.getString("pts");

            popularTopListList.add(popularTopList);
        }

        return popularTopListList;
    }
}
