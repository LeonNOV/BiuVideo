package com.leon.biuvideo.utils.parseDataUtils.userParseUtils;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.userBeans.UserInfo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import okhttp3.Headers;

public class UserInfoParser {
    private final Map<String, String> requestHeader;

    public UserInfoParser(Context context) {
        this.requestHeader = ParserUtils.getInterfaceRequestHeader(context);
    }

    /**
     * 通过Cookie获取用户基本信息
     *
     * @return  返回UserInfo对象
     */
    public UserInfo userInfoParse() {
        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.nav, Headers.of(requestHeader), null);
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
