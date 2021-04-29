package com.leon.biuvideo.utils.parseDataUtils.userDataParsers;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.userBeans.UserInfo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.Role;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2020/12/8
 * @Desc 用户数据解析类
 */
public class UserInfoParser {
    private OnSuccessListener onSuccessListener;

    private final Context context;

    public UserInfoParser(Context context) {
        this.context = context;
    }

    public interface OnSuccessListener {
        /**
         * 获取完数据后的回调方法
         *
         * @param userInfo 用户基本信息
         * @param banner   横幅图片
         * @param bCoins   B币数量
         * @param stat  关注数和粉丝数
         * @param upStat 播放数、阅读数和获赞数
         */
        void onCallback(UserInfo userInfo, String banner, int bCoins, int[] stat, int[] upStat);
    }

    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    /**
     * 通过Cookie获取用户基本信息
     */
    public void parseData() {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                if (onSuccessListener != null) {
                    UserInfo userInfo = getBaseData();
                    String userBannerPhoto = getUserBannerPhoto();
                    int coinNum = getCoinNum();
                    int[] status = getStatus();
                    int[] upStatus = getUpStatus();

                    onSuccessListener.onCallback(userInfo, userBannerPhoto, coinNum, status, upStatus);
                }
            }
        });
    }

    /**
     * 获取横幅图片
     */
    private String getUserBannerPhoto() {
        Map<String, String> params = new HashMap<>(2);
        params.put("mid", PreferenceUtils.getUserId());
        params.put("photo", "true");

        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.USER_CARD, Headers.of(HttpUtils.getAPIRequestHeader()), params);
        JSONObject data = responseObject.getJSONObject("data");

        if (data != null) {
            return data.getJSONObject("space").getString("l_img");
        }

        return null;
    }

    /**
     * 获取B币数量
     */
    private int getCoinNum() {
        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.USER_WALLET, Headers.of(HttpUtils.getAPIRequestHeader()), null);
        return response.getJSONObject("data").getJSONObject("accountInfo").getIntValue("defaultBp");
    }

    /**
     * 获关注数和粉丝数
     *
     * @return int[关注数, 粉丝数]
     */
    private int[] getStatus() {
        Map<String, String> paramsA = new HashMap<>(1);
        paramsA.put("vmid", PreferenceUtils.getUserId());

        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.BILI_USER_STATUS, paramsA);
        JSONObject data = response.getJSONObject("data");

        int[] status = new int[2];
        if (data != null) {
            status[0] = data.getIntValue("following");
            status[1] = data.getIntValue("follower");
        }

        return status;
    }

    /**
     * 播放数、阅读数和获赞数
     *
     * @return int[播放数, 阅读数, 获赞数]
     */
    private int[] getUpStatus() {
        Map<String, String> params = new HashMap<>(1);
        params.put("mid", PreferenceUtils.getUserId());

        int[] upStatus = new int[3];
        if (PreferenceUtils.getLoginStatus()) {
            JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.BILI_USER_UP_STATUS, Headers.of(HttpUtils.getAPIRequestHeader()), params);
            JSONObject data = response.getJSONObject("data");

            if (data != null) {
                upStatus[0] = data.getJSONObject("archive").getIntValue("view");
                upStatus[1] = data.getJSONObject("article").getIntValue("view");
                upStatus[2] = data.getIntValue("likes");
            }
        }

        return upStatus;
    }

    /**
     * 获取用户基本数据
     *
     * @return UserInfo
     */
    private UserInfo getBaseData() {
        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.USER_BASE_INFO, Headers.of(HttpUtils.getAPIRequestHeader()), null);
        JSONObject data = responseObject.getJSONObject("data");

        if (data != null) {
            UserInfo userInfo = new UserInfo();

            userInfo.mid = data.getLongValue("mid");
            userInfo.userName = data.getString("name");
            userInfo.sex = data.getString("sex");
            userInfo.userFace = data.getString("face");

            String sign = data.getString("sign");
            userInfo.sign = "".equals(sign) ? context.getString(R.string.default_sign) : sign;
            userInfo.moral = data.getIntValue("moral");
            userInfo.birthday = ValueUtils.generateTime(data.getLongValue("birthday"), "yyyy-MM-dd", true);

            JSONObject levelExp = data.getJSONObject("level_exp");
            userInfo.currentExp = levelExp.getIntValue("current_exp");
            userInfo.currentLevel = levelExp.getIntValue("current_level");
            userInfo.totalExp = levelExp.getIntValue("next_exp");

            userInfo.coins = data.getIntValue("coins");

            userInfo.isVip = data.getIntValue("status") == 1;
            if (userInfo.isVip) {
                userInfo.vipDueDate = ValueUtils.generateTime(data.getLongValue("due_date"), "yyyy-MM-dd HH:mm:ss", false);
                userInfo.vipLabel = data.getJSONObject("label").getString("text");
            } else {
                userInfo.vipDueDate = "普通会员，无期限";
                userInfo.vipLabel = "普通会员";
            }

            JSONObject official = data.getJSONObject("official");
            userInfo.isVerify = official.getIntValue("type") == 0;
            int role = official.getIntValue("role");
            userInfo.role = role == 0 ? Role.NONE : role > 2 ? Role.OFFICIAL : Role.PERSON;
            userInfo.verifyTitle = official.getString("title");
            userInfo.verifyDesc = official.getString("desc");


            return userInfo;
        }

        return null;
    }
}
