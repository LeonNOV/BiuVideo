package com.leon.biuvideo.utils.parseDataUtils.homeParseUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.orderBeans.Tag;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserInterface;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2021/3/15
 * @Desc 订阅数据解析类
 */
public class TagParser implements ParserInterface<Tag> {
    /**
     * API URL
     */
    private static final String API = BiliBiliAPIs.ORDER_TAG;

    /**
     * 单页条目数
     */
    private static final int PAGE_SIZE = 100;

    /**
     * 总条目数
     */
    private int dataCount;

    /**
     * 数据状态
     */
    private boolean dataStatus = true;

    /**
     * 请求头信息
     */
    private final Map<String, String> requestHeader = new HashMap<>();

    /**
     * 请求参数
     */
    private final Map<String, String> params = new HashMap<>();

    public TagParser() {
        if (PreferenceUtils.getLoginStatus()) {
            requestHeader.put("Cookie", PreferenceUtils.getCookie());
        }
        requestHeader.putAll(HttpUtils.getHeaders());

        params.put("vmid", PreferenceUtils.getUserId());
    }

    @Override
    public List<Tag> parseData() {
        JSONObject responseObject = HttpUtils.getResponse(API, Headers.of(requestHeader), params);
        JSONObject data = responseObject.getJSONObject("data");

        if (data != null) {
            dataCount = data.getIntValue("total");
            JSONArray tags = data.getJSONArray("tags");

            List<Tag> tagList = new ArrayList<>(tags.size());
            for (Object o : tags) {
                JSONObject jsonObject = (JSONObject) o;
                Tag tag = new Tag();

                tag.tagId = jsonObject.getString("tag_id");
                tag.tagName = jsonObject.getString("tag_name");

                String cover = jsonObject.getString("cover");
                tag.tagCover = "".equals(cover) ? null : cover;

                tagList.add(tag);
            }

            if (tagList.size() < PAGE_SIZE) {
                dataStatus = false;
            }

            return tagList;
        }

        return null;
    }

    public int getDataCount() {
        return dataCount;
    }

    public boolean getDataStatus() {
        return dataStatus;
    }
}
