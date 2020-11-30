package com.leon.biuvideo.utils.parseDataUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.Favorite;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.reflect.KVariance;

/**
 * 获取Ta的关注列表数据
 */
public class FollowParseUtils {

    /**
     * 获取Ta的关注列表信息
     * 从该响应体获取的信息可以使用Favorite当作数据结构
     *
     * @param vmid  用户ID
     * @param pn    页码，从1开始
     * @return  返回Favorite集合
     */
    public static List<Favorite> parseFollow(long vmid, int pn) {
        Map<String, String> params = new HashMap<>();
        params.put("vmid", String.valueOf(vmid));
        params.put("pn", String.valueOf(pn));
        params.put("ps", "20");
        params.put("order", "desc");

        JSONObject responseObject = HttpUtils.getResponse(Paths.follow, params);
        JSONObject data = responseObject.getJSONObject("data");
        if (data != null) {
            List<Favorite> followings = new ArrayList<>();

            JSONArray list = data.getJSONArray("list");
            for (Object temp : list) {
                JSONObject jsonObject = (JSONObject) temp;

                Favorite favorite = new Favorite();

                //获取mid
                favorite.mid = jsonObject.getLongValue("mid");

                //获取name
                favorite.name = jsonObject.getString("uname");

                //获取简介
                favorite.desc = jsonObject.getString("sign");

                //获取头像url
                favorite.faceUrl = jsonObject.getString("face");

                followings.add(favorite);
            }

            return followings;
        } else {
            return null;
        }
    }

    /**
     * 获取关注总数
     *
     * @param mid   用户ID
     * @return  返回total
     */
    public static int getTotal(long mid) {
        Map<String, String> params = new HashMap<>();
        params.put("vmid", String.valueOf(mid));
        params.put("pn", "1");
        params.put("ps", "20");
        params.put("order", "desc");

        HttpUtils httpUtils = new HttpUtils(Paths.follow, params);

        String response = httpUtils.getData();

        JSONObject responseObject = JSON.parseObject(response);
        JSONObject data = responseObject.getJSONObject("data");
        if (data != null) {
            return data.getIntValue("total");
        } else {
            return 0;
        }
    }
}
