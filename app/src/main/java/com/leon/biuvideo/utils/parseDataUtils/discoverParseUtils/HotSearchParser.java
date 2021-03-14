package com.leon.biuvideo.utils.parseDataUtils.discoverParseUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.discoverBeans.HotSearch;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParseInterface;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/3/14
 * @Desc 热搜榜数据解析类
 */
public class HotSearchParser implements ParseInterface<HotSearch> {
    @Override
    public List<HotSearch> parseData() {
        Map<String, String> params = new HashMap<>(2);
        params.put("build", "0");
        params.put("limit", "10");

        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.HOT_SEARCH_LIST, params);
        JSONArray hotSearchKeywords = ((JSONObject) response.getJSONArray("data").get(0)).getJSONObject("data").getJSONArray("list");

        List<HotSearch> hotSearchList = new ArrayList<>(hotSearchKeywords.size());
        for (Object o : hotSearchKeywords) {
            JSONObject jsonObject = (JSONObject) o;
            HotSearch hotSearch = new HotSearch();

            hotSearch.position = jsonObject.getIntValue("position");
            hotSearch.showName = jsonObject.getString("show_name");
            hotSearch.keyword = jsonObject.getString("keyword");
            hotSearch.icon = jsonObject.getString("icon");

            hotSearchList.add(hotSearch);
        }

        // 根据成员`position`进行排序
        hotSearchList.sort(new Comparator<HotSearch>() {
            @Override
            public int compare(HotSearch o1, HotSearch o2) {
                return o1.position - o2.position;
            }
        });

        return hotSearchList;
    }
}
