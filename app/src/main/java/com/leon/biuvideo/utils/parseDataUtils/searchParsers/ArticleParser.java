package com.leon.biuvideo.utils.parseDataUtils.searchParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.homeBeans.favoriteBeans.FavoriteArticle;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;
import com.leon.biuvideo.values.SearchType;
import com.leon.biuvideo.values.SortType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * 解析搜索结果-专栏数据
 */
public class ArticleParser {
    private final Map<String, String> requestHeader;

    public ArticleParser() {
        this.requestHeader = HttpUtils.getAPIRequestHeader("Referer", "https://search.bilibili.com");
    }

    /**
     * 获取专栏列表
     *
     * @param keyword   关键字
     * @param pn    页码
     * @param sortType 排序方式
     * @return  返回专栏数据
     */
    public List<FavoriteArticle> articleParse(String keyword, int pn, SortType sortType) {
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("search_type", SearchType.ARTICLE.value);
        params.put("page", String.valueOf(pn));
        params.put("order", sortType.value);

        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.search, Headers.of(requestHeader), params);
        JSONObject data = responseObject.getJSONObject("data");

        List<FavoriteArticle> favoriteArticles = new ArrayList<>();
        if (data != null) {
            favoriteArticles = parseData(data);
        }

        return favoriteArticles;
    }

    /**
     * 解析JSONObject类型的数据
     *
     * @param data  JSONObject对象
     * @return  返回解析结果
     */
    private List<FavoriteArticle> parseData(JSONObject data) {
        JSONArray result = data.getJSONArray("result");

        List<FavoriteArticle> favoriteArticles;
        if (result.size() != 0) {
            favoriteArticles = new ArrayList<>();

            for (Object o : result) {
                FavoriteArticle favoriteArticle = new FavoriteArticle();
                JSONObject jsonObject = (JSONObject) o;

                //获取上传时间
                favoriteArticle.ctime = jsonObject.getLongValue("pub_time");

                //获取点赞数
                favoriteArticle.like = jsonObject.getIntValue("like");

                //获取标题
                favoriteArticle.title = jsonObject.getString("title").replaceAll("<em class=\"keyword\">", "").replaceAll("</em>", "");

                //获取封面url
                favoriteArticle.coverUrl = "http://" + jsonObject.getJSONArray("image_urls").getString(0);

                //获取阅读数
                favoriteArticle.view = jsonObject.getIntValue("view");

                //获取评论数
                favoriteArticle.reply = jsonObject.getIntValue("reply");

                //获取文章摘要
                favoriteArticle.summary = jsonObject.getString("desc");

                //获取文章ID
                favoriteArticle.articleId = jsonObject.getLongValue("id");

                //获取文章分类标签
                favoriteArticle.category = jsonObject.getString("category_name");

                favoriteArticles.add(favoriteArticle);
            }

            //获取详细信息
            List<FavoriteArticle> articlesWhitDetails = gteArticleDetail(favoriteArticles);

            return articlesWhitDetails;
        }

        return null;
    }

    /**
     * 将Article详细信息添加至articles中
     *
     * @param favoriteArticles  无face、name、mid的article集合
     * @return  返回含有face、name、mid的article结合
     */
    private List<FavoriteArticle> gteArticleDetail(List<FavoriteArticle> favoriteArticles) {

        //获取articleID
        StringBuilder ids = new StringBuilder();
        String separator = ",";

        for (FavoriteArticle favoriteArticle : favoriteArticles) {
            ids.append(favoriteArticle.articleId).append(separator);
        }

        int length = ids.length();
        ids.delete(length - 1, length);

        Map<String, Object> details = new HashMap<>();

        //获取article详细信息
        Map<String, String> params = new HashMap<>();
        params.put("ids", ids.toString());

        HttpUtils httpUtils = new HttpUtils(BiliBiliAPIs.metas, Headers.of("Referer", "https://search.bilibili.com"), params);
        String response = httpUtils.getData();

        JSONObject responseObject = JSONObject.parseObject(response);
        JSONObject data = responseObject.getJSONObject("data");
        if (data != null) {
            Map<String, Object> innerMap = data.getInnerMap();

            details = innerMap;
        }

        //将对应详细信息放入对应Article对象中
        for (FavoriteArticle favoriteArticle : favoriteArticles) {
            JSONObject articleObject = (JSONObject) details.get(String.valueOf(favoriteArticle.articleId));

            //获取author对象
            JSONObject authorObject = articleObject.getJSONObject("author");

            //获取face
            favoriteArticle.face = authorObject.getString("face");

            //获取name
            favoriteArticle.author = authorObject.getString("name");

            //获取mid
            favoriteArticle.mid = authorObject.getLongValue("mid");
        }

        return favoriteArticles;
    }

    /**
     * 获取结果个数
     *
     * @param keyword   关键字
     * @return  返回搜索结果个数
     */
    public int getSearchArticleCount(String keyword) {
        int count = -1;

        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("search_type", SearchType.ARTICLE.value);
        params.put("page", "1");
        params.put("order", SortType.DEFAULT.value);

        HttpUtils httpUtils = new HttpUtils(BiliBiliAPIs.search, Headers.of(requestHeader), params);
        String response = httpUtils.getData();

        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONObject data = jsonObject.getJSONObject("data");

        if (data != null) {
            count = data.getIntValue("numResults");
        }

        return count;
    }
}
