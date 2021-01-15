package com.leon.biuvideo.utils.parseDataUtils;

import android.content.Context;
import android.content.SharedPreferences;

import com.leon.biuvideo.utils.HttpUtils;

import java.util.HashMap;
import java.util.Map;

public class ParserUtils {
    /**
     * 该方法适用于在获取接口数据时调用<br/>
     * 如果已登录账号，则会获取initValues中存放的Cookie数据
     *
     * @param context   context
     * @return  返回用户Cookie
     */
    public static Map<String, String> getInterfaceRequestHeader (Context context) {
        Map<String, String> requestHeader = new HashMap<>(HttpUtils.getHeaders());
        String cookie = getCookie(context);

        if (cookie == null) {
            return requestHeader;
        } else {
            requestHeader.put("Cookie", cookie);
        }

        return requestHeader;
    }

    /**
     * 该方法用于获取已登录账户的cookie数据
     *
     * @param context   context
     * @return  返回cookie数据
     */
    public static String getCookie(Context context) {
        SharedPreferences initValues = context.getSharedPreferences("initValues", Context.MODE_PRIVATE);
        return initValues.getString("cookie", null);
    }
}
