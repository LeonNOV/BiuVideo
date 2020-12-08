package com.leon.biuvideo.utils.parseDataUtils.userParseUtils;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.userBeans.UserInfo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.values.Paths;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Headers;

public class UserInfoParser {
    /**
     * 通过Cookie获取用户基本信息
     *
     * @param cookie    cookie
     * @return  返回UserInfo对象
     */
    public static UserInfo userInfoParse(String cookie) {
        JSONObject responseObject = HttpUtils.getResponse(Paths.nav, Headers.of("Cookie", cookie), null);
        JSONObject data = responseObject.getJSONObject("data");

        if (data != null) {
            UserInfo userInfo = new UserInfo();

            userInfo.userName = data.getString("uname");
            userInfo.userFace = data.getString("face");
            userInfo.mid = data.getLongValue("mid");

            JSONObject levelInfo = data.getJSONObject("level_info");
            userInfo.currentExp = levelInfo.getIntValue("current_exp");
            userInfo.currentLevel = levelInfo.getIntValue("current_level");
            userInfo.totalExp = levelInfo.getIntValue("next_exp");

            userInfo.money = data.getIntValue("money");
            userInfo.moral = data.getIntValue("moral");

            JSONObject wallet = data.getJSONObject("wallet");
            userInfo.bCoinBalance = wallet.getIntValue("bcoin_balance");

            userInfo.isVip = data.getIntValue("vipStatus") == 1;
            if (userInfo.isVip) {
                userInfo.vipDueDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date(data.getLongValue("vipDueDate")));;
                userInfo.vipLabel = data.getJSONObject("vip_label").getString("text");
            } else {
                userInfo.vipDueDate = "普通会员，无期限";
                userInfo.vipLabel = "普通会员";
            }

            return userInfo;
        }

        return null;
    }
}
