package com.leon.biuvideo.utils.articleParseUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.articleBeans.Article;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

public class ArticleParseUtils {

    /**
     * 解析article接口
     * @param mid   用户id
     * @param pn    页码
     * @return  返回Article集合
     */
    public static List<Article> parseArticle (long mid, int pn) {
        Map<String, String> params = new HashMap<>();
        params.put("mid", String.valueOf(mid));
        params.put("pn", String.valueOf(pn));

        HttpUtils httpUtils = new HttpUtils(Paths.article, params);
        String response = httpUtils.getData();

        JSONObject responseObject = JSON.parseObject(response);

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
    private static Article getArticleInfo(JSONObject articleObject) {
        if (articleObject != null) {
            Article article = new Article();

            //获取文章id
            article.articleID = articleObject.getLongValue("id");

            //获取文章分类
            article.category = articleObject.getJSONObject("category").getString("name");

            //获取文章标题
            article.title = articleObject.getString("title");

            //获取文章摘要
            article.summary = articleObject.getString("summary");

            //获取文章作者
            article.author = articleObject.getJSONObject("author").getString("name");

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
            article.favorite = stats.getIntValue("favorite");

            //获取点赞数
            article.like = stats.getIntValue("like");

            //获取评论数
            article.replay = stats.getIntValue("replay");

            //获取分享数
            article.share = stats.getIntValue("share");

            //获取投币数
            article.coin = stats.getIntValue("coin");

            return article;
        }

        return null;
    }

    /**
     * 获取总条目数
     *
     * @param mid   用户ID
     * @return  返回文章总条目数
     */
    public static int getCount(long mid) {
        Map<String, String> params = new HashMap<>();
        params.put("mid", String.valueOf(mid));
        params.put("pn", "1");

        HttpUtils httpUtils = new HttpUtils(Paths.article, Headers.of(HttpUtils.getHeaders()), params);
        String response = httpUtils.getData();

        JSONObject responseObject = JSON.parseObject(response);

        return responseObject.getJSONObject("data").getIntValue("count");
    }
}
