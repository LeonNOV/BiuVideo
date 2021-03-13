package com.leon.biuvideo.utils.parseDataUtils.homeParseUtils;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.homeBeans.Recommend;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParseInterface;
import com.leon.biuvideo.values.RecommendType;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
public class RecommendParser implements ParseInterface<Recommend> {
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
    public List<Recommend> parseData() {
        Map<String, String> headers;
        if (cookie != null) {
            headers = new HashMap<>(4);
            headers.put("Cookie", cookie);
            headers.putAll(HttpUtils.getHeaders());
        } else {
            headers = HttpUtils.getHeaders();
        }

        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.RECOMMEND, Headers.of(headers), null);

        Set<Map.Entry<String, Object>> entrySet = response.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();

        List<Recommend> recommendList= new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            RecommendType recommendType = RecommendType.getRecommendType(next.getKey());

            Object value = next.getValue();
            if (value instanceof JSONObject) {
                recommendList.addAll(parseByType(recommendType, (JSONObject) value));
            }
        }

        // 对推荐数据进行打乱
        Collections.shuffle(recommendList);

        return recommendList;
    }

    /**
     * 解析单个类型
     *
     *
     * @param recommendType 该推荐内容类型
     * @param jsonObject    jsonObject
     * @return  返回Recommend集合
     */
    private List<Recommend> parseByType(RecommendType recommendType, JSONObject jsonObject) {
        Set<Map.Entry<String, Object>> entrySet = jsonObject.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();

        List<Recommend> recommendList = new ArrayList<>(entrySet.size());

        while (iterator.hasNext()) {
            Recommend recommend = new Recommend();
            recommend.recommendType = recommendType;

            JSONObject nextValue = (JSONObject) iterator.next().getValue();

            recommend.aid = nextValue.getLongValue("aid");
            recommend.bvid = nextValue.getString("bvid");
            recommend.cover = nextValue.getString("pic");
            recommend.title = nextValue.getString("title");
            recommend.desc = nextValue.getString("desc");
            recommend.pubdate = nextValue.getLong("pubdate");
            recommend.duration = ValueUtils.lengthGenerate(nextValue.getIntValue("duration"));

            JSONObject owner = nextValue.getJSONObject("owner");
            recommend.userName = owner.getString("name");
            recommend.userFace = owner.getString("face");
            recommend.userMid = owner.getString("mid");

            JSONObject stat = nextValue.getJSONObject("stat");
            recommend.view = stat.getIntValue("view");
            recommend.danmaku = stat.getIntValue("danmaku");
            recommend.reply = stat.getIntValue("reply");
            recommend.favorite = stat.getIntValue("favorite");
            recommend.coin = stat.getIntValue("coin");
            recommend.share = stat.getIntValue("share");
            recommend.like = stat.getIntValue("like");

            recommendList.add(recommend);
        }

        return recommendList;
    }
}
