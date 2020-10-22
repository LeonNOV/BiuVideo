package com.leon.biuvideo.utils.resourcesParseUtils;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.upMasterBean.UpPicture;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.LogTip;
import com.leon.biuvideo.utils.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析相簿接口
 */
public class UpPictureParseUtils {

    /**
     * 解析相簿接口
     *
     * @param mid   up主id
     * @param pageNum   获取的页码，从0开始
     * @return  返回UpPicture类型集合
     */
    public static List<UpPicture> parsePicture(long mid, int pageNum) {
        Map<String, Object> values = new HashMap<>();
        values.put("uid", mid);
        values.put("page_num", pageNum);
        values.put("page_size", 30);
        values.put("biz", "all");

        String response = HttpUtils.GETByParam(Paths.picture, values);

        JSONObject jsonObject = JSON.parseObject(response);

        JSONObject dataObject = jsonObject.getJSONObject("data");

        if (dataObject != null) {
            List<UpPicture> upPictures = new ArrayList<>();

            JSONArray items = dataObject.getJSONArray("items");

            for (Object item : items) {
                JSONObject itemObject = (JSONObject) item;

                UpPicture upPicture = new UpPicture();

                //获取音频id
                Long docId = itemObject.getLong("doc_id");
                upPicture.docId = docId;

                //获取上传者id
                Long posterUid = itemObject.getLong("poster_uid");
                upPicture.posterUid = posterUid;

                //获取标题
                String title = itemObject.getString("title");
                upPicture.title = title;

                //获取介绍信息
                String description = itemObject.getString("description");
                upPicture.description = description;

                //获取图片数量
                int count = itemObject.getIntValue("count");
                upPicture.count = count;

                //获取查看量
                Long view = itemObject.getLong("view");
                upPicture.view = view;

                //获取点赞量
                Long like = itemObject.getLong("like");
                upPicture.like = like;

                //获取第一张图片的链接
                JSONObject picObject = (JSONObject) itemObject.getJSONArray("pictures").get(0);
                upPicture.pictureUrl = picObject.getString("img_src");

                upPictures.add(upPicture);
            }

            return upPictures;
        }


        Log.e(LogTip.red, "doc_list接口数据获取失败", new NullPointerException("doc_list接口数据获取失败"));
        return null;
    }
}
