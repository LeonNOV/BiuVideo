package com.leon.biuvideo.utils.parseDataUtils.searchParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.searchResultBeans.SearchResultVideo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserInterface;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2020/11/22
 * @Desc 视频搜索结果解析类
 */
public class SearchResultVideoParser extends ParserInterface<SearchResultVideo> {
    /**
     * 页码数
     */
    private int pageNum = 1;

    /**
     * 总页面数
     */
    private int maxPages = -1;

    /**
     * 总条目数
     */
    private int maxItems = -1;

    /**
     * 当前条目数
     */
    private int currentItems = 0;

    private final String keyword;
    private final String order;
    private final String length;
    private final String partition;

    public SearchResultVideoParser(String keyword, String order, String length, String partition) {
        this.keyword = keyword;
        this.order = order;
        this.length = length;
        this.partition = partition;
    }

    @Override
    public List<SearchResultVideo> parseData() {
        Map<String, String> params = new HashMap<>(6);
        params.put("search_type", "video");
        params.put("page", String.valueOf(pageNum));
        params.put("order", order == null ? "" : order);
        params.put("keyword", keyword);
        params.put("duration", length == null ? "" : length);
        params.put("tids", partition == null ? "" : partition);

        if (dataStatus) {
            JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.SEARCH_WITH_TYPE, params);
            JSONObject data = response.getJSONObject("data");

            if (maxItems == -1 || maxPages == -1) {
                maxItems = data.getIntValue("numResults");
                maxPages = data.getIntValue("numPages");
            }

            JSONArray jsonArray = data.getJSONArray("result");
            if (jsonArray == null || jsonArray.size() == 0) {
                dataStatus = false;
                return null;
            }

            List<SearchResultVideo> searchResultVideoList = new ArrayList<>(jsonArray.size());
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                SearchResultVideo searchResultVideo = new SearchResultVideo();

                searchResultVideo.aid = jsonObject.getString("aid");
                searchResultVideo.bvid = jsonObject.getString("bvid");
                searchResultVideo.duration = jsonObject.getString("duration");
                searchResultVideo.cover = "https:" + jsonObject.getString("pic");

                searchResultVideo.title = jsonObject.getString("title")
                        .replaceAll("<em class=\"keyword\">", "<p style=\"color: #fb7299\">")
                        .replaceAll("</em>", "</p>");
                searchResultVideo.userName = jsonObject.getString("author");
                searchResultVideo.play = ValueUtils.generateCN(jsonObject.getIntValue("play"));
                searchResultVideo.danmaku = ValueUtils.generateCN(jsonObject.getIntValue("video_review"));

                searchResultVideoList.add(searchResultVideo);
            }

            currentItems += searchResultVideoList.size();

            if (pageNum == maxPages && currentItems == maxItems) {
                dataStatus = false;
            }

            pageNum ++;
            return searchResultVideoList;
        }

        return null;
    }
}
