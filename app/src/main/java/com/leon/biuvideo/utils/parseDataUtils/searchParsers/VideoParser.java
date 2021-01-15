package com.leon.biuvideo.utils.parseDataUtils.searchParsers;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.upMasterBean.Video;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserUtils;
import com.leon.biuvideo.values.Paths;
import com.leon.biuvideo.values.SearchType;
import com.leon.biuvideo.values.SortType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * 解析搜索结果-视频数据
 */
public class VideoParser {
    private final Map<String, String> requestHeader;

    public VideoParser(Context context) {
        this.requestHeader = ParserUtils.getInterfaceRequestHeader(context);
        for (Map.Entry<String, String> entry : this.requestHeader.entrySet()) {
            if (entry.getKey().equals("Referer")) {
                entry.setValue("https://search.bilibili.com");
                break;
            }
        }
    }

    /**
     * 解析视频数据
     *
     * @param keyword   关键字
     * @param pn    页码
     * @param sortType 排序方式
     * @return  返回视频数据
     */
    public List<Video> videoParse(String keyword, int pn, SortType sortType) {
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("search_type", SearchType.VIDEO.value);
        params.put("page", String.valueOf(pn));
        params.put("order", sortType.value);

        JSONObject responseObject = HttpUtils.getResponse(Paths.search, Headers.of(requestHeader), params);
        JSONObject data = responseObject.getJSONObject("data");

        List<Video> videos = new ArrayList<>();
        if (data != null) {
            videos = parseData(data);
        }

        return videos;
    }

    /**
     * 解析JSONObject类型的数据
     *
     * @param data  JSONObject对象
     * @return  返回解析结果
     */
    private List<Video> parseData(JSONObject data) {
        JSONArray result = data.getJSONArray("result");

        List<Video> videos;
        if (result.size() != 0) {
            videos = new ArrayList<>();

            for (Object o : result) {
                Video video = new Video();
                JSONObject jsonObject = (JSONObject) o;

                //获取作者
                video.author = jsonObject.getString("author");

                //获取作者ID
                video.mid = jsonObject.getLongValue("mid");

                //获取aid
                video.aid = jsonObject.getLongValue("aid");

                //获取bvid
                video.bvid = jsonObject.getString("bvid");

                //获取标题
                video.title = jsonObject.getString("title").replaceAll("<em class=\"keyword\">", "").replaceAll("</em>", "");

                //获取视频说明
                video.description = jsonObject.getString("description");

                //获取视频封面
                video.cover = "http:" + jsonObject.getString("pic");

                //获取播放量
                video.play = jsonObject.getIntValue("play");

                //获取评论数
                video.create = jsonObject.getLongValue("pubdate");

                //获取视频长度
                video.length = jsonObject.getString("duration");

                //获取合作关系
                video.isUnionVideo = jsonObject.getIntValue("is_union_video");

                videos.add(video);
            }

            return videos;
        }

        return null;
    }

    /**
     * 获取结果个数
     *
     * @param keyword   关键字
     * @return  返回搜索结果个数
     */
    public int getSearchVideoCount(String keyword) {
        int count = -1;

        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("search_type", SearchType.VIDEO.value);
        params.put("page", "1");
        params.put("order", SortType.DEFAULT.value);

        HttpUtils httpUtils = new HttpUtils(Paths.search, Headers.of(requestHeader), params);
        String response = httpUtils.getData();

        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONObject data = jsonObject.getJSONObject("data");

        if (data != null) {
            count = data.getIntValue("numResults");
        }

        return count;
    }

    /**
     * 是否与搜索关键词相匹配
     *
     * @param keyword   搜索关键词
     * @return  返回是否匹配
     */
    public boolean dataState(String keyword) {
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("search_type", SearchType.VIDEO.value);
        params.put("page", "1");
        params.put("order", SortType.DEFAULT.value);

        HttpUtils httpUtils = new HttpUtils(Paths.search, Headers.of(requestHeader), params);
        String response = httpUtils.getData();

        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONObject data = jsonObject.getJSONObject("data");

        String isMatch = data.getString("suggest_keyword");

        return !isMatch.equals("");
    }
}
