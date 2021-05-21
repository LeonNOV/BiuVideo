package com.leon.biuvideo.utils.parseDataUtils.biliUserResourcesParseUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.biliUserResourcesBeans.BiliUserAudio;
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
 * @Time 2020/10/22
 * @Desc B站用户音频数据解析类
 */
public class BiliUserAudiosParser extends ParserInterface<BiliUserAudio> {
    /**
     * 最新发布
     */
    public static final int ORDER_DEFAULT = 1;

    /**
     * 最多播放
     */
    public static final int ORDER_CLICK = 2;

    /**
     * 最多收藏
     */
    public static final int ORDER_STOW = 3;


    private final String mid;
    private final int order;

    private int pageNum = 1;
    private static final String PAGE_SIZE = "30";

    private int total = -1;
    private int currentCount = 0;

    public BiliUserAudiosParser(String mid, int order) {
        this.mid = mid;
        this.order = order;
    }

    @Override
    public List<BiliUserAudio> parseData() {
        Map<String, String> params = new HashMap<>(4);
        params.put("uid", String.valueOf(mid));
        params.put("pn", String.valueOf(pageNum));
        params.put("ps", PAGE_SIZE);
        params.put("order", String.valueOf(order));

        if (dataStatus) {
            JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.BILI_USER_AUDIO, params);
            JSONObject data = responseObject.getJSONObject("data");

            if (total == -1) {
                total = data.getIntValue("totalSize");
                if (total == 0) {
                    return null;
                }
            }

            JSONArray jsonArray = data.getJSONArray("data");
            List<BiliUserAudio> biliUserAudioList = new ArrayList<>(jsonArray.size());
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                BiliUserAudio biliUserAudio = new BiliUserAudio();

                biliUserAudio.id = jsonObject.getString("id");
                biliUserAudio.cover = jsonObject.getString("cover");
                biliUserAudio.duration = ValueUtils.lengthGenerate(jsonObject.getIntValue("duration"));
                biliUserAudio.title = jsonObject.getString("title");
                biliUserAudio.play = ValueUtils.generateCN(jsonObject.getJSONObject("statistic").getLong("play"));
                biliUserAudio.pubTime = ValueUtils.generateTime(jsonObject.getLong("ctime"), "yyyy-MM-dd HH:mm", true);

                biliUserAudioList.add(biliUserAudio);
            }

            currentCount += biliUserAudioList.size();

            if (currentCount == total) {
                dataStatus = false;
            }

            pageNum ++;

            return biliUserAudioList;
        }

        return null;
    }
}
