package com.leon.biuvideo;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.searchResultBeans.SearchResultArticle;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void test() {
        Map<String, String> params = new HashMap<>(1);
        params.put("ids", "2077,10527333,9845027,8721242,10527829,8932728,8757788,10540634,8885684,8891066,8814519,8748337,9410694,9810826,8765259,9315833,10383349,8791697,9470577,10542286");
        JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.METAS, params);
        JSONObject data = response.getJSONObject("data");
    }
}