package com.leon.biuvideo.utils.parseDataUtils.userParseUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.orderBeans.FavoriteVideoFolderDetail;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2021/3/19
 * @Desc 用户收藏夹内容解析类
 */
public class FavoriteVideoFolderDetailParser {
    private final long folderId;

    /**
     * 单页条目数
     */
    private static final int PAGE_SIZE = 20;

    /**
     * 页码
     */
    private int pageNum = 1;

    /**
     * 数据状态
     */
    public boolean dataStatus = true;

    public FavoriteVideoFolderDetailParser(long folderId) {
        this.folderId = folderId;
    }

    /**
     * 获取用户指定收藏夹的所有数据
     *
     * @return  返回FavoriteVideoFolderDetail
     */
    public FavoriteVideoFolderDetail parseData() {
        Map<String, String> params = new HashMap<>(6);
        params.put("media_id", String.valueOf(folderId));
        params.put("pn", String.valueOf(pageNum));
        params.put("ps", String.valueOf(PAGE_SIZE));
        params.put("order", "mtime");
        params.put("type", "0");
        params.put("tid", "0");

        if (dataStatus) {
            JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.USER_FAV_FOLDER_DETAIL, Headers.of(HttpUtils.getAPIRequestHeader()), params);
            JSONObject data = responseObject.getJSONObject("data");

            if (data != null) {
                FavoriteVideoFolderDetail favoriteVideoFolderDetail = new FavoriteVideoFolderDetail();

                JSONObject info = data.getJSONObject("info");
                favoriteVideoFolderDetail.id = info.getLongValue("id");
                favoriteVideoFolderDetail.addTime = info.getLongValue("ctime");
                favoriteVideoFolderDetail.cover = info.getString("cover");
                favoriteVideoFolderDetail.count = info.getIntValue("media_count");
                favoriteVideoFolderDetail.title = info.getString("title");

                JSONObject upper = info.getJSONObject("upper");
                favoriteVideoFolderDetail.userFace = upper.getString("face");
                favoriteVideoFolderDetail.userMid = upper.getLongValue("mid");
                favoriteVideoFolderDetail.userName = upper.getString("name");

                JSONArray medias = data.getJSONArray("medias");
                favoriteVideoFolderDetail.medias = new ArrayList<>(medias.size());

                if (medias != null) {
                    for (Object o : medias) {
                        JSONObject jsonObject = (JSONObject) o;
                        FavoriteVideoFolderDetail.Media media = new FavoriteVideoFolderDetail.Media();

                        media.bvid = jsonObject.getString("bvid");
                        media.cover = jsonObject.getString("cover");
                        media.addTime = jsonObject.getLongValue("fav_time");
                        media.duration = jsonObject.getIntValue("duration");
                        media.title = jsonObject.getString("title");
                        media.desc = jsonObject.getString("intro");
                        media.link = jsonObject.getString("link");

                        JSONObject cntInfo = jsonObject.getJSONObject("cnt_info");
                        media.collect = cntInfo.getIntValue("collect");
                        media.danmaku = cntInfo.getIntValue("danmaku");
                        media.play = cntInfo.getIntValue("play");

                        JSONObject innerUpper = jsonObject.getJSONObject("upper");

                        media.mid = innerUpper.getLongValue("mid");
                        media.name = innerUpper.getString("name");

                        favoriteVideoFolderDetail.medias.add(media);
                    }
                }

                if (favoriteVideoFolderDetail.medias.size() < PAGE_SIZE) {
                    dataStatus = false;
                }

                pageNum ++;

                return favoriteVideoFolderDetail;
            }
        }

        return null;
    }
}
