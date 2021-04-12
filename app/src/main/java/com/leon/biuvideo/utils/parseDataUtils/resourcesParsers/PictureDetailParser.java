package com.leon.biuvideo.utils.parseDataUtils.resourcesParsers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.resourcesBeans.PictureDetail;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.Role;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2021/4/12
 * @Desc 获取相簿详细数据
 */
public class PictureDetailParser {
    private static final String TYPE = "2";

    public static PictureDetail parseData (String rid) {
        Map<String, String> params = new HashMap<>(2);
        params.put("rid", rid);
        params.put("type", TYPE);

        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.BILI_USER_PICTURE_DETAIL, Headers.of(HttpUtils.getAPIRequestHeader()), params);
        if (response.getIntValue("code") == 0) {
            JSONObject data = response.getJSONObject("data");
            PictureDetail pictureDetail = new PictureDetail();

            JSONObject card = data.getJSONObject("card");

            JSONObject desc = card.getJSONObject("desc");
            pictureDetail.view = ValueUtils.generateCN(desc.getIntValue("view"));
            pictureDetail.comment = ValueUtils.generateCN(desc.getIntValue("comment"));
            pictureDetail.like = ValueUtils.generateCN(desc.getIntValue("like"));
            pictureDetail.isLiked = desc.getIntValue("is_liked") == 1;
            pictureDetail.pubTime = ValueUtils.generateTime(desc.getLongValue("timestamp"), "yyyy-MM-dd HH:mm", true);

            JSONObject userProfile = desc.getJSONObject("user_profile");
            JSONObject info = userProfile.getJSONObject("info");
            pictureDetail.userName = info.getString("uname");
            pictureDetail.userMid = info.getString("uid");
            pictureDetail.userFace = info.getString("face");

            JSONObject vip = userProfile.getJSONObject("vip");
            int vipStatus = vip.getIntValue("vipStatus");
            pictureDetail.isVip = vipStatus == 1;

            int type  = userProfile.getJSONObject("card").getJSONObject("official_verify").getIntValue("type");
            pictureDetail.role = type == 0 ? Role.PERSON : type == 1 ? Role.OFFICIAL : Role.NONE;

            JSONObject relation = card.getJSONObject("display").getJSONObject("relation");
            pictureDetail.isFollow = relation.getIntValue("is_follow") == 1;

            JSONObject innerCard = (JSONObject) JSONObject.parse(card.getString("card"));
            JSONObject item = innerCard.getJSONObject("item");
            String title = item.getString("title");
            pictureDetail.title = "".equals(title) ? null : title;
            pictureDetail.desc = item.getString("description");
            pictureDetail.picturesCount = item.getIntValue("pictures_count");

            pictureDetail.pictures = new HashMap<>(pictureDetail.picturesCount);
            if (pictureDetail.picturesCount > 0) {
                JSONArray pictures = item.getJSONArray("pictures");

                for (int i = 0; i < pictures.size(); i++) {
                    JSONObject jsonObject = (JSONObject) pictures.get(i);

                    double imgSize = jsonObject.getDoubleValue("img_size");
                    String picSize = String.format(Locale.CHINA, "%.2f", (imgSize / 1024)) + "MB";

                    String[] strings = {jsonObject.getString("img_src"), picSize};
                    pictureDetail.pictures.put(i, strings);
                }
            }

            return pictureDetail;
        }

        return null;
    }
}
