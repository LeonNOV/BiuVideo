package com.leon.biuvideo.utils.parseDataUtils.userDataParsers;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.userBeans.Follower;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserInterface;
import com.leon.biuvideo.values.Role;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2021/3/18
 * @Desc 粉丝数据解析类
 */
public class FollowersParser implements ParserInterface<Follower> {
    private final Context context;
    private final String mid;

    /**
     * 页码数
     */
    private int pageNum = 1;

    /**
     * 每页条目数
     */
    private static final int PAGE_SIZE = 20;

    /**
     * 数据状态
     */
    public boolean dataStatus = true;

    public FollowersParser(Context context, String mid) {
        this.context = context;
        this.mid = mid;
    }

    @Override
    public List<Follower> parseData() {
        Map<String, String> params = new HashMap<>(3);
        params.put("vmid", mid);
        params.put("pn", String.valueOf(pageNum));
        params.put("ps", String.valueOf(PAGE_SIZE));

        if (dataStatus) {
            JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.USER_FOLLOWERS, Headers.of(HttpUtils.getAPIRequestHeader("Referer", "https://space.bilibili.com/")), params);
            JSONArray list = response.getJSONObject("data").getJSONArray("list");

            List<Follower> followerList = new ArrayList<>(list.size());

            for (Object o : list) {
                JSONObject jsonObject = (JSONObject) o;
                Follower follower = new Follower();

                follower.followerMid = jsonObject.getIntValue("mid");
                follower.userName = jsonObject.getString("uname");
                follower.userFace = jsonObject.getString("face");
                follower.followerMid = jsonObject.getIntValue("mid");
                follower.userStatus = jsonObject.getIntValue("attribute");

                String sign = jsonObject.getString("sign");
                follower.sign = "".equals(sign) ? context.getString(R.string.default_sign) : sign;

                JSONObject officialVerify = jsonObject.getJSONObject("official_verify");
                int type = officialVerify.getIntValue("type");
                follower.role = type == 0 ? Role.PERSON : type == 1 ? Role.OFFICIAL : Role.NONE;

                JSONObject vip = jsonObject.getJSONObject("vip");
                follower.vipStatus = vip.getIntValue("vipStatus") == 1;

                followerList.add(follower);
            }

            if (followerList.size() < PAGE_SIZE) {
                dataStatus = false;
            }

            pageNum++;

            return followerList;
        }

        return null;
    }
}
