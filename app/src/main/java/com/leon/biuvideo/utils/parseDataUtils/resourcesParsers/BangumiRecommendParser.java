package com.leon.biuvideo.utils.parseDataUtils.resourcesParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.resourcesBeans.BangumiRecommend;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/4/27
 * @Desc 推荐番剧解析类
 */
public class BangumiRecommendParser {
    public static List<BangumiRecommend> parseData (String seasonId) {
        Map<String, String> params = new HashMap<>(1);
        params.put("season_id", seasonId);

        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.BANGUMI_RECOMMENDS, params);
        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("season");

        List<BangumiRecommend> bangumiRecommendList = new ArrayList<>(jsonArray.size());
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            BangumiRecommend bangumiRecommend = new BangumiRecommend();

            bangumiRecommend.seasonId = jsonObject.getString("season_id");
            bangumiRecommend.title = jsonObject.getString("title");
            bangumiRecommend.cover = jsonObject.getString("cover");

            bangumiRecommend.newEp = jsonObject.getJSONObject("new_ep").getString("index_show");
            bangumiRecommend.badge = jsonObject.getString("badge");

            JSONObject rating = jsonObject.getJSONObject("rating");
            bangumiRecommend.ratingCount = ValueUtils.generateCN(rating.getIntValue("count")) + "人点评";
            bangumiRecommend.ratingScore = ValueUtils.generateCN(rating.getIntValue("score"));

            JSONObject stat = jsonObject.getJSONObject("stat");
            bangumiRecommend.play = ValueUtils.generateCN(stat.getIntValue("view"));
            bangumiRecommend.follow = ValueUtils.generateCN(stat.getIntValue("follow"));

            bangumiRecommendList.add(bangumiRecommend);
        }

        return bangumiRecommendList;
    }
}
