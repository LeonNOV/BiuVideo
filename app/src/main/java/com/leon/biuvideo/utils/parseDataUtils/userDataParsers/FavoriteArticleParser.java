package com.leon.biuvideo.utils.parseDataUtils.userDataParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.homeBeans.favoriteBeans.FavoriteArticle;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserInterface;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2020/12/15
 * @Desc 专栏收藏数据解析类
 */
public class FavoriteArticleParser implements ParserInterface<FavoriteArticle> {
    /**
     * 页码数
     */
    private int pageNum = 1;

    /**
     * 单页条目数
     */
    private static final int PAGE_SIZE = 16;

    /**
     * 数据状态
     */
    public boolean dataStatus = true;

    @Override
    public List<FavoriteArticle> parseData() {
        Map<String, String> params = new HashMap<>(2);
        params.put("pn", String.valueOf(pageNum));
        params.put("ps", String.valueOf(PAGE_SIZE));

        if (dataStatus) {
            JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.USER_ARTICLE, Headers.of(HttpUtils.getAPIRequestHeader()), params);
            JSONObject dataObject = responseObject.getJSONObject("data");

            if (dataObject != null) {
                JSONArray favorites = dataObject.getJSONArray("favorites");
                List<FavoriteArticle> favoriteArticles = new ArrayList<>(favorites.size());

                for (Object favorite : favorites) {
                    JSONObject jsonObject = (JSONObject) favorite;
                    FavoriteArticle favoriteArticle = new FavoriteArticle();

                    JSONObject author = jsonObject.getJSONObject("author");
                    favoriteArticle.mid = author.getLongValue("mid");
                    favoriteArticle.face = author.getString("face");
                    favoriteArticle.author = author.getString("name");

                    favoriteArticle.articleId = jsonObject.getLongValue("id");
                    favoriteArticle.title = jsonObject.getString("title");
                    favoriteArticle.summary = jsonObject.getString("summary");
                    favoriteArticle.coverUrl = jsonObject.getString("banner_url");
                    favoriteArticle.category = jsonObject.getJSONObject("category").getString("name");
                    favoriteArticle.ctime = jsonObject.getLongValue("ctime");

                    JSONObject stats = jsonObject.getJSONObject("stats");
                    favoriteArticle.view = stats.getIntValue("view");
                    favoriteArticle.like = stats.getIntValue("like");
                    favoriteArticle.reply = stats.getIntValue("reply");

                    favoriteArticles.add(favoriteArticle);
                }

                if (favoriteArticles.size() < PAGE_SIZE) {
                    dataStatus = false;
                }

                pageNum ++;

                return favoriteArticles;
            }
        }

        return null;
    }
}
