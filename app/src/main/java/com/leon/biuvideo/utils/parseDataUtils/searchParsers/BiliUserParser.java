package com.leon.biuvideo.utils.parseDataUtils.searchParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.searchResultBeans.bangumi.SearchBiliUser;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.values.Role;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;
import com.leon.biuvideo.values.SearchType;
import com.leon.biuvideo.values.SortType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * 解析搜索结果-用户数据
 */
public class BiliUserParser {
    private final Map<String, String> requestHeader;

    public BiliUserParser() {
        this.requestHeader = HttpUtils.getAPIRequestHeader("Referer", "https://search.bilibili.com");
    }

    /**
     * 获取用户列表
     *
     * @param keyword   关键字
     * @param pn    页码
     * @param sortType 排序方式
     * @return  返回用户数据
     */
    public List<SearchBiliUser> userParse(String keyword, int pn, SortType sortType) {
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("search_type", SearchType.BILI_USER.value);
        params.put("page", String.valueOf(pn));
        params.put("order", sortType.value);

        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.SEARCH_WITH_TYPE, Headers.of(requestHeader), params);
        JSONObject data = responseObject.getJSONObject("data");

        List<SearchBiliUser> searchBiliUsers = new ArrayList<>();
        if (data != null) {
            searchBiliUsers = parseData(data);
        }

        return searchBiliUsers;
    }

    /**
     * 解析JSONObject类型的数据
     *
     * @param data  JSONObject对象
     * @return  返回解析结果
     */
    private List<SearchBiliUser> parseData(JSONObject data) {
        JSONArray result = data.getJSONArray("result");

        List<SearchBiliUser> searchBiliUsers;
        if (result.size() != 0) {
            searchBiliUsers = new ArrayList<>();

            for (Object o : result) {
                SearchBiliUser searchBiliUser = new SearchBiliUser();
                JSONObject jsonObject = (JSONObject) o;

                //用户ID
                searchBiliUser.mid = jsonObject.getLongValue("mid");

                //用户名称
                searchBiliUser.name = jsonObject.getString("uname");

                //用户说明
                searchBiliUser.usign = jsonObject.getString("usign");

                //粉丝数
                searchBiliUser.fans = jsonObject.getIntValue("fans");

                //视频数
                searchBiliUser.videos = jsonObject.getIntValue("videos");

                //头像链接
                searchBiliUser.face = "http://" + jsonObject.getString("upic");

                JSONObject officialVerify = jsonObject.getJSONObject("official_verify");

                int type = officialVerify.getIntValue("type");
                searchBiliUser.role = type > 1 ? Role.NONE : type == 1 ? Role.OFFICIAL : Role.PERSON;
                searchBiliUser.verifyDesc = officialVerify.getString("desc");

                searchBiliUsers.add(searchBiliUser);
            }

            return searchBiliUsers;
        }

        return null;
    }

    /**
     * 获取结果个数
     *
     * @param keyword   关键字
     * @return  返回搜索结果个数
     */
    public int getSearchUserCount(String keyword) {
        int count = -1;

        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("search_type", SearchType.BILI_USER.value);
        params.put("page", "1");
        params.put("order", SortType.DEFAULT.value);

        HttpUtils httpUtils = new HttpUtils(BiliBiliAPIs.SEARCH_WITH_TYPE, Headers.of(requestHeader), params);
        String response = httpUtils.getData();

        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONObject data = jsonObject.getJSONObject("data");

        if (data != null) {
            count = data.getIntValue("numResults");
        }

        return count;
    }
}
