package com.leon.biuvideo.utils.parseDataUtils.resourcesParsers;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.resourcesBeans.ArticleInfo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2021/4/12
 * @Desc 专栏信息解析类
 */
public class ArticleInfoParser {
    public static ArticleInfo parseData (String articleId) {
        Map<String, String> params = new HashMap<>(1);
        params.put("id", articleId);

        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.ARTICLE_INFO, Headers.of(HttpUtils.getAPIRequestHeader()), params);
        JSONObject data = response.getJSONObject("data");

        ArticleInfo articleInfo = new ArticleInfo();

        articleInfo.title = data.getString("title");

        String bannerUrl = data.getString("banner_url");
        if ("".equals(bannerUrl)) {
            articleInfo.banner = (String) data.getJSONArray("image_urls").get(0);
        } else {
            articleInfo.banner = bannerUrl;
        }

        articleInfo.mid = data.getString("mid");
        articleInfo.likeStatus = data.getIntValue("like") == 1;
        articleInfo.favoriteStatus = data.getBooleanValue("favorite");
        articleInfo.attentionStatus = data.getBooleanValue("attention");

        JSONObject stats = data.getJSONObject("stats");
        articleInfo.view = ValueUtils.generateCN(stats.getIntValue("view"));
        articleInfo.favorite = ValueUtils.generateCN(stats.getIntValue("favorite"));
        articleInfo.like = ValueUtils.generateCN(stats.getIntValue("like"));
        articleInfo.comment = ValueUtils.generateCN(stats.getIntValue("reply"));

        return articleInfo;
    }
}
