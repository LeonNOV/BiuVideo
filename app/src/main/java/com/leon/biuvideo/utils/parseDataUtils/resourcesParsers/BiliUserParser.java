package com.leon.biuvideo.utils.parseDataUtils.resourcesParsers;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.mediaBeans.BiliUserInfo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.values.Role;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2021/4/9
 * @Desc B站用户数据解析类
 */
public class BiliUserParser {
    private final String mid;
    private OnCallBack onCallBack;

    public BiliUserParser(String mid) {
        this.mid = mid;
    }

    public interface OnCallBack {
        /**
         * 回调方法，返回用户相关数据
         *
         * @param biliUserInfo  biliUserInfo
         * @param status    status
         * @param upStatus  upStatus
         */
        void callBack(BiliUserInfo biliUserInfo, int[] status, int[] upStatus);
    }

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    public void parseData () {
        BiliUserInfo biliUserInfo = getUserInfo(mid);
        int[] status = getStatus();
        int[] upStatus = getUpStatus();

        if (onCallBack != null) {
            onCallBack.callBack(biliUserInfo, status, upStatus);
        }
    }

    /**
     * 获取B站用户信息数据
     *
     * @return  BiliUserInfo
     */
    public static BiliUserInfo getUserInfo(String mid) {
        Map<String, String> params = new HashMap<>(1);
        params.put("mid", mid);

        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.BILI_USER_INFO, Headers.of(HttpUtils.getAPIRequestHeader()), params);
        JSONObject data = response.getJSONObject("data");

        if (data != null) {
            BiliUserInfo biliUserInfo = new BiliUserInfo();

            biliUserInfo.userMid = data.getString("mid");
            biliUserInfo.userName = data.getString("name");
            biliUserInfo.userFace = data.getString("face");
            biliUserInfo.gender = data.getString("sex");

            String sign = data.getString("sign");
            biliUserInfo.sign = "".equals(sign) ? null : sign;

            biliUserInfo.topPhoto = data.getString("top_photo");
            biliUserInfo.level = data.getIntValue("level");

            JSONObject vip = data.getJSONObject("vip");
            biliUserInfo.isVip = vip.getIntValue("status") == 1;

            String label = vip.getJSONObject("label").getString("text");
            biliUserInfo.vipLabel = "".equals(label) ? null : label;

            biliUserInfo.attentionState = data.getBooleanValue("is_followed");

            JSONObject official = data.getJSONObject("official");
            int role = official.getIntValue("role");
            biliUserInfo.role = role == 0 ? Role.NONE : role > 0 && role <= 2 ? Role.PERSON : Role.OFFICIAL;

            String title = official.getString("title");
            biliUserInfo.verifyDesc = "".equals(title) ? null : title;

            return biliUserInfo;
        }

        return null;
    }

    /**
     * 获取B站用户的关注数和粉丝数
     *
     * @return  int[关注数, 粉丝数]
     */
    private int[] getStatus() {
        Map<String, String> params = new HashMap<>(1);
        params.put("vmid", mid);

        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.BILI_USER_STATUS, params);
        JSONObject data = response.getJSONObject("data");

        int[] status = new int[2];
        if (data != null) {
            status[0] = data.getIntValue("following");
            status[1] = data.getIntValue("follower");
        }

        return status;
    }

    /**
     * 获取B站用户播放数、阅读数和获赞数
     *
     * @return  int[播放数, 阅读数, 获赞数]
     */
    private int[] getUpStatus() {
        Map<String, String> params = new HashMap<>(1);
        params.put("mid", mid);

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
}
