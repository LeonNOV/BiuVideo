package com.leon.biuvideo.utils.parseDataUtils.userParseUtils;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.Follow;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.dataBaseUtils.FavoriteUserDatabaseUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * 获取Ta的关注列表数据
 */
public class FollowParser {
    private final Map<String, String> requestHeader;
    private final long vmid;
    public int pn;

    /**
     * construct
     *
     * @param vmid  用户ID
     */
    public FollowParser(long vmid, String cookie) {
        this.vmid = vmid;

        this.requestHeader = new HashMap<>();
        if (cookie != null) {
            requestHeader.put("Cookie", cookie);
        }

        for (Map.Entry<String, String> entry : this.requestHeader.entrySet()) {
            if (entry.getKey().equals("Referer")) {
                entry.setValue("https://space.bilibili.com/");
                break;
            }
        }
    }

    /**
     * 获取Ta的关注列表信息
     * 从该响应体获取的信息可以使用Favorite当作数据结构
     *
     * @param pn    页码，从1开始
     * @return  返回Favorite集合
     */
    public List<Follow> parseFollow(int pn) {
        Map<String, String> params = new HashMap<>();
        params.put("vmid", String.valueOf(vmid));
        params.put("pn", String.valueOf(pn));
        params.put("ps", "20");
        params.put("order", "desc");

        if (requestHeader.containsKey("Cookie")) {
            params.put("order_type", "attention");//按照最常访问获取
        }

        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.follow, Headers.of(requestHeader), params);
        JSONObject data = responseObject.getJSONObject("data");

        if (data != null) {
            List<Follow> followings = new ArrayList<>();

            JSONArray list = data.getJSONArray("list");
            for (Object temp : list) {
                JSONObject jsonObject = (JSONObject) temp;

                Follow follow = new Follow();

                //获取mid
                follow.mid = jsonObject.getLongValue("mid");

                //获取name
                follow.name = jsonObject.getString("uname");

                //获取简介
                follow.desc = jsonObject.getString("sign");

                //获取头像url
                follow.faceUrl = jsonObject.getString("face");

                followings.add(follow);
            }

            return followings;
        } else {
            return null;
        }
    }

    /**
     * 获取关注总数
     *
     * @return  返回total
     */
    public int getTotal() {
        Map<String, String> params = new HashMap<>();
        params.put("vmid", String.valueOf(this.vmid));
        params.put("pn", "1");
        params.put("ps", "20");
        params.put("order", "desc");

        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.follow, params);
        JSONObject data = responseObject.getJSONObject("data");

        if (data != null) {
            return data.getIntValue("total");
        } else {
            return 0;
        }
    }

    /**
     * 获取关注列表并进行添加
     *
     * @param mid   用户ID
     * @param cookie 用户cookie
     * @return  返回插入状态
     */
    public static Map<String, Long> getFollowings(Context context, long mid, String cookie) {

        if (mid != 0) {
            //获取总数
            FollowParser followParser = new FollowParser(mid, cookie);
            int total = followParser.getTotal();
            if (total == 0) {
                return null;
            }

            //由于官方的限制
            //在没有cookie的情况下，关注列表最多只能获取100条
            if (cookie == null) {
                if (total > 100) {
                    total = 100;
                }
            }

            //获取数据
            int pn = 1;
            int currentTotal = 0;

            FavoriteUserDatabaseUtils favoriteUserDatabaseUtils = new FavoriteUserDatabaseUtils(context);

            long successNum = 0;
            long failNum = 0;
            while (currentTotal != total) {
                List<Follow> follows = followParser.parseFollow(pn);
                if (follows != null || follows.size() == 0) {
                    currentTotal += follows.size();

                    Map<String, Long> stringLongMap = favoriteUserDatabaseUtils.addFavorite(follows);
                    successNum += stringLongMap.get("successNum");
                    failNum += stringLongMap.get("failNum");

                    pn++;
                }
            }
            Map<String, Long> importMap = new HashMap<>();
            importMap.put("successNum", successNum);
            importMap.put("failNum", failNum);

            favoriteUserDatabaseUtils.close();
            return importMap;
        } else {
            return null;
        }
    }
}
