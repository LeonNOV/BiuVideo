package com.leon.biuvideo.utils.parseDataUtils.userDataParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.homeBeans.favoriteBeans.FavoriteVideoFolder;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserInterface;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2020/12/15
 * @Desc 获取用户所有收藏夹数据
 */
public class FavoriteVideoFolderParser extends ParserInterface<FavoriteVideoFolder> {
    @Override
    public List<FavoriteVideoFolder> parseData() {
        if (PreferenceUtils.getLoginStatus()) {

            Map<String, String> params = new HashMap<>(1);
            params.put("up_mid", PreferenceUtils.getUserId());

            JSONObject responseObject = HttpUtils.getResponse(BiliBiliAPIs.USER_FAV_FOLDER, Headers.of(HttpUtils.getAPIRequestHeader()), params);
            JSONObject data = responseObject.getJSONObject("data");

            JSONArray list = data.getJSONArray("list");
            List<FavoriteVideoFolder> favoriteVideoFolders = new ArrayList<>(list.size());

            for (Object o : list) {
                JSONObject jsonObject = (JSONObject) o;
                FavoriteVideoFolder favoriteVideoFolder = new FavoriteVideoFolder();

                favoriteVideoFolder.id = jsonObject.getLongValue("id");
                favoriteVideoFolder.count = jsonObject.getIntValue("media_count");
                favoriteVideoFolder.mid = jsonObject.getLongValue("mid");
                favoriteVideoFolder.title = jsonObject.getString("title");

                favoriteVideoFolders.add(favoriteVideoFolder);
            }

            return favoriteVideoFolders;
        } else {
            return null;
        }
    }
}