package com.leon.biuvideo.utils.parseDataUtils.searchParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.searchResultBeans.SearchResultBiliUser;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserInterface;
import com.leon.biuvideo.values.Role;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2020/11/22
 * @Desc 用户搜索结果解析类
 */
public class SearchResultBiliUserParser implements ParserInterface<SearchResultBiliUser> {
    /**
     * 页码数
     */
    private int pageNum = 1;

    /**
     * 数据状态
     */
    private boolean dataStatus = true;

    /**
     * 总页面数
     */
    private int maxPages = -1;

    /**
     * 总条目数
     */
    private int maxItems = -1;

    /**
     * 当前条目数
     */
    private int currentItems = 0;

    private final String keyword;
    private final String order;
    private final String orderSort;
    private final String userType;

    public SearchResultBiliUserParser(String keyword, String order, String orderSort, String userType) {
        this.keyword = keyword;
        this.order = order;
        this.orderSort = orderSort;
        this.userType = userType;
    }

    @Override
    public List<SearchResultBiliUser> parseData() {
        Map<String, String> params = new HashMap<>(6);
        params.put("search_type", "bili_user");
        params.put("page", String.valueOf(pageNum));
        params.put("order", order == null ? "" : order);
        params.put("order_sort", orderSort == null ? "" : orderSort);
        params.put("user_type", userType == null ? "" : userType);
        params.put("keyword", keyword);

        if (dataStatus) {
            JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.SEARCH_WITH_TYPE, params);
            JSONObject data = response.getJSONObject("data");

            if (maxItems == -1 || maxPages == -1) {
                maxItems = data.getIntValue("numResults");
                maxPages = data.getIntValue("numPages");
            }

            JSONArray jsonArray = data.getJSONArray("result");
            List<SearchResultBiliUser> searchResultBiliUserList = new ArrayList<>(jsonArray.size());
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                SearchResultBiliUser searchResultBiliUser = new SearchResultBiliUser();

                searchResultBiliUser.mid = jsonObject.getString("mid");
                searchResultBiliUser.userName = jsonObject.getString("uname");
                searchResultBiliUser.userSign = jsonObject.getString("usign");
                searchResultBiliUser.userFace = "https:" + jsonObject.getString("upic");

                JSONObject officialVerify = jsonObject.getJSONObject("official_verify");
                int type = officialVerify.getIntValue("type");
                searchResultBiliUser.role = type == 0 ? Role.PERSON : type == 1 ? Role.OFFICIAL : Role.NONE;

                searchResultBiliUserList.add(searchResultBiliUser);
            }

            currentItems += searchResultBiliUserList.size();
            pageNum ++;

            if (pageNum == maxPages && currentItems == maxItems) {
                dataStatus = false;
            }

            return searchResultBiliUserList;
        }

        return null;
    }
}
