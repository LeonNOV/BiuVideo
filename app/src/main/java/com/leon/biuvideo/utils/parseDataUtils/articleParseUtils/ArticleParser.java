package com.leon.biuvideo.utils.parseDataUtils.articleParseUtils;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.homeBeans.favoriteBeans.FavoriteArticle;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

public class ArticleParser {
    private long mid;

    /**
     * construct
     *
     * @param context   context
     * @param mid   用户id
     */
    public ArticleParser(Context context, long mid) {
        this.mid = mid;
    }

    /**
     * 解析article接口
     * @param pn    页码
     * @return  返回Article集合
     */
    public List<FavoriteArticle> parseArticle (int pn) {
        Map<String, String> params = new HashMap<>();
        params.put("mid", String.valueOf(this.mid));
        params.put("pn", String.valueOf(pn));
        params.put("ps", "12");

        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.article, Headers.of(HttpUtils.getAPIRequestHeader()), params);
        JSONObject dataObject = responseObject.getJSONObject("data");

        if (dataObject != null) {
            JSONArray articleArray = dataObject.getJSONArray("favoriteArticles");
            List<FavoriteArticle> favoriteArticles = new ArrayList<>();
            for (Object article : articleArray) {
                FavoriteArticle favoriteArticleInfo = getArticleInfo((JSONObject) article);

                if (favoriteArticleInfo != null) {
                    favoriteArticles.add(favoriteArticleInfo);
                }
            }

            return favoriteArticles;
        }

        return null;
    }

    /**
     * 获取文章信息
     *
     * @param articleObject JSON对象
     * @return  返回Article对象
     */
    private FavoriteArticle getArticleInfo(JSONObject articleObject) {
        if (articleObject != null) {
            FavoriteArticle favoriteArticle = new FavoriteArticle();

            //获取文章id
            favoriteArticle.articleId = articleObject.getLongValue("id");

            //获取文章分类
            favoriteArticle.category = articleObject.getJSONObject("category").getString("name");

            //获取文章标题
            favoriteArticle.title = articleObject.getString("title");

            //获取文章摘要
            favoriteArticle.summary = articleObject.getString("summary");

            JSONObject author = articleObject.getJSONObject("author");

            //获取作者face
            favoriteArticle.face = author.getString("face");

            //获取作者mid
            favoriteArticle.mid = author.getLongValue("mid");

            //获取文章作者
            favoriteArticle.author = author.getString("name");

            //获取文章封面
            //如果banner_url的值为空则获取image_urls中的第一个链接
            String banner_url = articleObject.getString("banner_url");
            if (!banner_url.equals("")) {
                favoriteArticle.coverUrl = banner_url;
            } else {
                favoriteArticle.coverUrl = articleObject.getJSONArray("image_urls").get(0).toString();
            }

            //获取创建时间
            favoriteArticle.ctime = articleObject.getLongValue("ctime");

            //获取文章相关信息
            JSONObject stats = articleObject.getJSONObject("stats");

            //获取观看数
            favoriteArticle.view = stats.getIntValue("view");

            //获取收藏数
//            favoriteArticle.favorite = stats.getIntValue("favorite");

            //获取点赞数
            favoriteArticle.like = stats.getIntValue("like");

            //获取评论数
            favoriteArticle.reply = stats.getIntValue("replay");

            //获取分享数
//            favoriteArticle.share = stats.getIntValue("share");

            //获取投币数
//            favoriteArticle.coin = stats.getIntValue("coin");

            return favoriteArticle;
        }

        return null;
    }

    /**
     * 获取单个专栏信息
     *
     * @param articleId     专栏ID
     * @return      返回Article
     */
    public FavoriteArticle getArticle(long articleId) {
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(articleId));

        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.articleInfo, Headers.of(HttpUtils.getAPIRequestHeader()), params);
        JSONObject dataObject = responseObject.getJSONObject("data");

        if (dataObject != null) {
            FavoriteArticle favoriteArticle = new FavoriteArticle();

            favoriteArticle.articleId = articleId;
            favoriteArticle.mid = dataObject.getLongValue("mid");
            favoriteArticle.author = dataObject.getString("author_name");

            JSONObject stats = dataObject.getJSONObject("stats");
            favoriteArticle.view = stats.getIntValue("view");
            favoriteArticle.like = stats.getIntValue("like");
            favoriteArticle.reply = stats.getIntValue("reply");

            return favoriteArticle;
        }

        return null;
    }

    /**
     * 获取总条目数
     *
     * @return  返回文章总条目数
     */
    public int getArticleTotal() {
        Map<String, String> params = new HashMap<>();
        params.put("mid", String.valueOf(this.mid));
        params.put("pn", "1");

        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.article, params);

        return responseObject.getJSONObject("data").getIntValue("count");
    }
}
