package com.leon.biuvideo.utils.parseDataUtils.userParseUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.userBeans.History;
import com.leon.biuvideo.beans.userBeans.HistoryType;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.values.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

public class HistoryParser {

    /**
     * 获取历史记录
     *
     * @param cookie    用户Cookie
     * @param max   第一次调用为-1,后面调用只需History类中的max的值即可
     * @param viewAt    第一次调用为-1,后面调用只需History类中的viewAt的值即可
     * @param historyType   记录类别
     * @return  返回指定类别的历史记录
     */
    public History parseHistory(String cookie, long max, long viewAt, HistoryType historyType) {
        Map<String, String> params = new HashMap<>();
        params.put("max", max == -1 ? "" : String.valueOf(max));
        params.put("view_at", viewAt == -1 ? "" : String.valueOf(viewAt));
        params.put("type", historyType.value);

        JSONObject responseObject = HttpUtils.getResponse(Paths.nav, Headers.of("Cookie", cookie), params);
        JSONObject data = responseObject.getJSONObject("data");

        if (data != null) {
            History history = new History();

            JSONObject cursor = data.getJSONObject("cursor");
            history.max = cursor.getLongValue("max");
            history.viewAt = cursor.getLongValue("view_at");

            JSONArray list = data.getJSONArray("list");
            history.innerHistory = parseJSONArray(list);

            return history;
        }

        return null;
    }

    private List<History.InnerHistory> parseJSONArray(JSONArray jsonArray) {
        List<History.InnerHistory> innerHistories = new ArrayList<>();

        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;

            History.InnerHistory innerHistory = new History.InnerHistory();

            innerHistory.authorName = jsonObject.getString("author_name");

            innerHistory.authorMid = jsonObject.getLongValue("author_mid");

            innerHistory.authorFace = jsonObject.getString("author_face");

            innerHistory.viewDate = jsonObject.getLongValue("view_at");

            //获取cover和covers两个值，那个有就获取那个
            String cover = jsonObject.getString("cover");
            if (!cover.equals("")) {
                innerHistory.cover = cover;
            } else {
                innerHistory.cover = jsonObject.getJSONArray("covers").getString(0);
            }

            //获取historyJSONObject中的数据
            JSONObject object = jsonObject.getJSONObject("history");
            String business = object.getString("business");
            switch (business) {
                case "archive":
                    innerHistory.historyType = HistoryType.ARCHIVE;

                    break;
                case "article":
                    innerHistory.historyType = HistoryType.ARTICLE;

                    break;
                case "live":
                    innerHistory.historyType = HistoryType.LIVE;

                    break;
                default:
                    break;
            }

            innerHistory.bvid = object.getString("bvid");

            innerHistory.cid = object.getLongValue("cid");

            innerHistory.oid = object.getLongValue("oid");

            innerHistory.duration = jsonObject.getIntValue("duration");

            innerHistory.progress = jsonObject.getIntValue("progress");

            innerHistory.isFinish = jsonObject.getIntValue("is_finish") != 0;

            innerHistory.liveState = jsonObject.getIntValue("live_status") != 0;

            innerHistory.tagName = jsonObject.getString("tag_name");

            innerHistory.title = jsonObject.getString("title");

            innerHistory.subTitle = jsonObject.getString("show_title");

            innerHistory.videos = jsonObject.getIntValue("videos");

            innerHistories.add(innerHistory);
        }

        return innerHistories;
    }
}