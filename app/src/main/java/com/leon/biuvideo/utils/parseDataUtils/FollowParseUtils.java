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
        params.put("mid", String.valueOf(vmid));
        params.put("pn", String.valueOf(pn));
        params.put("ps", "20");
        params.put("order", "desc");

        HttpUtils httpUtils = new HttpUtils(Paths.follow, params);

        String response = httpUtils.getData();

        JSONObject responseObject = JSON.parseObject(response);
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
            }

            return followings;
        } else {
            return null;
        }
    }
}
