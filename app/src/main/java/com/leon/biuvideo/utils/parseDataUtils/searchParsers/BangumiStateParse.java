package com.leon.biuvideo.utils.parseDataUtils.searchParsers;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.searchBean.bangumi.BangumiState;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserUtils;
import com.leon.biuvideo.values.Paths;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;

public class BangumiStateParse {
    private final Map<String, String> requestHeader;

    public BangumiStateParse(Context context) {
        this.requestHeader = ParserUtils.getInterfaceRequestHeader(context);
    }

    /**
     * 获取番剧投币数、弹幕数量等信息
     *
     * @param seasonId  seasonId
     * @return  返回BangumiState
     */
    public BangumiState bangumiStateParse(long seasonId) {
        Map<String, String> params = new HashMap<>();
        params.put("season_id", String.valueOf(seasonId));

        JSONObject responseObject = HttpUtils.getResponse(Paths.bangumiState, Headers.of(requestHeader), params);
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
