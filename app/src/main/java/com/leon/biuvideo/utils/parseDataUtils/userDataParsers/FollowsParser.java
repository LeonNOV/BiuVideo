package com.leon.biuvideo.utils.parseDataUtils.userDataParsers;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.userBeans.Follow;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.PreferenceUtils;
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
 * @Time 2021/1/22
 * @Desc 关注数据解析类 (如果是查看其他用户的关注数据，则最多只能获取100条数据)
 */
public class FollowsParser extends ParserInterface<Follow> {
    private static final String REFERER = "https://space.bilibili.com/";
    private final Context context;
    private final String mid;

    /**
     * 页码数
     */
    private int pageNum = 1;

    /**
     * 条目数
     */
    private static final int PAGE_SIZE = 20;

    private int total = 100;
    private int currentTotal;

    public FollowsParser(Context context, String mid) {
        this.context = context;
        this.mid = mid;

        String userId = PreferenceUtils.getUserId();
        if (userId != null) {
            if (!userId.equals(mid)) {
                total = -1;
            }
        }
    }

    @Override
    public List<Follow> parseData() {
        if (PreferenceUtils.getLoginStatus()) {
            Map<String, String> params = new HashMap<>(4);
            params.put("vmid", mid);
            params.put("pn", String.valueOf(pageNum));
            params.put("ps", String.valueOf(PAGE_SIZE));
            params.put("order", "desc");

            if (dataStatus) {
                JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.USER_FOLLOWINGS, Headers.of(HttpUtils.getAPIRequestHeader("Referer", REFERER)), params);
                JSONArray jsonArray = response.getJSONObject("data").getJSONArray("list");

                List<Follow> followingList = new ArrayList<>(jsonArray.size());
                for (Object o : jsonArray) {
                    JSONObject jsonObject = (JSONObject) o;
                    Follow follow = new Follow();

                    follow.followerMid = jsonObject.getIntValue("mid");
                    follow.userName = jsonObject.getString("uname");
                    follow.userFace = jsonObject.getString("face");
                    follow.followerMid = jsonObject.getIntValue("mid");
                    follow.userStatus = jsonObject.getIntValue("attribute");

                    String sign = jsonObject.getString("sign");
                    follow.sign = "".equals(sign) ? context.getString(R.string.default_sign) : sign;

                    JSONObject officialVerify = jsonObject.getJSONObject("official_verify");
                    int type = officialVerify.getIntValue("type");
                    follow.role = type == 0 ? Role.PERSON : type == 1 ? Role.OFFICIAL : Role.NONE;

                    JSONObject vip = jsonObject.getJSONObject("vip");
                    follow.vipStatus = vip.getIntValue("vipStatus") == 1;

                    followingList.add(follow);
                }

                this.currentTotal += followingList.size();
                if (currentTotal == total) {
                    dataStatus = false;
                }

                if (followingList.size() < PAGE_SIZE) {
                    dataStatus = false;
                }

                pageNum++;

                return followingList;
            }
        }

        return null;
    }
}
