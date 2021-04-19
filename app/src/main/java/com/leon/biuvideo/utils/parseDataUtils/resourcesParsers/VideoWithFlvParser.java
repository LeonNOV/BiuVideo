package com.leon.biuvideo.utils.parseDataUtils.resourcesParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoWithFlv;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2020/10/18
 * @Desc 单视频数据解析类，使用该方式获取的视频只有一个画质的视频信息，默认获取最高画质视频
 */
public class VideoWithFlvParser {
    /**
     * 默认获取画质最高的视频
     * 分辨率代码
     * 6	240P 极速（仅mp4方式）
     * 16	360P 流畅
     * 32	480P 清晰
     * 64	720P 高清（登录）
     * 74	720P60 高清（大会员）
     * 80	1080P 高清（登录）
     * 112	1080P+ 高清（大会员）
     * 116	1080P60 高清（大会员）
     * 120	4K 超清（大会员）
     */
    private static final String DEFAULT_QUALITY = "120";

    /**
     * 获取4K画质需要添加该参数，默认添加
     */
    private static final String FOURK = "1";

    private final String bvid;

    public VideoWithFlvParser(String bvid) {
        this.bvid = bvid;
    }

    public VideoWithFlv parseData(String cid) {
        Map<String, String> params = new HashMap<>(4);
        params.put("bvid", String.valueOf(bvid));
        params.put("cid", String.valueOf(cid));
        params.put("qn", DEFAULT_QUALITY);
        params.put("fourk", FOURK);

        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.VIDEO_STREAM_INFO, Headers.of(HttpUtils.getAPIRequestHeader()), params);
        JSONObject data = response.getJSONObject("data");
        if (data != null) {
            VideoWithFlv videoWithFlv = new VideoWithFlv();
            videoWithFlv.cid = cid;

            JSONArray supportFormats = data.getJSONArray("support_formats");
            videoWithFlv.qualityMap = new LinkedHashMap<>(supportFormats.size());
            for (Object o : supportFormats) {
                JSONObject jsonObject = (JSONObject) o;
                int key = jsonObject.getIntValue("quality");
                String value = jsonObject.getString("new_description");
                videoWithFlv.qualityMap.put(key, value);
            }

            videoWithFlv.currentQualityId = data.getIntValue("quality");

            JSONArray jsonArray = data.getJSONArray("durl");
            videoWithFlv.videoStreamInfoList = new ArrayList<>(jsonArray.size());

            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                VideoWithFlv.VideoStreamInfo videoStreamInfo = new VideoWithFlv.VideoStreamInfo();

                videoStreamInfo.order = jsonObject.getIntValue("order");
                videoStreamInfo.size = ValueUtils.sizeFormat(jsonObject.getIntValue("size"), true);
                videoStreamInfo.url = jsonObject.getString("url");

                JSONArray backupUrl = jsonObject.getJSONArray("backup_url");
                videoStreamInfo.backupUrl = new String[backupUrl.size()];

                for (int i = 0; i < backupUrl.size(); i++) {
                    videoStreamInfo.backupUrl[i] = backupUrl.get(i).toString();
                }

                videoWithFlv.videoStreamInfoList.add(videoStreamInfo);
            }

            return videoWithFlv;
        }

        return null;
    }
}
