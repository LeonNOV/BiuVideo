package com.leon.biuvideo.utils.parseDataUtils.bangumiParsers;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.BangumiAnthologyStat;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2021/4/23
 * @Desc 用户与番剧选集关系解析类
 */
public class BangumiAnthologyStatParser {
    public static BangumiAnthologyStat parseData (String epId) {
        Map<String, String> params = new HashMap<>(1);
        params.put("ep_id", epId);

        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.BANGUMI_SERIES_STAT, Headers.of(HttpUtils.getAPIRequestHeader()), params);
        if (response.getIntValue("code") == 0) {
            JSONObject data = response.getJSONObject("data");

            BangumiAnthologyStat bangumiAnthologyStat = new BangumiAnthologyStat();
            JSONObject stat = data.getJSONObject("stat");
            bangumiAnthologyStat.coin = stat.getIntValue("coin");
            bangumiAnthologyStat.danmaku = stat.getIntValue("dm");
            bangumiAnthologyStat.like = stat.getIntValue("like");
            bangumiAnthologyStat.reply = stat.getIntValue("reply");
            bangumiAnthologyStat.view = stat.getIntValue("view");

            JSONObject userCommunity = data.getJSONObject("user_community");
            bangumiAnthologyStat.isCoin = userCommunity.getIntValue("coin_number") > 0;
            bangumiAnthologyStat.isFavorite = userCommunity.getIntValue("favorite") > 0;
            bangumiAnthologyStat.isLike = userCommunity.getIntValue("like") > 0;

            return bangumiAnthologyStat;
        }

        return null;
    }
}
