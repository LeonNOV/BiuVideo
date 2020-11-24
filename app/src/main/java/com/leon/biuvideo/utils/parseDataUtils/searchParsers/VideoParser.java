package com.leon.biuvideo.utils.parseDataUtils.searchParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.upMasterBean.UpVideo;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.OrderType;
import com.leon.biuvideo.utils.Paths;
import com.leon.biuvideo.utils.SearchType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * 解析搜索结果-视频数据
 */
public class VideoParser {
    /**
     * 解析视频数据
     *
     * @param keyword   关键字
     * @param pn    页码
     * @param orderType 排序方式
     * @return  返回视频数据
     */
    public List<UpVideo> videoParse(String keyword, int pn, OrderType orderType) {
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("search_type", SearchType.VIDEO.value);
        params.put("page", String.valueOf(pn));
        params.put("order", orderType.value);

        Fuck.blue(Paths.search + params.toString());

        HttpUtils httpUtils = new HttpUtils(Paths.search, Headers.of("Referer", "https://search.bilibili.com"), params);
        String response = httpUtils.getData();

        JSONObject responseObject = JSONObject.parseObject(response);

        List<UpVideo> videos = new ArrayList<>();

        JSONObject data = responseObject.getJSONObject("data");
        if (data != null) {
            videos = parseData(data, keyword);
        }

        return videos;
    }

    /**
     * 解析JSONObject类型的数据
     *
     * @param data  JSONObject对象
     * @return  返回解析结果
     */
    private List<UpVideo> parseData(JSONObject data, String keyword) {
        String oldStr = "<em class=\"keyword\">" + keyword + "</em>";

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
                upVideo.title = jsonObject.getString("title").replaceAll(oldStr, "");

                //获取视频说明
                upVideo.description = jsonObject.getString("description");

                //获取视频封面
                upVideo.cover = "http://" + jsonObject.getString("pic");

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

    /**
     * 是否与搜索关键词相匹配
     *
     * @param keyword   搜索关键词
     * @return  返回是否匹配
     */
    public static boolean isMatch(String keyword) {
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("search_type", SearchType.VIDEO.value);
        params.put("page", "1");
        params.put("order", OrderType.DEFAULT.value);

        HttpUtils httpUtils = new HttpUtils(Paths.search, Headers.of("Referer", "https://search.bilibili.com"), params);
        String response = httpUtils.getData();

        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONObject data = jsonObject.getJSONObject("data");

        String isMatch = data.getString("suggest_keyword");

        return isMatch.equals("");
    }

    /**
     * 获取结果个数
     *
     * @param keyword   关键字
     * @return  返回搜索结果个数
     */
    public static int getSearchVideoCount(String keyword) {
        int count = -1;

        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("search_type", SearchType.VIDEO.value);
        params.put("page", "1");
        params.put("order", OrderType.DEFAULT.value);

        HttpUtils httpUtils = new HttpUtils(Paths.search, Headers.of("Referer", "https://search.bilibili.com"), params);
        String response = httpUtils.getData();

        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONObject data = jsonObject.getJSONObject("data");

        if (data != null) {
            count = data.getIntValue("numResults");
        }

        return count;
    }
}
