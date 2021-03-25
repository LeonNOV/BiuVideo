package com.leon.biuvideo.utils.parseDataUtils.homeParseUtils.popularParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.homeBeans.popularBeans.PopularWeeklySeries;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserInterface;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2021/3/24
 * @Desc 每周必看往期数据解析类
 */
public class PopularWeeklySeriesParser implements ParserInterface<PopularWeeklySeries> {
    @Override
    public List<PopularWeeklySeries> parseData() {
        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.POPULAR_WEEKLY_SERIES, Headers.of(HttpUtils.getAPIRequestHeader()), null);
        JSONArray seriesList = response.getJSONObject("data").getJSONArray("list");

        List<PopularWeeklySeries> popularWeeklySeriesList = new ArrayList<>(seriesList.size());
        for (Object o : seriesList) {
            JSONObject jsonObject = (JSONObject) o;

            PopularWeeklySeries popularWeeklySeries = new PopularWeeklySeries();

            popularWeeklySeries.number = jsonObject.getIntValue("number");
            popularWeeklySeries.name = jsonObject.getString("name");
            popularWeeklySeries.subject = jsonObject.getString("subject");

            popularWeeklySeriesList.add(popularWeeklySeries);
        }

        return popularWeeklySeriesList;
    }
}
