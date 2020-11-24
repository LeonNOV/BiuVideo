package com.leon.biuvideo.utils.parseDataUtils.searchParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.BiliUser;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.OrderType;
import com.leon.biuvideo.utils.Paths;
import com.leon.biuvideo.utils.SearchType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * 解析搜索结果-用户数据
 */
public class BiliUserParser {
    /**
     * 获取用户列表
     *
     * @param keyword   关键字
     * @param pn    页码
     * @param orderType 排序方式
     * @return  返回用户数据
     */
    public List<BiliUser> userParse(String keyword, int pn, OrderType orderType) {
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("search_type", SearchType.BILI_USER.value);
        params.put("page", String.valueOf(pn));
        params.put("order", orderType.value);

        Fuck.blue("BiliUser----pageNum:" + pn + "----" + Paths.search + params.toString());

        HttpUtils httpUtils = new HttpUtils(Paths.search, Headers.of("Referer", "https://search.bilibili.com"), params);
        String response = httpUtils.getData();

        JSONObject responseObject = JSONObject.parseObject(response);

        List<BiliUser> biliUsers = new ArrayList<>();

        JSONObject data = responseObject.getJSONObject("data");
        if (data != null) {
            biliUsers = parseData(data);
        }

        return biliUsers;
    }

    /**
     * 解析JSONObject类型的数据
     *
     * @param data  JSONObject对象
     * @return  返回解析结果
     */
    private List<BiliUser> parseData(JSONObject data) {
        JSONArray result = data.getJSONArray("result");

        List<BiliUser> biliUsers;
        if (result.size() != 0) {
            biliUsers = new ArrayList<>();

            for (Object o : result) {
                BiliUser biliUser = new BiliUser();
                JSONObject jsonObject = (JSONObject) o;

                //用户ID
                biliUser.mid = jsonObject.getLongValue("mid");

                //用户名称
                biliUser.name = jsonObject.getString("uname");

                //用户说明
                biliUser.usign = jsonObject.getString("usign");

                //粉丝数
                biliUser.fans = jsonObject.getIntValue("fans");

                //视频数
                biliUser.videos = jsonObject.getIntValue("videos");

                //头像链接
                biliUser.face = "http://" + jsonObject.getString("upic");

                biliUsers.add(biliUser);
            }

            return biliUsers;
        }

        return null;
    }

    /**
     * 获取结果个数
     *
     * @param keyword   关键字
     * @return  返回搜索结果个数
     */
    public static int getSearchUserCount(String keyword) {
        int count = -1;

        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("search_type", SearchType.BILI_USER.value);
        params.put("page", "1");
        params.put("order", OrderType.DEFAULT.value);

        HttpUtils httpUtils = new HttpUtils(Paths.search, Headers.of("Referer", "https://search.bilibili.com"), params);
        String response = httpUtils.getData();

        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONObject data = jsonObject.getJSONObject("data");

        if (data != null) {
            count = data.getIntValue("numResults");
        }

        return count;
    }
}
