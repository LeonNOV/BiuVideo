package com.leon.biuvideo.utils.parseDataUtils;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.Favorite;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.values.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * 获取Ta的关注列表数据
 */
public class FollowParse {
    private final Map<String, String> requestHeader;
    private final long vmid;
    public int pn;

    /**
     * construct
     *
     * @param context   context
     * @param vmid  用户ID
     */
    public FollowParse(Context context, long vmid) {
        this.vmid = vmid;
        this.requestHeader = ParserUtils.getInterfaceRequestHeader(context);
    }

    /**
     * 获取Ta的关注列表信息
     * 从该响应体获取的信息可以使用Favorite当作数据结构
     *
     * @param pn    页码，从1开始
     * @return  返回Favorite集合
     */
    public List<Favorite> parseFollow(int pn) {
        Map<String, String> params = new HashMap<>();
        params.put("vmid", String.valueOf(vmid));
        params.put("pn", String.valueOf(pn));
        params.put("ps", "20");
        params.put("order", "desc");
        params.put("order_type", "attention");//按照最常访问获取

        JSONObject responseObject = HttpUtils.getResponse(Paths.follow, Headers.of(requestHeader), params);
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
     * @return  返回total
     */
    public int getTotal() {
        Map<String, String> params = new HashMap<>();
        params.put("vmid", String.valueOf(this.vmid));
        params.put("pn", "1");
        params.put("ps", "20");
        params.put("order", "desc");

        JSONObject responseObject = HttpUtils.getResponse(Paths.follow, params);
        JSONObject data = responseObject.getJSONObject("data");

        if (data != null) {
            return data.getIntValue("total");
        } else {
            return 0;
        }
    }
}
