package com.leon.biuvideo.utils.parseDataUtils.biliUserResourcesParseUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.biliUserResourcesBeans.BiliUserPicture;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.parseDataUtils.ParserInterface;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/4/10
 * @Desc B站用户相簿数据
 */
public class BiliUserPicturesParser implements ParserInterface<BiliUserPicture> {
    private final String mid;

    private final static String PAGE_SIZE = "30";
    private int pageNum = 0;
    private int total = -1;
    private int currentCount = 0;

    public boolean dataStatus = true;

    public BiliUserPicturesParser(String mid) {
        this.mid = mid;
    }

    @Override
    public List<BiliUserPicture> parseData() {
        Map<String, String> params = new HashMap<>(4);
        params.put("uid", mid);
        params.put("page_num", String.valueOf(pageNum));
        params.put("page_size", PAGE_SIZE);
        params.put("biz", "all");

        if (dataStatus) {
            // 先获取总个数
            if (total == -1) {
                Map<String, String> map = new HashMap<>(1);
                map.put("uid", mid);

                JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.BILI_USER_PICTURE_TOTAL, map);
                total = response.getJSONObject("data").getIntValue("all_count");
                if (total == 0) {
                    return null;
                }
            }

            JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.BILI_USER_PICTURE, params);
            JSONArray jsonArray = response.getJSONObject("data").getJSONArray("items");
            List<BiliUserPicture> biliUserPictureList = new ArrayList<>(jsonArray.size());
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                BiliUserPicture biliUserPicture = new BiliUserPicture();

                biliUserPicture.id = jsonObject.getString("doc_id");
                biliUserPicture.desc = jsonObject.getString("description");
                biliUserPicture.cover = ((JSONObject) jsonObject.getJSONArray("pictures").get(0)).getString("img_src");
                biliUserPicture.count = jsonObject.getIntValue("count");
                biliUserPicture.view = ValueUtils.generateCN(jsonObject.getIntValue("view"));
                biliUserPicture.like = ValueUtils.generateCN(jsonObject.getIntValue("like"));

                biliUserPictureList.add(biliUserPicture);
            }

            currentCount += biliUserPictureList.size();

            if (currentCount == total) {
                dataStatus = false;
            }

            pageNum ++;

            return biliUserPictureList;
        }

        return null;
    }
}
