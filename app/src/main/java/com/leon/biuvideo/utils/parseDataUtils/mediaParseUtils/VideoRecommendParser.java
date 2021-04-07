package com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.homeBeans.Recommend;
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
    public static List<Recommend> parseData (String bvid) {
        Map<String, String> params = new HashMap<>(1);
        params.put("bvid", bvid);

        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.VIDEO_RECOMMEND, params);
        JSONArray data = response.getJSONArray("data");
        List<Recommend> recommendList = new ArrayList<>(data.size());
        for (Object datum : data) {
            JSONObject jsonObject = (JSONObject) datum;
            Recommend recommend = new Recommend();

            recommend.aid = jsonObject.getLongValue("aid");
            recommend.bvid = jsonObject.getString("bvid");
            recommend.cover = jsonObject.getString("pic");
            recommend.title = jsonObject.getString("title");
            recommend.desc = jsonObject.getString("desc");
            recommend.pubdate = jsonObject.getLong("pubdate");
            recommend.duration = ValueUtils.lengthGenerate(jsonObject.getIntValue("duration"));

            JSONObject owner = jsonObject.getJSONObject("owner");
            recommend.userName = owner.getString("name");
            recommend.userFace = owner.getString("face");
            recommend.userMid = owner.getString("mid");

            JSONObject stat = jsonObject.getJSONObject("stat");
            recommend.view = stat.getIntValue("view");
            recommend.danmaku = stat.getIntValue("danmaku");
            recommend.reply = stat.getIntValue("reply");
            recommend.favorite = stat.getIntValue("favorite");
            recommend.coin = stat.getIntValue("coin");
            recommend.share = stat.getIntValue("share");
            recommend.like = stat.getIntValue("like");

            recommendList.add(recommend);
        }

        return recommendList;
    }
}
