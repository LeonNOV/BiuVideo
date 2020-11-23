package com.leon.biuvideo.utils.searchParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.articleBeans.Article;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.OrderType;
import com.leon.biuvideo.utils.Paths;
import com.leon.biuvideo.utils.SearchType;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleParser {
    public List<Article> articleParse(String keyword, int pn, OrderType orderType) {
        Map<String, String> params = new HashMap<>();
        try {
            //进行URL编码
            String keywordCoded = URLEncoder.encode(keyword, "utf-8");
            params.put("keyword", keywordCoded);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        params.put("search_type", SearchType.ARTICLE.value);
        params.put("page", String.valueOf(pn));
        params.put("order", orderType.value);

        HttpUtils httpUtils = new HttpUtils(Paths.search, params);
        String response = httpUtils.getData();

        JSONObject responseObject = JSONObject.parseObject(response);

        List<Article> articles = new ArrayList<>();

        JSONObject data = responseObject.getJSONObject("data");
        if (data != null) {
            articles = parseData(data);
        }

        return articles;
    }

    private List<Article> parseData(JSONObject data) {
        JSONArray result = data.getJSONArray("result");

        List<Article> articles;
        if (result.size() != 0) {
            articles = new ArrayList<>();

            for (Object o : result) {
                Article article = new Article();
                JSONObject jsonObject = (JSONObject) o;

                //获取上传时间
                article.ctime = jsonObject.getLongValue("pub_time");

                //获取点赞数
                article.like = jsonObject.getIntValue("like");

                //获取标题
                article.title = jsonObject.getString("title");

                //获取封面url
                article.coverUrl = jsonObject.getJSONArray("image_urls").getString(0);

                //获取阅读数
                article.view = jsonObject.getIntValue("view");

                //获取评论数
                article.reply = jsonObject.getIntValue("reply");

                //获取文章摘要
                article.summary = jsonObject.getString("desc");

                //获取文章ID
                article.articleID = jsonObject.getLongValue("id");

                //获取文章分类标签
                article.category = jsonObject.getString("category_name");

                articles.add(article);
            }

            return articles;
        }

        return null;
    }

    public static int getSearchVideoCount(String keyword) {
        int count = -1;

        Map<String, String> params = new HashMap<>();
        try {
            //进行URL编码
            String keywordCoded = URLEncoder.encode(keyword, "utf-8");
            params.put("keyword", keywordCoded);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        params.put("search_type", SearchType.ARTICLE.value);
        params.put("page", "1");
        params.put("order", OrderType.DEFAULT.value);

        HttpUtils httpUtils = new HttpUtils(Paths.search, params);
        String response = httpUtils.getData();

        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONObject data = jsonObject.getJSONObject("data");

        if (data != null) {
            count = data.getIntValue("numResults");
        }

        return count;
    }
}
