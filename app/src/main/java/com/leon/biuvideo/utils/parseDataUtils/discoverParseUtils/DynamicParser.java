package com.leon.biuvideo.utils.parseDataUtils.discoverParseUtils;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.TestBeans.RvTestBean;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2021/3/13
 * @Desc
 */
public class DynamicParser {
    public DynamicParser() {
    }

    public List<RvTestBean> parseDataWithFirstPage(String cookie) {
        Map<String, String> headers = new HashMap<>(4);
        if (cookie == null) {
            throw new NullPointerException("Cookie不能为null");
        }

        headers.put("Cookie", cookie);
        headers.putAll(HttpUtils.getHeaders());

        Map<String, String> params = new HashMap<>(1);
        params.put("type_list", BiliBiliAPIs.TYPE_LIST);

        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.DYNAMIC_BASE_PATH + BiliBiliAPIs.DYNAMIC_LOGIN, Headers.of(headers), params);


        return null;
    }

    public List<RvTestBean> parseDataWithNoLogin(String hotOffset) {
        Map<String, String> params = new HashMap<>(2);
        params.put("fake_uid", "123456");
        params.put("hot_offset", hotOffset);

        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.DYNAMIC_BASE_PATH + BiliBiliAPIs.DYNAMIC_UN_LOGIN, params);


        return null;
    }

    public List<RvTestBean> parseDataWithLoginNext(String offset) {
        Map<String, String> params = new HashMap<>(2);
        params.put("type_list", BiliBiliAPIs.TYPE_LIST);
        params.put("offset", offset);

        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.DYNAMIC_BASE_PATH + BiliBiliAPIs.DYNAMIC_LOGIN_NEXT, params);

        return null;
    }
}
