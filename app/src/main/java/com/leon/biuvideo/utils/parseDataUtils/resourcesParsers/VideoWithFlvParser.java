package com.leon.biuvideo.utils.parseDataUtils.resourcesParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoWithFlv;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.PreferenceUtils;
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
     * 获取4K画质需要添加该参数，默认添加
     */
    private static final String FOURK = "1";

    private String bvid;

    public VideoWithFlvParser() {
    }

    public VideoWithFlvParser(String bvid) {
        this.bvid = bvid;
    }

    /**
     * 获取视频流链接
     *
     * @param cid   选集ID
     * @param qualityCode   清晰度代码，可为null
     * @param isBangumi 是否为番剧
     * @param isPlay    是否用于播放
     * @return  VideoWithFlv
     */
    public VideoWithFlv parseData(String cid, String qualityCode, boolean isBangumi, boolean isPlay) {
        Map<String, String> params = new HashMap<>(3);
        params.put("cid", String.valueOf(cid));

        if (qualityCode == null) {
            params.put("qn", isPlay ?
                    String.valueOf(PreferenceUtils.getPlayQuality()) :
                    String.valueOf(PreferenceUtils.getDownloadQuality()));
        } else {
            params.put("qn", qualityCode);
        }

        params.put("fourk", FOURK);
        if (!isBangumi && bvid != null) {
            params.put("bvid", bvid);
        }

        JSONObject response = HttpUtils.getResponse(isBangumi ?
                BiliBiliAPIs.BANGUMI_STREAM_INFO:
                BiliBiliAPIs.VIDEO_STREAM_INFO,
                Headers.of(HttpUtils.getAPIRequestHeader()),
                params);

        JSONObject data;

        if (response.getIntValue("code") == 0) {

            if (isBangumi) {
                data = response.getJSONObject("result");
            } else {
                data = response.getJSONObject("data");
            }
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
        }

        return null;
    }
}
