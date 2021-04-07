package com.leon.biuvideo;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.mediaBeans.videoBeans.VideoWithFlv;
import com.leon.biuvideo.beans.searchResultBeans.SearchResultArticle;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils.VideoWithFlvParser;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void test() {
        Map<String, String> testMap = new HashMap<>();
        testMap.put("[doge]", "111");
        testMap.put("[笑]", "222");
        String str = "或者是直[doge]接[doge]主[doge]线剧情[笑]送[笑]";

        for (Map.Entry<String, String> entry : testMap.entrySet()) {
            String key = entry.getKey();

            Pattern compile = Pattern.compile(key.replaceAll("\\[", "\\\\[").replaceAll("]", "\\\\]"));
            Matcher matcher = compile.matcher(str);

            List<String> indexList = new ArrayList<>();
            while (matcher.find()) {
                int[] position = new int[2];
                position[0] = matcher.start();
                position[1] = matcher.start() + key.length();

                indexList.add(Arrays.toString(position));
            }

            System.out.println(indexList);
        }
    }
}