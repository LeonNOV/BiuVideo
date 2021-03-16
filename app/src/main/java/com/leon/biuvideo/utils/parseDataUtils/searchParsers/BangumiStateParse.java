package com.leon.biuvideo.utils.parseDataUtils.searchParsers;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.searchBean.bangumi.BangumiState;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;

public class BangumiStateParse {

    /**
     * 获取番剧投币数、弹幕数量等信息
     *
     * @param seasonId  seasonId
     * @return  返回BangumiState
     */
    public BangumiState bangumiStateParse(long seasonId) {
        Map<String, String> params = new HashMap<>(1);
        params.put("season_id", String.valueOf(seasonId));

        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.bangumiState, Headers.of(HttpUtils.getAPIRequestHeader()), params);
        JSONObject result = responseObject.getJSONObject("result");

        if (result != null) {
            BangumiState bangumiState = new BangumiState();

            bangumiState.coins = result.getLongValue("coins");
            bangumiState.danmakus = result.getLongValue("danmakus");
            bangumiState.follow = result.getLongValue("follow");
            bangumiState.likes = result.getLongValue("likes");
            bangumiState.seriesFollow = result.getLongValue("series_follow");
            bangumiState.views = result.getLongValue("views");

            return bangumiState;
        }

        return null;
    }
}
