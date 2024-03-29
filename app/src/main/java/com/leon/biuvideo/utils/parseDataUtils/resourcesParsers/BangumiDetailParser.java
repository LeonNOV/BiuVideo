package com.leon.biuvideo.utils.parseDataUtils.resourcesParsers;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.Bangumi;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.BangumiAnthology;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.BangumiSeason;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.BangumiSection;
import com.leon.biuvideo.greendao.dao.DaoBaseUtils;
import com.leon.biuvideo.greendao.dao.DownloadHistory;
import com.leon.biuvideo.greendao.dao.DownloadHistoryDao;
import com.leon.biuvideo.greendao.daoutils.DownloadHistoryUtils;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.PreferenceUtils;
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

    public Bangumi parseData(Context context) {
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

            JSONObject publish = result.getJSONObject("publish");
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
            bangumi.link = result.getString("link");

            bangumi.bangumiAnthologyList = getBangumiEps(result.getJSONArray("episodes"), bangumi.seasonId, bangumi.title, context);
            bangumi.bangumiSeasonList = getBangumiSeasons(result.getJSONArray("seasons"));
            bangumi.bangumiSectionList = getBangumiSections(result.getJSONArray("section"));

            if (bangumi.bangumiAnthologyList != null) {
                bangumi.anthologyCount = bangumi.bangumiAnthologyList.size();
            } else {
                bangumi.anthologyCount = 0;
            }

            bangumi.isFollow = getFollowStatus();

            return bangumi;
        } else {
            return null;
        }
    }

    /**
     * 解析本季单集数据
     *
     * @param episodes  JSONArray
     * @param seasonId  番剧Id
     * @return  BangumiEp集合
     */
    private List<BangumiAnthology> getBangumiEps(JSONArray episodes, String seasonId, String mainTitle, Context context) {
        if (episodes == null) {
            return null;
        }

        DaoBaseUtils<DownloadHistory> downloadHistoryDaoUtils = new DownloadHistoryUtils(context).getDownloadHistoryDaoUtils();

        List<BangumiAnthology> bangumiAnthologies = new ArrayList<>(episodes.size());
        for (Object o : episodes) {
            JSONObject jsonObject = (JSONObject) o;
            BangumiAnthology bangumiAnthology = new BangumiAnthology();

            bangumiAnthology.seasonId = seasonId;
            bangumiAnthology.aid = jsonObject.getString("aid");
            bangumiAnthology.bvid = jsonObject.getString("bvid");
            bangumiAnthology.cid = jsonObject.getString("cid");
            bangumiAnthology.id = jsonObject.getString("id");

            List<DownloadHistory> downloadHistoryList = downloadHistoryDaoUtils.queryByQueryBuilder(DownloadHistoryDao.Properties.LevelOneId.eq(seasonId),
                    DownloadHistoryDao.Properties.LevelTwoId.eq(bangumiAnthology.cid));

            if (downloadHistoryList.size() > 0) {
                DownloadHistory downloadHistory = downloadHistoryList.get(0);

                if (downloadHistory.getIsCompleted()) {
                    bangumiAnthology.isDownloaded = true;
                }
                bangumiAnthology.isDownloading = true;
            }

            String badge = jsonObject.getString("badge");
            bangumiAnthology.badge = "".equals(badge) ? null : badge;
            bangumiAnthology.cover = jsonObject.getString("cover");

            String title;
            if (jsonObject.containsKey("toast_title")) {
                title = jsonObject.getString("toast_title");
            } else {

                String longTitle = jsonObject.getString("long_title");
                String string = jsonObject.getString("title").trim();
                try {
                    int i = Integer.parseInt(string);
                    title = "第" + i + "话";
                } catch (NumberFormatException e) {
                    title = string;
                }

                if (longTitle != null) {
                    if (!"".equals(longTitle)) {
                        title += longTitle;
                    }
                }
            }

            bangumiAnthology.mainTitle = mainTitle;
            bangumiAnthology.subTitle = title;
            bangumiAnthology.pubTime = jsonObject.getLongValue("pub_time");
            bangumiAnthology.shortLink = jsonObject.getString("short_link");

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

                if (jsonObject1.containsKey("toast_title")) {
                    episode1.title = jsonObject1.getString("toast_title");
                } else {
                    episode1.title = jsonObject1.getString("title");
                }

                episode1.subTitle = jsonObject1.getString("subtitle");
                episode1.pubTime = jsonObject1.getLongValue("pub_time");
                episode1.shortLink = jsonObject1.getString("short_link");

                bangumiSection.episodeList.add(episode1);
            }

            bangumiSectionList.add(bangumiSection);
        }

        return bangumiSectionList;
    }

    /**
     * 获取追番状态
     *
     * @return  true：已追番，false：未追番
     */
    private boolean getFollowStatus () {
        if (PreferenceUtils.getLoginStatus()) {
            Map<String, String> params = new HashMap<>(1);
            params.put("season_id", seasonId);

            JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.BANGUMI_STATUS, Headers.of(HttpUtils.getAPIRequestHeader()), params);
            if (response.getIntValue("code") == 0) {
                return response.getJSONObject("result").getIntValue("follow") != 0;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
