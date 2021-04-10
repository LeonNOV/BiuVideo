package com.leon.biuvideo.utils.parseDataUtils.biliUserResourcesParseUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.biliUserResourcesBeans.BiliUserArticle;
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
 * @Time 2021/4/10
 * @Desc
 */
public class BiliUserArticlesParser implements ParserInterface<BiliUserArticle> {
    /**
     * 最新发布
     */
    public static final String ORDER_DEFAULT = "publish_time";

    /**
     * 最多阅读
     */
    public static final String ORDER_READ = "view";

    /**
     * 最多收藏
     */
    public static final String ORDER_FAV = "fav";

    private static final String PAGE_SIZE = "12";
    private int pageNum = 1;
    private int total = -1;
    private int currentCount = 0;
    public boolean dataStatus = true;

    private final String mid;
    private final String order;

    public BiliUserArticlesParser(String mid, String order) {
        this.mid = mid;
        this.order = order;
    }

    @Override
    public List<BiliUserArticle> parseData() {
        Map<String, String> params = new HashMap<>(4);
        params.put("mid", mid);
        params.put("pn", String.valueOf(pageNum));
        params.put("ps", PAGE_SIZE);
        params.put("sort", order);

        if (dataStatus) {
            JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.BILI_USER_ARTICLE, params);
            JSONObject data = response.getJSONObject("data");

            if (total == -1) {
                total = data.getIntValue("count");
                if (total == 0) {
                    return null;
                }
            }

            JSONArray jsonArray = data.getJSONArray("articles");
            List<BiliUserArticle> biliUserArticleList = new ArrayList<>(jsonArray.size());
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                BiliUserArticle biliUserArticle = new BiliUserArticle();

                biliUserArticle.id = jsonObject.getString("id");
                biliUserArticle.title = jsonObject.getString("title");
                biliUserArticle.cover = (String) jsonObject.getJSONArray("image_urls").get(0);
                biliUserArticle.summary = jsonObject.getString("summary");

                JSONObject stats = jsonObject.getJSONObject("stats");
                biliUserArticle.view = ValueUtils.generateCN(stats.getIntValue("view"));
                biliUserArticle.like = ValueUtils.generateCN(stats.getIntValue("like"));
                biliUserArticle.comment = ValueUtils.generateCN(stats.getIntValue("reply"));

                biliUserArticleList.add(biliUserArticle);
            }

            currentCount += biliUserArticleList.size();

            if (currentCount == total) {
                dataStatus = false;
            }

            pageNum ++;

            return biliUserArticleList;
        }

        return null;
    }
}
