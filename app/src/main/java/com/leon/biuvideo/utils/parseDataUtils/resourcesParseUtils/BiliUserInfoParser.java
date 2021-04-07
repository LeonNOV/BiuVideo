package com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.upMasterBean.BiliUserInfo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.values.Role;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;

/**
 * up基础信息接口解析
 */
public class BiliUserInfoParser {

    /**
     * 获取up基本信息
     *
     * @param mid   up主id
     * @return  返回UpInfo对象
     */
    public BiliUserInfo parseUpInfo(long mid) {
        Map<String, String> params = new HashMap<>(2);
        params.put("mid", String.valueOf(mid));
        params.put("jsonp", "jsonp");

        JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.BILI_USER_INFO, Headers.of(HttpUtils.getAPIRequestHeader()), params);
        JSONObject dataObject = responseObject.getJSONObject("data");

        if (dataObject != null) {
            BiliUserInfo biliUserInfo = new BiliUserInfo();

            //获取name
            biliUserInfo.userName = dataObject.getString("name");

            //获取头像地址
            biliUserInfo.userFace = dataObject.getString("face");

            //获取个人签名
            biliUserInfo.sign = dataObject.getString("sign");

            //获取顶部图片
            biliUserInfo.topPhoto = dataObject.getString("top_photo");

            JSONObject official = dataObject.getJSONObject("official");
            int role = official.getIntValue("role");

            biliUserInfo.role = role == -1 ? Role.NONE : role > 1 ? Role.OFFICIAL : Role.PERSON;
            biliUserInfo.verifyDesc = official.getString("title");

            return biliUserInfo;
        }

        Log.e(Fuck.red, "响应体为空，解析失败", new NullPointerException("响应体为空，解析失败"));
        return null;
    }
}
