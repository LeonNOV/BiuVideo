package com.leon.biuvideo.utils.parseDataUtils.homeParseUtils;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.resourcesBeans.VideoRecommend;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserInterface;
import com.leon.biuvideo.values.RecommendType;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2021/3/10
 * @Desc 推荐数据解析类
 */
public class RecommendParser extends ParserInterface<VideoRecommend> {
    private final String cookie;

    /**
     * @param cookie    如果为null，则获取系统推荐的内容，不为null则获取个人推荐内容
     */
    public RecommendParser(String cookie) {
        this.cookie = cookie;
    }

    /**
     * 解析所有推荐的数据
     *
     * @return  返回一个被打乱“顺序”的集合
     */
    @Override
    public List<VideoRecommend> parseData() {
        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.RECOMMEND, Headers.of(HttpUtils.getAPIRequestHeader()), null);

        Set<Map.Entry<String, Object>> entrySet = response.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();

        List<VideoRecommend> videoRecommendList = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            RecommendType recommendType = RecommendType.getRecommendType(next.getKey());

            Object value = next.getValue();
            if (value instanceof JSONObject) {
                videoRecommendList.addAll(parseByType(recommendType, (JSONObject) value));
            }
        }

        // 对推荐数据进行打乱
        Collections.shuffle(videoRecommendList);

        return videoRecommendList;
    }

    /**
     * 解析单个类型
     *
     *
     * @param recommendType 该推荐内容类型
     * @param jsonObject    jsonObject
     * @return  返回Recommend集合
     */
    private List<VideoRecommend> parseByType(RecommendType recommendType, JSONObject jsonObject) {
        Set<Map.Entry<String, Object>> entrySet = jsonObject.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();

        List<VideoRecommend> videoRecommendList = new ArrayList<>(entrySet.size());

        while (iterator.hasNext()) {
            VideoRecommend videoRecommend = new VideoRecommend();
            videoRecommend.recommendType = recommendType;

            JSONObject nextValue = (JSONObject) iterator.next().getValue();

            videoRecommend.aid = nextValue.getLongValue("aid");
            videoRecommend.bvid = nextValue.getString("bvid");
            videoRecommend.cover = nextValue.getString("pic");
            videoRecommend.title = nextValue.getString("title");
            videoRecommend.desc = nextValue.getString("desc");
            videoRecommend.pubdate = nextValue.getLong("pubdate");
            videoRecommend.duration = ValueUtils.lengthGenerate(nextValue.getIntValue("duration"));

            JSONObject owner = nextValue.getJSONObject("owner");
            videoRecommend.userName = owner.getString("name");
            videoRecommend.userFace = owner.getString("face");
            videoRecommend.userMid = owner.getString("mid");

            JSONObject stat = nextValue.getJSONObject("stat");
            videoRecommend.view = stat.getIntValue("view");
            videoRecommend.danmaku = stat.getIntValue("danmaku");
            videoRecommend.reply = stat.getIntValue("reply");
            videoRecommend.favorite = stat.getIntValue("favorite");
            videoRecommend.coin = stat.getIntValue("coin");
            videoRecommend.share = stat.getIntValue("share");
            videoRecommend.like = stat.getIntValue("like");

            videoRecommendList.add(videoRecommend);
        }

        return videoRecommendList;
    }
}
