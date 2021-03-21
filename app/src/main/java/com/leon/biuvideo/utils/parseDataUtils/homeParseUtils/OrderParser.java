package com.leon.biuvideo.utils.parseDataUtils.homeParseUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.orderBeans.Order;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserInterface;
import com.leon.biuvideo.values.OrderType;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2020/12/14
 * @Desc 订阅数据解析类
 */
public class OrderParser implements ParserInterface<Order> {

    /**
     * 初始页码
     */
    private int pageNum = 1;

    /**
     * 单页条目数
     */
    private static final int PAGE_SIZE = 15;

    /**
     * 数据状态
     */
    public boolean dataStatus = true;

    /**
     * 请求头信息
     */
    private final Map<String, String> requestHeader = new HashMap<>();

    /**
     * 请求参数
     */
    private final Map<String, String> params = new HashMap<>();

    /**
     * @param orderType     只接收OrderType.BANGUMI、Order.SERIES
     */
    public OrderParser (OrderType orderType) {
        requestHeader.putAll(HttpUtils.getAPIRequestHeader());

        params.put("vmid", PreferenceUtils.getUserId());
        params.put("pn", String.valueOf(pageNum));
        params.put("ps", String.valueOf(PAGE_SIZE));
        params.put("type", String.valueOf(orderType.value));

        // 跟随状态默认为所有
        params.put("follow_status", "0");
    }

    @Override
    public List<Order> parseData() {
        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.ORDER_LIST, Headers.of(requestHeader), params);
        JSONObject data = responseObject.getJSONObject("data");

        if (data != null) {
            return parseJSONArray(data.getJSONArray("list"));
        }

        return null;
    }

    /**
     * 解析JSONArray
     *
     * @param jsonArray JSONArray
     * @return Order集合
     */
    private List<Order> parseJSONArray(JSONArray jsonArray) {
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
            order.mediaId = jsonObject.getLongValue("media_id");

            order.followStatus = jsonObject.getIntValue("follow_status");

            order.seasonCount = jsonObject.getJSONObject("series").getIntValue("season_count");
            order.seasonTitle = jsonObject.getString("season_title");

            int totalCount = jsonObject.getIntValue("total_count");
            if (totalCount == -1) {
                order.total = jsonObject.getJSONObject("new_ep").getString("index_show");
            } else {
                order.total = "全" + totalCount + "话";
            }

            order.progress = jsonObject.getString("progress");

            order.coin = jsonObject.getIntValue("coin");
            order.danmaku = jsonObject.getIntValue("danmaku");
            order.follow = jsonObject.getIntValue("follow");
            order.likes = jsonObject.getIntValue("likes");
            order.reply = jsonObject.getIntValue("reply");
            order.seriesFollow = jsonObject.getIntValue("series_follow");
            order.seriesView = jsonObject.getIntValue("series_view");
            order.view = jsonObject.getIntValue("view");

            orders.add(order);
        }

        // 每获取一次，页数+1
        pageNum ++;

        /**
         * 如果当前获取的条目数小于{@value PAGE_SIZE},则表示当前页为最后一页数据
         */
        if (orders.size() < PAGE_SIZE) {
            dataStatus = false;
        }

        return orders;
    }
}
