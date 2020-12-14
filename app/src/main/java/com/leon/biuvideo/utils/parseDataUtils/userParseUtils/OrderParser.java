package com.leon.biuvideo.utils.parseDataUtils.userParseUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.userBeans.Order;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.values.OrderFollowType;
import com.leon.biuvideo.values.OrderType;
import com.leon.biuvideo.values.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

public class OrderParser {
    /**
     * 获取指定订阅类型、订阅状态的数据
     *
     * @param mid   用户ID
     * @param cookie    用户Cookie，可为null
     * @param orderType  订阅类型， 1：番剧、2：其他剧，不包括标签（纪录片、电视剧等）
     * @param orderFollowType     订阅状态
     * @param pageNum   页码
     * @return  返回Bangumi集合
     */
    public List<Order> parseOrder(long mid, String cookie, OrderType orderType, OrderFollowType orderFollowType, int pageNum) {
        Map<String, String> params = new HashMap<>();
        params.put("vmid", String.valueOf(mid));
        params.put("pn", String.valueOf(pageNum));
        params.put("ps", "15");
        params.put("type", String.valueOf(orderType.value));
        params.put("follow_status", String.valueOf(orderFollowType.value));

        JSONObject responseObject;
        if (cookie != null) {
            responseObject = HttpUtils.getResponse(Paths.bangumi, Headers.of("Cookie", cookie), params);
        } else {
            responseObject = HttpUtils.getResponse(Paths.bangumi, params);
        }

        JSONObject data = responseObject.getJSONObject("data");

        if (data != null) {
            return parseJSONArray(data.getJSONArray("list"));
        }

        return null;
    }

    private List<Order> parseJSONArray (JSONArray jsonArray) {
        List<Order> orders = new ArrayList<>();

        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;

            Order order = new Order();

            order.title = jsonObject.getString("title");
            order.desc = jsonObject.getString("evaluate");
            order.cover = jsonObject.getString("cover");

            JSONArray areas = jsonObject.getJSONArray("areas");
            order.areas = new String[areas.size()];

            for (int i = 0; i < areas.size(); i++) {
                JSONObject areaObject = (JSONObject) areas.get(i);;
                order.areas[i] = areaObject.getString("name");
            }

            order.badgeType = jsonObject.getString("badge");
            order.seasonId = jsonObject.getLongValue("season_id");
            order.seasonType = jsonObject.getString("season_type_name");
            order.shortUrl = jsonObject.getString("short_url");

            int follow_status = jsonObject.getIntValue("follow_status");

            switch (follow_status) {
                case 0:
                    order.followStatus = OrderFollowType.ALL;
                    break;
                case 1:
                    order.followStatus = OrderFollowType.WANT_SEE;
                    break;
                case 2:
                    order.followStatus = OrderFollowType.LOOK_IN;
                    break;
                case 3:
                    order.followStatus = OrderFollowType.SEEN;
                    break;
                default:
                    break;
            }

            order.seasonCount = jsonObject.getJSONObject("series").getIntValue("season_count");
            order.seasonTitle = jsonObject.getString("season_title");

            int total_count = jsonObject.getIntValue("total_count");
            if (total_count == -1) {
                order.total = jsonObject.getJSONObject("new_ep").getString("index_show");
            } else {
                order.total = "全" + total_count + "话";
            }

            order.progress = jsonObject.getString("progress");

            order.coin = jsonObject.getIntValue("coin");
            order.danmaku = jsonObject.getIntValue("danmaku");
            order.follow = jsonObject.getIntValue("follow");
            order.likes = jsonObject.getIntValue("likes");
            order.reply = jsonObject.getIntValue("reply");
            order.series_follow = jsonObject.getIntValue("series_follow");
            order.series_view = jsonObject.getIntValue("series_view");
            order.view = jsonObject.getIntValue("view");

            orders.add(order);
        }

        return orders;
    }

    /**
     * 获取指定订阅类型、订阅状态的条目总数
     *
     * @param mid  用户ID
     * @param cookie    用户Cookie
     * @param orderType  订阅类型
     * @param orderFollowType     订阅状态
     * @return  返回条目总数
     */
    public int getOrderCount(long mid, String cookie, OrderType orderType, OrderFollowType orderFollowType) {
        Map<String, String> params = new HashMap<>();
        params.put("vmid", String.valueOf(mid));
        params.put("pn", "1");
        params.put("ps", "15");
        params.put("type", String.valueOf(orderType.value));
        params.put("follow_status", String.valueOf(orderFollowType.value));

        JSONObject responseObject;
        if (cookie != null) {
            responseObject = HttpUtils.getResponse(Paths.bangumi, Headers.of("Cookie", cookie), params);
        } else {
            responseObject = HttpUtils.getResponse(Paths.bangumi, params);
        }

        return responseObject.getJSONObject("data").getIntValue("total");
    }
}
