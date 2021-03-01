package com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.upMasterBean.UserInfo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.parseDataUtils.ParserUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;

/**
 * up基础信息接口解析
 */
public class UserInfoParser {
    private final Map<String, String> requestHeader;

    public UserInfoParser(Context context) {
        this.requestHeader = ParserUtils.getInterfaceRequestHeader(context);
    }

    /**
     * 获取up基本信息
     *
     * @param mid   up主id
     * @return  返回UpInfo对象
     */
    public UserInfo parseUpInfo(long mid) {
        Map<String, String> params = new HashMap<>();
        params.put("mid", String.valueOf(mid));
        params.put("jsonp", "jsonp");

        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.info, Headers.of(requestHeader), params);
        JSONObject dataObject = responseObject.getJSONObject("data");

        if (dataObject != null) {
            UserInfo userInfo = new UserInfo();

            //获取name
            userInfo.name = dataObject.getString("name");

            //获取头像地址
            userInfo.face = dataObject.getString("face");

            //获取个人签名
            userInfo.sign = dataObject.getString("sign");

            //获取顶部图片
            userInfo.topPhoto = dataObject.getString("top_photo");

            return userInfo;
        }

        Log.e(Fuck.red, "响应体为空，解析失败", new NullPointerException("响应体为空，解析失败"));
        return null;
    }
}
