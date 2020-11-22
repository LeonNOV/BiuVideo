package com.leon.biuvideo.utils.searchParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.BiliUser;
import com.leon.biuvideo.beans.upMasterBean.UpInfo;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.OrderType;
import com.leon.biuvideo.utils.Paths;
import com.leon.biuvideo.utils.SearchType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class biliUserParser {
    public List<BiliUser> userParse(String keyword, int pn, OrderType orderType) {
        Map<String, String> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("search_type", SearchType.BILI_USER.value);
        params.put("page", String.valueOf(pn));
        params.put("order", orderType.value);

        HttpUtils httpUtils = new HttpUtils(Paths.search, params);
        String response = httpUtils.getData();

        JSONObject responseObject = JSONObject.parseObject(response);

        List<BiliUser> biliUsers = new ArrayList<>();

        JSONObject data = responseObject.getJSONObject("data");
        if (data != null) {
            biliUsers = parseData(data);
        }

        return biliUsers;
    }

    private List<BiliUser> parseData(JSONObject data) {
        JSONArray result = data.getJSONArray("result");

        List<BiliUser> biliUsers;
        if (result.size() != 0) {
            biliUsers = new ArrayList<>();

            for (Object o : result) {
                BiliUser biliUser = new BiliUser();
                JSONObject jsonObject = (JSONObject) o;

                //用户ID
                biliUser.mid = jsonObject.getLongValue("mid");

                //用户名称
                biliUser.name = jsonObject.getString("uname");

                //用户说明
                biliUser.usign = jsonObject.getString("usign");

                //粉丝数
                biliUser.fans = jsonObject.getIntValue("fans");

                //视频数
                biliUser.videos = jsonObject.getIntValue("videos");

                //头像链接
                biliUser.face = jsonObject.getString("upic");

                biliUsers.add(biliUser);
            }

            return biliUsers;
        }

        return null;
    }
}
