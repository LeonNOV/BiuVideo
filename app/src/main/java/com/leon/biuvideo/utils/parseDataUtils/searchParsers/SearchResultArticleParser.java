package com.leon.biuvideo.utils.parseDataUtils.searchParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.searchResultBeans.SearchResultArticle;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserInterface;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2020/11/22
 * @Desc 专栏搜索结果解析类
 */
public class SearchResultArticleParser implements ParserInterface<SearchResultArticle> {
    /**
     * 页码数
     */
    private int pageNum = 1;

    /**
     * 数据状态
     */
    public boolean dataStatus = true;

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
    private final String categoryId;

    public SearchResultArticleParser(String keyword, String order, String categoryId) {
        this.keyword = keyword;
        this.order = order;
        this.categoryId = categoryId;
    }

    @Override
    public List<SearchResultArticle> parseData() {
        Map<String, String> params = new HashMap<>(2);
        params.put("keyword", keyword);
        params.put("order", order);
        params.put("category_id", categoryId);
        params.put("search_type", "article");

        if (dataStatus) {
            JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.SEARCH_WITH_TYPE, params);
            JSONObject data = response.getJSONObject("data");

            if (maxItems == -1 || maxPages == -1) {
                maxItems = data.getIntValue("numResults");
                maxPages = data.getIntValue("numPages");
            }

            JSONArray jsonArray = data.getJSONArray("result");
            List<SearchResultArticle> searchResultArticleList = new ArrayList<>(jsonArray.size());
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                SearchResultArticle searchResultArticle = new SearchResultArticle();

                searchResultArticle.articleId = jsonObject.getString("id");
                searchResultArticle.title = jsonObject.getString("title")
                        .replaceAll("<em class=\"keyword\">", "")
                        .replaceAll("</em>", "");

                JSONArray imageUrls = jsonObject.getJSONArray("image_urls");
                searchResultArticle.images = new String[imageUrls.size()];
                for (int i = 0; i < imageUrls.size(); i++) {
                    searchResultArticle.images[i] = "https:" + imageUrls.get(i);
                }

                searchResultArticle.view = ValueUtils.generateCN(jsonObject.getIntValue("view"));
                searchResultArticle.like = ValueUtils.generateCN(jsonObject.getIntValue("like"));
                searchResultArticle.comment = ValueUtils.generateCN(jsonObject.getIntValue("reply"));

                searchResultArticleList.add(searchResultArticle);
            }

            currentItems += searchResultArticleList.size();
            pageNum ++;

            if (pageNum == maxPages && currentItems == maxItems) {
                dataStatus = false;
            }

            return getArticleAutherNames(searchResultArticleList);
        }

        return null;
    }

    /**
     * 获取专栏作者名称
     *
     * @param searchResultArticleList   searchResultArticleList
     * @return  searchResultArticleList
     */
    private List<SearchResultArticle> getArticleAutherNames (List<SearchResultArticle> searchResultArticleList) {
        Map<String, String> params = new HashMap<>(1);

        StringBuilder ids = new StringBuilder();
        for (SearchResultArticle searchResultArticle : searchResultArticleList) {
            ids.append(searchResultArticle.articleId).append("%2C");
        }

        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.METAS, params);
        JSONObject data = response.getJSONObject("data");
        Iterator<Map.Entry<String, Object>> iterator = data.entrySet().iterator();

        for (SearchResultArticle searchResultArticle : searchResultArticleList) {
            JSONObject jsonObject = data.getJSONObject(searchResultArticle.articleId);
            searchResultArticle.userName = jsonObject.getJSONObject("author").getString("name");
        }

        return searchResultArticleList;
    }
}
