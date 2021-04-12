package com.leon.biuvideo.utils.parseDataUtils.resourcesParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoTag;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/4/6
 * @Desc 视频Tag(频道)解析类
 */
public class VideoTagsParser {
    public static List<VideoTag> parseData(String bvid) {
        Map<String, String> params = new HashMap<>(1);
        params.put("bvid", bvid);

        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.VIDEO_DETAIL_TAGS, params);

        JSONArray data = response.getJSONArray("data");
        List<VideoTag> videoTagList = new ArrayList<>(data.size());
        for (Object datum : data) {
            JSONObject jsonObject = (JSONObject) datum;
            VideoTag videoTag = new VideoTag();

            videoTag.tagName = jsonObject.getString("tag_name");
            videoTag.tagId = jsonObject.getString("tag_id");

            String color = jsonObject.getString("color");
            videoTag.color = "".equals(color) ? null : color;

            videoTagList.add(videoTag);
        }

        return videoTagList;
    }
}
