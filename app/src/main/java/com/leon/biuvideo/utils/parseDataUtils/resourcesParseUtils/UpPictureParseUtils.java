package com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.upMasterBean.UpPicture;
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
public class UpPictureParseUtils {

    /**
     * 解析相簿接口
     *
     * @param mid   up主id
     * @param pageNum   获取的页码，从0开始
     * @return  返回UpPicture类型集合
     */
    public static List<UpPicture> parsePicture(long mid, int pageNum) {
        Map<String, String> params = new HashMap<>();
        params.put("uid", String.valueOf(mid));
        params.put("page_num", String.valueOf(pageNum));
        params.put("page_size", String.valueOf(30));
        params.put("biz", "all");

        String response = new HttpUtils(Paths.picture, params).getData();

        JSONObject jsonObject = JSON.parseObject(response);

        JSONObject dataObject = jsonObject.getJSONObject("data");

        if (dataObject != null) {
            List<UpPicture> upPictures = new ArrayList<>();

            JSONArray items = dataObject.getJSONArray("items");

            for (Object item : items) {
                JSONObject itemObject = (JSONObject) item;

                UpPicture upPicture = new UpPicture();

                //获取音频id
                upPicture.docId = itemObject.getLong("doc_id");

                //获取上传者id
                upPicture.posterUid = itemObject.getLong("poster_uid");

                //获取标题
                upPicture.title = itemObject.getString("title");

                //获取介绍信息
                upPicture.description = itemObject.getString("description");

                //获取图片数量
                upPicture.count = itemObject.getIntValue("count");

                //获取上传日期
                upPicture.ctime = itemObject.getLongValue("ctime");

                //获取查看量
                upPicture.view = itemObject.getLong("view");

                //获取点赞量
                upPicture.like = itemObject.getLong("like");

                //获取图片信息
                upPicture.pictures = new ArrayList<>();
                for (Object picture : itemObject.getJSONArray("pictures")) {
                    JSONObject tempJSONObject = (JSONObject) picture;

                    //如果需要使用图片大小，则需要更改UpPicture中的pictures的泛型为Map类型
                    //注意，如果泛型不为String，则UserPictureAdapter中显示图片地方的参数也要进行更改
                    //tempMap.put("imgSize", tempJSONObject.getIntValue("img_size"));

                    upPicture.pictures.add(tempJSONObject.getString("img_src"));
                }

                upPictures.add(upPicture);
            }

            return upPictures;
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
    public static int getPictureCount(long mid) {
        Map<String, String> values = new HashMap<>();
        values.put("mid", String.valueOf(mid));

        String response = new HttpUtils(Paths.pictureCount, values).getData();

        JSONObject jsonObject = JSON.parseObject(response);

        JSONObject data = jsonObject.getJSONObject("data");

        return data.getIntValue("all_count");
    }
}
