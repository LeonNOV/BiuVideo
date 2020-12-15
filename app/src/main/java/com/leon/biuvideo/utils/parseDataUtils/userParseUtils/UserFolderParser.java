package com.leon.biuvideo.utils.parseDataUtils.userParseUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.userBeans.UserFolder;
import com.leon.biuvideo.beans.userBeans.UserFolderData;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.values.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

public class UserFolderParser {
    /**
     * 获取用户所有收藏夹数据
     *
     * @param mid   用户ID
     * @param cookie    用户Cookie
     * @return  返回UserFolder集合
     */
    public List<UserFolder> parseUserFolder(long mid, String cookie) {
        Map<String, String> params = new HashMap<>();
        params.put("up_mid", String.valueOf(mid));

        JSONObject responseObject;
        if (cookie != null) {
            responseObject = HttpUtils.getResponse(Paths.userAllFolder, Headers.of("Cookie", cookie), params);
        } else {
            responseObject = HttpUtils.getResponse(Paths.userAllFolder, params);
        }

        JSONObject data = responseObject.getJSONObject("data");

        if (data != null) {
            JSONArray list = data.getJSONArray("list");
            List<UserFolder> userFolders = new ArrayList<>();

            for (Object o : list) {
                JSONObject jsonObject = (JSONObject) o;
                UserFolder userFolder = new UserFolder();

                userFolder.id  = jsonObject.getLongValue("id");
                userFolder.total = jsonObject.getIntValue("media_count");
                userFolder.mid = jsonObject.getLongValue("mid");
                userFolder.title = jsonObject.getString("title");

                userFolders.add(userFolder);
            }

            return userFolders;
        }

        return null;
    }

    /**
     * 获取用户指定收藏夹的所有数据
     *
     * @param cookie    用户Cookie
     * @param mediaId   收藏夹ID
     * @param pageNum   页码
     * @return  返回UserFolderData对象
     */
    public UserFolderData parseUserFolderData(String cookie, long mediaId, int pageNum) {
        Map<String, String> params = new HashMap<>();
        params.put("media_id", String.valueOf(mediaId));
        params.put("pn", String.valueOf(pageNum));
        params.put("ps", "20");
        params.put("order", "mtime");
        params.put("type", "0");
        params.put("tid", "0");

        JSONObject responseObject;
        if (cookie != null) {
            responseObject = HttpUtils.getResponse(Paths.userFolderData, Headers.of("Cookie", cookie), params);
        } else {
            responseObject = HttpUtils.getResponse(Paths.userFolderData, params);
        }

        JSONObject data = responseObject.getJSONObject("data");
        if (data != null) {
            UserFolderData userFolderData = new UserFolderData();

            JSONObject info = data.getJSONObject("info");
            userFolderData.id = info.getLongValue("id");
            userFolderData.ctime = info.getLongValue("ctime");
            userFolderData.cover = info.getString("cover");
            userFolderData.total = info.getIntValue("media_count");
            userFolderData.title = info.getString("title");

            JSONObject upper = info.getJSONObject("upper");
            userFolderData.userFace = upper.getString("face");
            userFolderData.userMid = upper.getLongValue("mid");
            userFolderData.userName = upper.getString("name");

            userFolderData.medias = new ArrayList<>();
            JSONArray medias = data.getJSONArray("medias");

            for (Object o : medias) {
                JSONObject jsonObject = (JSONObject) o;
                UserFolderData.Media media = new UserFolderData.Media();

                media.bvid = jsonObject.getString("bvid");
                media.cover = jsonObject.getString("cover");
                media.addTime = jsonObject.getLongValue("fav_time");
                media.duration = jsonObject.getIntValue("duration");
                media.title = jsonObject.getString("title");
                media.desc = jsonObject.getString("intro");
                media.link = jsonObject.getString("link");

                JSONObject cnt_info = jsonObject.getJSONObject("cnt_info");
                media.collect = cnt_info.getIntValue("collect");
                media.danmaku = cnt_info.getIntValue("danmaku");
                media.play = cnt_info.getIntValue("play");

                JSONObject innerUpper = jsonObject.getJSONObject("upper");

                media.mid = innerUpper.getLongValue("mid");
                media.name = innerUpper.getString("name");

                userFolderData.medias.add(media);
            }

            return userFolderData;
        }

        return null;
    }
}