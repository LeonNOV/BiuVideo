package com.leon.biuvideo.utils.searchParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.upMasterBean.UpVideo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.OrderType;
import com.leon.biuvideo.utils.Paths;
import com.leon.biuvideo.utils.SearchType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoParser {

    public List<UpVideo> VideoParse(String keyword, int pn, OrderType orderType) {
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("search_type", SearchType.VIDEO.value);
        params.put("page", String.valueOf(pn));
        params.put("order", orderType.value);

        HttpUtils httpUtils = new HttpUtils(Paths.search, params);
        String response = httpUtils.getData();

        JSONObject responseObject = JSONObject.parseObject(response);

        List<UpVideo> videos = new ArrayList<>();

        JSONObject data = responseObject.getJSONObject("data");
        if (data != null) {
            videos = parseData(data);
        }

        return videos;
    }

    private List<UpVideo> parseData(JSONObject data) {
        JSONArray result = data.getJSONArray("result");

        List<UpVideo> videos;
        if (result.size() != 0) {
            videos = new ArrayList<>();

            for (Object o : result) {
                UpVideo upVideo = new UpVideo();
                JSONObject jsonObject = (JSONObject) o;

                //获取作者
                upVideo.author = jsonObject.getString("author");

                //获取作者ID
                upVideo.mid = jsonObject.getLongValue("mid");

                //获取aid
                upVideo.aid = jsonObject.getLongValue("aid");

                //获取bvid
                upVideo.bvid = jsonObject.getString("bvid");

                //获取标题
                upVideo.title = jsonObject.getString("title");

                //获取视频说明
                upVideo.description = jsonObject.getString("description");

                //获取视频封面
                upVideo.cover = jsonObject.getString("pic");

                //获取播放量
                upVideo.play = jsonObject.getIntValue("play");

                //获取评论数
                upVideo.create = jsonObject.getLongValue("pubdate");

                //获取视频长度
                upVideo.length = jsonObject.getString("duration");

                //获取合作关系
                upVideo.isUnionVideo = jsonObject.getIntValue("is_union_video");

                videos.add(upVideo);
            }

            return videos;
        }

        return null;
    }
}
