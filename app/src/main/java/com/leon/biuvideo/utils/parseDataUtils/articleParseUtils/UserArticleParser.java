package com.leon.biuvideo.utils.parseDataUtils.articleParseUtils;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.articleBeans.Article;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

public class UserArticleParser {

    /**
     * 使用Cookie获取用户收藏的所有专栏
     *
     * @param pageNum       页码
     * @return      返回Article集合
     */
    public List<Article> parseArticle (int pageNum) {
        Map<String, String> params = new HashMap<>();
        params.put("pn", String.valueOf(pageNum));
        params.put("ps", "16");

        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.userArticle, Headers.of(HttpUtils.getAPIRequestHeader()), params);
        JSONObject dataObject = responseObject.getJSONObject("data");

        if (dataObject != null) {
            JSONArray favorites = dataObject.getJSONArray("favorites");

           return parseJSONArray(favorites);
        }

        return null;
    }

    private List<Article> parseJSONArray(JSONArray favorites) {
        List<Article> articles = new ArrayList<>();

        for (Object favorite : favorites) {
            JSONObject jsonObject = (JSONObject) favorite;
            Article article = new Article();

            JSONObject author = jsonObject.getJSONObject("author");
            article.mid = author.getLongValue("mid");
            article.face = author.getString("face");
            article.author = author.getString("name");

            article.articleId = jsonObject.getLongValue("id");
            article.title = jsonObject.getString("title");
            article.summary = jsonObject.getString("summary");
            article.coverUrl = jsonObject.getString("banner_url");
            article.category = jsonObject.getJSONObject("category").getString("name");
            article.ctime = jsonObject.getLongValue("ctime");

            JSONObject stats = jsonObject.getJSONObject("stats");
            article.view = stats.getIntValue("view");
            article.like = stats.getIntValue("like");
            article.reply = stats.getIntValue("reply");

            articles.add(article);
        }

        return articles;
    }

    /**
     * 获取用户专栏收藏总数
     *
     * @return      返回收藏的专栏总数
     */
    public int getTotal() {
        Map<String, String> params = new HashMap<>();
        params.put("pn", "1");
        params.put("ps", "16");

        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.userArticle, Headers.of(HttpUtils.getAPIRequestHeader()), params);
        JSONObject dataObject = responseObject.getJSONObject("data");

        if (dataObject != null) {
            JSONObject page = dataObject.getJSONObject("page");
            return page.getIntValue("total");
        }

        return 0;
    }
}
