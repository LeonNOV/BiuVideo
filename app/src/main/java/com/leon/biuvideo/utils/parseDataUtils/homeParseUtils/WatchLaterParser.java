package com.leon.biuvideo.utils.parseDataUtils.homeParseUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.homeBeans.WatchLater;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserInterface;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2021/3/14
 * @Desc 稍后观看解析类
 */
public class WatchLaterParser implements ParserInterface<WatchLater> {
    @Override
    public List<WatchLater> parseData() {
        // 获取Cookie
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Cookie", PreferenceUtils.getCookie());

        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.WATCH_LATER, Headers.of(headers),null);
        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("list");

        List<WatchLater> watchLaterList = new ArrayList<>(jsonArray.size());
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            WatchLater watchLater = new WatchLater();

            watchLater.isInvalid = jsonObject.getIntValue("state") < 0;
            watchLater.aid = jsonObject.getLongValue("aid");
            watchLater.bvid = jsonObject.getString("bvid");
            watchLater.videos = jsonObject.getIntValue("videos");
            watchLater.cover = jsonObject.getString("pic");
            watchLater.title = jsonObject.getString("title");
            watchLater.desc = jsonObject.getString("desc");
            watchLater.duration = jsonObject.getIntValue("duration");

            JSONObject owner = jsonObject.getJSONObject("owner");
            watchLater.userMid = owner.getString("mid");
            watchLater.userName = owner.getString("name");
            watchLater.userFace = owner.getString("face");

            watchLater.progress = jsonObject.getIntValue("progress");

            JSONObject stat = jsonObject.getJSONObject("stat");
            watchLater.view = stat.getIntValue("view");
            watchLater.danmaku = stat.getIntValue("danmaku");
            watchLater.reply = stat.getIntValue("reply");
            watchLater.favorite = stat.getIntValue("favorite");
            watchLater.coin = stat.getIntValue("coin");
            watchLater.share = stat.getIntValue("share");
            watchLater.like = stat.getIntValue("like");

            watchLater.addTime = jsonObject.getLongValue("add_at");

            watchLaterList.add(watchLater);
        }

        return watchLaterList;
    }
}
