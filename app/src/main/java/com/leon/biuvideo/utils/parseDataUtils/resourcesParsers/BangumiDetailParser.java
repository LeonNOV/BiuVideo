package com.leon.biuvideo.utils.parseDataUtils.resourcesParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.Bangumi;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.BangumiAnthology;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.BangumiSeason;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.BangumiSection;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2021/3/21
 * @Desc 番剧详细信息解析类
 */
public class BangumiDetailParser {
    private final String seasonId;

    public BangumiDetailParser(String seasonId) {
        this.seasonId = seasonId;
    }

    public Bangumi parseData() {
        Map<String, String> params = new HashMap<>(1);
        params.put("season_id", seasonId);

        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.BANGUMI_DETAIL, Headers.of(HttpUtils.getAPIRequestHeader()), params);
        int code = response.getIntValue("code");
        if (code == 0) {
            JSONObject result = response.getJSONObject("result");
            Bangumi bangumi = new Bangumi();

            bangumi.mediaId = result.getString("media_id");
            bangumi.seasonId = result.getString("season_id");
            bangumi.seasonTitle = result.getString("season_title");
            bangumi.cover = result.getString("cover");
            bangumi.desc = result.getString("evaluate");

            JSONObject newEp = result.getJSONObject("new_ep");
            bangumi.newEpDesc = newEp.getString("desc");
            bangumi.newEpIsNew = newEp.getIntValue("is_new") == 1;

            JSONObject publish = result.getJSONObject("publish");
            bangumi.isFinished = publish.getIntValue("is_finish") == 1;
            bangumi.isStarted = publish.getIntValue("is_started") == 1;
            bangumi.pubTime = publish.getString("pub_time");
            bangumi.pubTimeShow = publish.getString("pub_time_show");

            JSONObject rating = result.getJSONObject("rating");
            bangumi.ratingCount = rating.getIntValue("count");
            bangumi.ratingScore = rating.getDoubleValue("score");

            JSONObject series = result.getJSONObject("series");
            bangumi.seriesId = series.getString("series_id");
            bangumi.seriesTitle = series.getString("series_title");

            JSONObject stat = result.getJSONObject("stat");
            bangumi.coins = stat.getIntValue("coins");
            bangumi.danmaku = stat.getIntValue("danmakus");
            bangumi.favorites = stat.getIntValue("favorites");
            bangumi.likes = stat.getIntValue("likes");
            bangumi.reply = stat.getIntValue("reply");
            bangumi.share = stat.getIntValue("share");
            bangumi.views = stat.getIntValue("views");

            bangumi.subtitle = result.getString("subtitle");
            bangumi.title = result.getString("title");

            bangumi.bangumiAnthologyList = getBangumiEps(result.getJSONArray("episodes"));
            bangumi.bangumiSeasonList = getBangumiSeasons(result.getJSONArray("seasons"));
            bangumi.bangumiSectionList = getBangumiSections(result.getJSONArray("section"));

            return bangumi;
        } else {
            return null;
        }
    }

    /**
     * 解析本季单集数据
     *
     * @param episodes  JSONArray
     * @return  BangumiEp集合
     */
    private List<BangumiAnthology> getBangumiEps(JSONArray episodes) {
        if (episodes == null) {
            return null;
        }

        List<BangumiAnthology> bangumiAnthologies = new ArrayList<>(episodes.size());
        for (Object o : episodes) {
            JSONObject jsonObject = (JSONObject) o;
            BangumiAnthology bangumiAnthology = new BangumiAnthology();

            bangumiAnthology.aid = jsonObject.getString("aid");
            bangumiAnthology.bvid = jsonObject.getString("bvid");
            bangumiAnthology.cid = jsonObject.getString("cid");
            bangumiAnthology.id = jsonObject.getString("id");

            String badge = jsonObject.getString("badge");
            bangumiAnthology.badge = "".equals(badge) ? null : badge;
            bangumiAnthology.cover = jsonObject.getString("cover");
            bangumiAnthology.longTitle = jsonObject.getString("long_title");
            bangumiAnthology.pubTime = jsonObject.getLongValue("pub_time");
            bangumiAnthology.shortLink = jsonObject.getString("short_link");
            bangumiAnthology.subTitle = jsonObject.getString("subTitle");

            bangumiAnthologies.add(bangumiAnthology);
        }

        return bangumiAnthologies;
    }

    /**
     * 解析季数据
     *
     * @param seasons   JSONArray
     * @return  BangumiSeason集合
     */
    private List<BangumiSeason> getBangumiSeasons(JSONArray seasons) {
        if (seasons == null) {
            return null;
        }

        List<BangumiSeason> bangumiSeasonList = new ArrayList<>(seasons.size());
        for (Object season : seasons) {
            JSONObject jsonObject = (JSONObject) season;
            BangumiSeason bangumiSeason = new BangumiSeason();

            String badge = jsonObject.getString("badge");
            bangumiSeason.badge = "".equals(badge) ? null : badge;
            bangumiSeason.cover = jsonObject.getString("cover");
            bangumiSeason.mediaId = jsonObject.getString("media_id");
            bangumiSeason.seasonId = jsonObject.getString("season_id");
            bangumiSeason.seasonTitle = jsonObject.getString("season_title");

            JSONObject stat = jsonObject.getJSONObject("stat");
            bangumiSeason.favorites = stat.getIntValue("favorites");
            bangumiSeason.seriesFollow = stat.getIntValue("series_follow");
            bangumiSeason.views = stat.getIntValue("views");

            bangumiSeasonList.add(bangumiSeason);
        }

        return bangumiSeasonList;
    }

    /**
     * 解析PV数据
     *
     * @param section   JSONArray
     * @return  BangumiSection集合
     */
    private List<BangumiSection> getBangumiSections(JSONArray section) {
        if (section == null) {
            return null;
        }

        List<BangumiSection> bangumiSectionList = new ArrayList<>(section.size());
        for (Object o : section) {
            JSONObject jsonObject = (JSONObject) o;
            BangumiSection bangumiSection = new BangumiSection();
            bangumiSection.id = jsonObject.getIntValue("id");
            bangumiSection.title = jsonObject.getString("title");

            JSONArray episodes = jsonObject.getJSONArray("episodes");
            bangumiSection.episodeList = new ArrayList<>(episodes.size());
            for (Object o1 : episodes) {
                JSONObject jsonObject1 = (JSONObject) o1;
                BangumiSection.Episode episode1 = new BangumiSection.Episode();

                episode1.aid = jsonObject1.getString("aid");
                episode1.bvid = jsonObject1.getString("bvid");

                String badge = jsonObject.getString("badge");
                episode1.badge = "".equals(badge) ? null : badge;

                episode1.cid = jsonObject1.getString("cid");
                episode1.cover = jsonObject1.getString("cover");

                episode1.title = jsonObject1.getString("title");
                episode1.longTitle = jsonObject1.getString("long_title");
                episode1.pubTime = jsonObject1.getLongValue("pub_time");
                episode1.shortLink = jsonObject1.getString("short_link");

                JSONObject stat = jsonObject1.getJSONObject("stat");
                episode1.coin = stat.getIntValue("coin");
                episode1.danmaku = stat.getIntValue("danmakus");
                episode1.likes = stat.getIntValue("likes");
                episode1.play = stat.getIntValue("play");
                episode1.reply = stat.getIntValue("reply");

                episode1.subTitle = jsonObject1.getString("subtitle");

                bangumiSection.episodeList.add(episode1);
            }

            bangumiSectionList.add(bangumiSection);
        }

        return bangumiSectionList;
    }
}
