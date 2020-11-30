package com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.upMasterBean.Picture;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析相簿接口
 */
public class PictureParseUtils {

    /**
     * 解析相簿接口
     *
     * @param mid   up主id
     * @param pageNum   获取的页码，从0开始
     * @return  返回UpPicture类型集合
     */
    public static List<Picture> parsePicture(long mid, int pageNum) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", String.valueOf(mid));
        params.put("page_num", String.valueOf(pageNum));
        params.put("page_size", String.valueOf(30));
        params.put("biz", "all");

        JSONObject responseObject = HttpUtils.getResponse(Paths.picture, params);
        JSONObject dataObject = responseObject.getJSONObject("data");

        if (dataObject != null) {
            List<Picture> pictures = new ArrayList<>();

            JSONArray items = dataObject.getJSONArray("items");

            for (Object item : items) {
                JSONObject itemObject = (JSONObject) item;

                Picture picture = new Picture();

                //获取音频id
                picture.docId = itemObject.getLong("doc_id");

                //获取上传者id
                picture.posterUid = itemObject.getLong("poster_uid");

                //获取标题
                picture.title = itemObject.getString("title");

                //获取介绍信息
                picture.description = itemObject.getString("description");

                //如果title为空，则使用description
                if (picture.title.equals("")) {
                    picture.title = picture.description;
                }

                //获取图片数量
                picture.count = itemObject.getIntValue("count");

                //获取上传日期
                picture.ctime = itemObject.getLongValue("ctime");

                //获取查看量
                picture.view = itemObject.getLong("view");

                //获取点赞量
                picture.like = itemObject.getLong("like");

                //获取图片信息
                picture.pictures = new ArrayList<>();
                for (Object pic : itemObject.getJSONArray("pictures")) {
                    JSONObject tempJSONObject = (JSONObject) pic;

                    //如果需要使用图片大小，则需要更改Picture中的pictures的泛型为Map类型
                    //注意，如果泛型不为String，则UserPictureAdapter中显示图片地方的参数也要进行更改
                    //tempMap.put("imgSize", tempJSONObject.getIntValue("img_size"));

                    picture.pictures.add(tempJSONObject.getString("img_src"));
                }

                pictures.add(picture);
            }

            return pictures;
        }

        Log.e(Fuck.red, "doc_list接口数据获取失败", new NullPointerException("doc_list接口数据获取失败"));
        return null;
    }

    /**
     * 获取相簿总数
     *
     * @param mid   用户ID
     * @return  返回相簿数量
     */
    public static int getPictureTotal(long mid) {
        Map<String, String> values = new HashMap<>();
        values.put("uid", String.valueOf(mid));

        JSONObject responseObject = HttpUtils.getResponse(Paths.pictureCount, values);
        JSONObject data = responseObject.getJSONObject("data");

        return data.getIntValue("all_count");
    }
}
