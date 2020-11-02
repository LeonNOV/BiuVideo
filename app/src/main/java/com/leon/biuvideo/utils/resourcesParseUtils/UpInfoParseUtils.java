package com.leon.biuvideo.utils.resourcesParseUtils;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.upMasterBean.UpInfo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.LogTip;
import com.leon.biuvideo.utils.Paths;

import java.util.HashMap;
import java.util.Map;

/**
 * up基础信息接口解析
 */
public class UpInfoParseUtils {
    /**
     * 获取up基本信息
     *
     * @param mid   up主id
     * @return  返回UpInfo对象
     */
    public static UpInfo parseUpInfo(long mid) {
        Map<String, Object> values = new HashMap<>();
        values.put("mid", mid);
        values.put("jsonp", "jsonp");

        String response = HttpUtils.GETByParam(Paths.info, values);

        JSONObject jsonObject = JSON.parseObject(response);

        JSONObject dataObject = jsonObject.getJSONObject("data");

        if (dataObject != null) {
            UpInfo upInfo = new UpInfo();

            //获取name
            upInfo.name = dataObject.getString("name");

            //获取头像地址
            upInfo.face = dataObject.getString("face");

            //获取个人签名
            upInfo.sign = dataObject.getString("sign");

            //获取顶部图片
            upInfo.topPhoto = dataObject.getString("top_photo");

            return upInfo;
        }

        Log.e(LogTip.red, "响应体为空，解析失败", new NullPointerException("响应体为空，解析失败"));
        return null;
    }
}
