package com.leon.biuvideo.utils.parseDataUtils.searchParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.searchResultBeans.SearchResultBangumi;
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
 * @Time 2021/3/30
 * @Desc 番剧搜索结果解析类
 */
public class SearchResultBangumiParser extends ParserInterface<SearchResultBangumi> {
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

    public SearchResultBangumiParser(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public List<SearchResultBangumi> parseData() {
        Map<String, String> params = new HashMap<>(3);
        params.put("search_type", "media_bangumi");
        params.put("page", String.valueOf(pageNum));
        params.put("keyword", keyword);

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

            List<SearchResultBangumi> searchResultBangumiList = new ArrayList<>(jsonArray.size());
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                SearchResultBangumi searchResultBangumi = new SearchResultBangumi();

                searchResultBangumi.mediaId = jsonObject.getString("media_id");
                searchResultBangumi.seasonId = jsonObject.getString("season_id");
                searchResultBangumi.cover = "https:" + jsonObject.getString("cover");

                String angleTitle = jsonObject.getString("angle_title");
                searchResultBangumi.badge = "".equals(angleTitle) ? null : angleTitle;

                searchResultBangumi.title = jsonObject.getString("title")
                        .replaceAll("<em class=\"keyword\">", "")
                        .replaceAll("</em>", "");

                searchResultBangumi.pubTime = ValueUtils.generateTime(jsonObject.getLongValue("pubtime"), "yyyy-MM-dd HH:mm", true);
                searchResultBangumi.areas = jsonObject.getString("areas");
                searchResultBangumi.styles = jsonObject.getString("styles");

                JSONObject mediaScore = jsonObject.getJSONObject("media_score");
                if (mediaScore != null) {
                    searchResultBangumi.score = mediaScore.getString("score");
                    searchResultBangumi.userCount = ValueUtils.generateCN(mediaScore.getIntValue("user_count")) + "人评分";
                } else {
                    searchResultBangumi.score = "0";
                    searchResultBangumi.userCount = "暂无评分";
                }

                JSONArray eps = jsonObject.getJSONArray("eps");
                searchResultBangumi.searchResultBangumiEpList = new ArrayList<>(eps.size());
                for (Object object : eps) {
                    JSONObject ep = (JSONObject) object;
                    SearchResultBangumi.SearchResultBangumiEp searchResultBangumiEp = new SearchResultBangumi.SearchResultBangumiEp();

                    searchResultBangumiEp.epId = ep.getString("id");
                    searchResultBangumiEp.title = ep.getString("title");
                    searchResultBangumiEp.longTitle = ep.getString("long_title");

                    JSONArray badges = ep.getJSONArray("badges");
                    if (badges.size() > 0) {
                        String badge = ((JSONObject) badges.get(0)).getString("text");
                        ;
                        searchResultBangumiEp.badge = "".equals(badge) ? null : badge;
                        searchResultBangumiEp.isVip = "会员".equals(badge);
                    } else {
                        searchResultBangumiEp.badge = null;
                        searchResultBangumiEp.isVip = false;
                    }


                    searchResultBangumi.searchResultBangumiEpList.add(searchResultBangumiEp);
                }

                searchResultBangumiList.add(searchResultBangumi);
            }

            currentItems += searchResultBangumiList.size();

            if (pageNum == maxPages && currentItems == maxItems) {
                dataStatus = false;
            }

            pageNum ++;
            return searchResultBangumiList;
        }

        return null;
    }
}
