package com.leon.biuvideo.utils.parseDataUtils.userDataParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.userBeans.History;
import com.leon.biuvideo.beans.userBeans.HistoryType;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserInterface;
import com.leon.biuvideo.values.HistoryPlatformType;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2020/12/9
 * @Desc 历史记录解析类
 */
public class HistoryParser extends ParserInterface<History> {
    private long max = 0;
    private long viewAt = 0;

    @Override
    public List<History> parseData() {
        if (PreferenceUtils.getLoginStatus()) {
            Map<String, String> params = new HashMap<>(2);
            params.put("max", String.valueOf(max));
            params.put("view_at", String.valueOf(viewAt));

            if (dataStatus) {
                JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.HISTORY, Headers.of(HttpUtils.getAPIRequestHeader()), params);

                int code = responseObject.getIntValue("code");
                if (code == 0) {
                    JSONObject data = responseObject.getJSONObject("data");

                    JSONObject cursor = data.getJSONObject("cursor");
                    this.max = cursor.getLongValue("max");
                    this.viewAt = cursor.getLongValue("view_at");
                    if (this.max == 0 && this.viewAt == 0) {
                        this.dataStatus = false;
                    }

                    JSONArray list = data.getJSONArray("list");
                    List<History> historyList = new ArrayList<>(list.size());

                    if (list.size() == 0) {
                        return null;
                    }

                    for (Object o : list) {
                        History history = parseArrayElement((JSONObject) o);

                        if (history != null) {
                            historyList.add(history);
                        }
                    }

                    return historyList;
                }
            }
        }

        return null;
    }

    private History parseArrayElement (JSONObject jsonObject) {
        History history = new History();

        history.authorName = jsonObject.getString("author_name");
        history.authorMid = jsonObject.getLongValue("author_mid");
        history.authorFace = jsonObject.getString("author_face");
        history.viewTime = jsonObject.getLongValue("view_at");
        history.badge = jsonObject.getString("badge");

        //获取cover和covers两个值，那个有就获取那个
        String cover = jsonObject.getString("cover");
        if (!"".equals(cover)) {
            history.cover = cover;
        } else {
            history.cover = jsonObject.getJSONArray("covers").getString(0);
        }

        //获取historyJSONObject中的数据
        JSONObject historyJSONObject = jsonObject.getJSONObject("history");
        String business = historyJSONObject.getString("business");
        switch (business) {
            case "archive":
                history.historyType = HistoryType.VIDEO;
                if ("".equals(history.badge)) {
                    history.badge = "视频";
                }
                break;
            case "article-list":
            case "article":
                history.historyType = HistoryType.ARTICLE;
                break;
            case "live":
                history.historyType = HistoryType.LIVE;
                break;
            case "pgc":
                history.historyType = HistoryType.BANGUMI;
                history.newDesc = jsonObject.getString("new_desc");
                history.showTitle = jsonObject.getString("show_title");
                break;
            default:
                break;
        }

        // 获取历史记录观看平台
        int dt = historyJSONObject.getIntValue("dt");
        if (dt <= 7) {
            if (dt % 2 != 0) {
                history.historyPlatformType = HistoryPlatformType.PHOTO;
            } else {
                if (dt == 2) {
                    history.historyPlatformType = HistoryPlatformType.PC;
                } else if (dt == 4 || dt == 6) {
                    history.historyPlatformType = HistoryPlatformType.PAD;
                }
            }
        } else {
            if (dt == 33) {
                history.historyPlatformType = HistoryPlatformType.TV;
            } else {
                history.historyPlatformType = HistoryPlatformType.OTHER;
            }
        }

        // 如果不存在以上business类型则不获取该历史记录
        if (history.historyType == null) {
            return null;
        }

        history.business = historyJSONObject.getString("business");
        history.kid = jsonObject.getString("kid");

        history.bvid = historyJSONObject.getString("bvid");
        history.seasonId = jsonObject.getString("kid");

        if ("article-list".equals(history.business)) {
            history.articleId = historyJSONObject.getString("cid");
        } else {
            history.articleId = history.seasonId;
        }

        history.duration = jsonObject.getIntValue("duration");

        history.progress = jsonObject.getIntValue("progress");

        history.isFinish = jsonObject.getIntValue("is_finish") != 0;

        history.liveState = jsonObject.getIntValue("live_status") != 0;

        history.tagName = jsonObject.getString("tag_name");

        history.title = jsonObject.getString("title");

        String showTitle = jsonObject.getString("show_title");
        history.subTitle = "".equals(showTitle) ? null : showTitle;

        history.videos = jsonObject.getIntValue("videos");

        return history;
    }
}
