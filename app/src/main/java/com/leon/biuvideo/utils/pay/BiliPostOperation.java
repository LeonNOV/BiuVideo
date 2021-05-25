package com.leon.biuvideo.utils.pay;

import com.leon.biuvideo.utils.HttpUtils;

import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/5/25
 * @Desc 用户数据POST操作类
 */
public class BiliPostOperation {
    public static HttpUtils getInstance(String url, Map<String, String> formBody) {
        return new HttpUtils(url, formBody);
    }
}
