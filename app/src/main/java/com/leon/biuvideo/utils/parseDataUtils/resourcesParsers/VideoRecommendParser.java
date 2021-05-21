package com.leon.biuvideo.utils.parseDataUtils.resourcesParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.resourcesBeans.VideoRecommend;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/4/7
 * @Desc 推荐视频解析类
 */
public class VideoRecommendParser {
    public static List<VideoRecommend> parseData (String bvid) {
        Map<String, String> params = new HashMap<>(1);
        params.put("bvid", bvid);

        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.VIDEO_RECOMMENDS, params);
        JSONArray data = response.getJSONArray("data");
        List<VideoRecommend> videoRecommendList = new ArrayList<>(data.size());
        for (Object datum : data) {
            JSONObject jsonObject = (JSONObject) datum;
            VideoRecommend videoRecommend = new VideoRecommend();

            videoRecommend.aid = jsonObject.getLongValue("aid");
            videoRecommend.bvid = jsonObject.getString("bvid");
            videoRecommend.cover = jsonObject.getString("pic");
            videoRecommend.title = jsonObject.getString("title");
            videoRecommend.desc = jsonObject.getString("desc");
            videoRecommend.pubdate = jsonObject.getLong("pubdate");
            videoRecommend.duration = ValueUtils.lengthGenerate(jsonObject.getIntValue("duration"));

            JSONObject owner = jsonObject.getJSONObject("owner");
            videoRecommend.userName = owner.getString("name");
            videoRecommend.userFace = owner.getString("face");
            videoRecommend.userMid = owner.getString("mid");

            JSONObject stat = jsonObject.getJSONObject("stat");
            videoRecommend.view = stat.getIntValue("view");
            videoRecommend.danmaku = stat.getIntValue("danmaku");
            videoRecommend.reply = stat.getIntValue("reply");
            videoRecommend.favorite = stat.getIntValue("favorite");
            videoRecommend.coin = stat.getIntValue("coin");
            videoRecommend.share = stat.getIntValue("share");
            videoRecommend.like = stat.getIntValue("like");

            videoRecommendList.add(videoRecommend);
        }

        return videoRecommendList;
    }
}
