package com.leon.biuvideo.utils.parseDataUtils.biliUserResourcesParseUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.biliUserResourcesBeans.BiliUserVideo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserInterface;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2020/10/21
 * @Desc 用户投稿视频数据解析类
 */
public class BiliUserVideoParser implements ParserInterface<BiliUserVideo> {

    /**
     * 最多播放
     */
    public static final String ORDER_CLICK = "click";

    /**
     * 最多收藏
     */
    public static final String ORDER_STOW = "stow";

    private final String mid;
    private final String order;


    private static final String PAGE_SIZE = "30";
    private int pageNum = 1;
    public boolean dataStatus = true;
    private int total = -1;
    private int currentCount = 0;

    public BiliUserVideoParser(String mid, String order) {
        this.mid = mid;
        this.order = order;
    }

    @Override
    public List<BiliUserVideo> parseData() {
        Map<String, String> params = new HashMap<>(6);
        params.put("mid", String.valueOf(mid));
        params.put("ps", PAGE_SIZE);
        params.put("pn", String.valueOf(pageNum));
        if (order != null) {
            params.put("order", order);
        }

        if (dataStatus) {
            JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.BILI_USER_VIDEO, params);
            JSONObject data = responseObject.getJSONObject("data");

            if (total == -1) {
                total = data.getJSONObject("page").getIntValue("count");
            }

            JSONArray jsonArray = data.getJSONObject("list").getJSONArray("vlist");
            List<BiliUserVideo> biliUserVideoList = new ArrayList<>(jsonArray.size());

            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                BiliUserVideo biliUserVideo = new BiliUserVideo();

                biliUserVideo.cover = "http://" + jsonObject.getString("pic");
                biliUserVideo.bvid = jsonObject.getString("bvid");
                biliUserVideo.avid = jsonObject.getString("aid");
                biliUserVideo.play = ValueUtils.generateCN(jsonObject.getInteger("play"));
                biliUserVideo.duration = jsonObject.getString("length");
                biliUserVideo.pubTime = ValueUtils.generateTime(jsonObject.getLongValue("created"), "yyyy-MM-dd HH:mm", true);
                biliUserVideo.title = jsonObject.getString("title");

                biliUserVideoList.add(biliUserVideo);
            }

            currentCount += biliUserVideoList.size();

            if (currentCount == total) {
                dataStatus = false;
            }

            pageNum ++;

            return biliUserVideoList;
        }

        return null;
    }
}
