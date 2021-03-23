package com.leon.biuvideo.utils.parseDataUtils.homeParseUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.homeBeans.PartitionVideo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2021/3/23
 * @Desc
 */
public class PartitionParser {
    /**
     * 按照播放数排序
     */
    public static final String ORDER_CLICK = "click";

    /**
     * 按照评论数排序
     */
    public static final String ORDER_SCORES = "scores";

    /**
     * 按照收藏数排序
     */
    public static final String ORDER_STOW = "stow";

    /**
     * 按照投币数排序
     */
    public static final String ORDER_COIN = "coin";

    /**
     * 按照弹幕数排序
     */
    public static final String ORDER_DM = "dm";

    /**
     * 子分区ID
     */
    private final String id;

    /**
     * 当前页码数
     */
    private int pageNum = 1;

    /**
     * 单页条目数
     */
    private static final int PAGE_SIZE = 20;

    /**
     * 数据状态
     */
    public boolean dataStatus = true;

    private final String timeFrom;
    private final String timeTo;

    private int count = 0;
    private int currentCount = 0;

    public PartitionParser(String id) {
        this.id = id;

        long nowTime = System.currentTimeMillis();
        timeTo = ValueUtils.generateTime(nowTime, "yyyyMMdd", false);
        timeFrom = ValueUtils.generateTime((nowTime - (86400000 * 7)), "yyyyMMdd", false);
    }

    public List<PartitionVideo> parseData(String order) {
        Map<String, String> params = new HashMap<>(10);
        params.put("main_ver", "v3");
        params.put("search_type", "video");
        params.put("view_type", "hot_rank");
        params.put("order", order);
        params.put("copy_right", "-1");
        params.put("cate_id", id);
        params.put("page", String.valueOf(pageNum));
        params.put("pagesize", String.valueOf(PAGE_SIZE));
        params.put("time_from", timeFrom);
        params.put("time_to", timeTo);

        if (dataStatus) {
            JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.PARTITION, Headers.of(HttpUtils.getAPIRequestHeader()), params);

            if (count == 0) {
                count = response.getIntValue("numResults");
            }

            JSONArray result = response.getJSONArray("result");
            List<PartitionVideo> partitionVideoList = new ArrayList<>(result.size());
            for (Object o : result) {
                JSONObject jsonObject = (JSONObject) o;
                PartitionVideo partitionVideo = new PartitionVideo();

                partitionVideo.title = jsonObject.getString("title");
                partitionVideo.bvid = jsonObject.getString("bvid");
                partitionVideo.pic = "https:" + jsonObject.getString("pic");
                partitionVideo.play = Integer.parseInt(jsonObject.getString("play"));
                partitionVideo.danmaku = jsonObject.getIntValue("video_review");
                partitionVideo.duration = ValueUtils.lengthGenerate(jsonObject.getIntValue("duration"));

                partitionVideoList.add(partitionVideo);
            }

            currentCount += partitionVideoList.size();

            if (count == currentCount || partitionVideoList.size() < PAGE_SIZE) {
                dataStatus = false;
            }

            pageNum ++;

            return partitionVideoList;
        }

        return null;
    }
}
