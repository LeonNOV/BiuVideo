package com.leon.biuvideo.utils.parseDataUtils.articleParseUtils;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.articleBeans.Article;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

public class ArticleParser {
    private final Map<String, String> requestHeader;
    private long mid;

    public ArticleParser(Context context) {
        this.requestHeader = ParserUtils.getInterfaceRequestHeader(context);
    }

    /**
     * construct
     *
     * @param context   context
     * @param mid   用户id
     */
    public ArticleParser(Context context, long mid) {
        this.mid = mid;
        this.requestHeader = ParserUtils.getInterfaceRequestHeader(context);
    }

    /**
     * 解析article接口
     * @param pn    页码
     * @return  返回Article集合
     */
    public List<Article> parseArticle (int pn) {
        Map<String, String> params = new HashMap<>();
        params.put("mid", String.valueOf(this.mid));
        params.put("pn", String.valueOf(pn));
        params.put("ps", "12");

        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.article, Headers.of(requestHeader), params);
        JSONObject dataObject = responseObject.getJSONObject("data");

        if (dataObject != null) {
            JSONArray articleArray = dataObject.getJSONArray("articles");
            List<Article> articles = new ArrayList<>();
            for (Object article : articleArray) {
                Article articleInfo = getArticleInfo((JSONObject) article);

                if (articleInfo != null) {
                    articles.add(articleInfo);
                }
            }

            return articles;
        }

        return null;
    }

    /**
     * 获取文章信息
     *
     * @param articleObject JSON对象
     * @return  返回Article对象
     */
    private Article getArticleInfo(JSONObject articleObject) {
        if (articleObject != null) {
            Article article = new Article();

            //获取文章id
            article.articleId = articleObject.getLongValue("id");

            //获取文章分类
            article.category = articleObject.getJSONObject("category").getString("name");

            //获取文章标题
            article.title = articleObject.getString("title");

            //获取文章摘要
            article.summary = articleObject.getString("summary");

            JSONObject author = articleObject.getJSONObject("author");

            //获取作者face
            article.face = author.getString("face");

            //获取作者mid
            article.mid = author.getLongValue("mid");

            //获取文章作者
            article.author = author.getString("name");

            //获取文章封面
            //如果banner_url的值为空则获取image_urls中的第一个链接
            String banner_url = articleObject.getString("banner_url");
            if (!banner_url.equals("")) {
                article.coverUrl = banner_url;
            } else {
                article.coverUrl = articleObject.getJSONArray("image_urls").get(0).toString();
            }

            //获取创建时间
            article.ctime = articleObject.getLongValue("ctime");

            //获取文章相关信息
            JSONObject stats = articleObject.getJSONObject("stats");

            //获取观看数
            article.view = stats.getIntValue("view");

            //获取收藏数
//            article.favorite = stats.getIntValue("favorite");

            //获取点赞数
            article.like = stats.getIntValue("like");

            //获取评论数
            article.reply = stats.getIntValue("replay");

            //获取分享数
//            article.share = stats.getIntValue("share");

            //获取投币数
//            article.coin = stats.getIntValue("coin");

            return article;
        }

        return null;
    }

    /**
     * 获取单个专栏信息
     *
     * @param articleId     专栏ID
     * @return      返回Article
     */
    public Article getArticle(long articleId) {
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(articleId));

        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.articleInfo, Headers.of(requestHeader), params);
        JSONObject dataObject = responseObject.getJSONObject("data");

        if (dataObject != null) {
            Article article = new Article();

            article.articleId = articleId;
            article.mid = dataObject.getLongValue("mid");
            article.author = dataObject.getString("author_name");

            JSONObject stats = dataObject.getJSONObject("stats");
            article.view = stats.getIntValue("view");
            article.like = stats.getIntValue("like");
            article.reply = stats.getIntValue("reply");

            return article;
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
