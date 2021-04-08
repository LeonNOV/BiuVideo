package com.leon.biuvideo;

import android.text.Spanned;
import android.text.method.LinkMovementMethod;

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

        testMap.put("[脱单doge]", "111");
        testMap.put("[笑]", "222");
        String str = "若有忠 : 回复 @闇の火 :一些博士抽不到的原因找到了[笑]，池子里的夕都跑出来了。[脱单doge][脱单doge][脱单doge]";

        for (Map.Entry<String, String> entry : testMap.entrySet()) {
            String key = entry.getKey();

            Pattern compile = Pattern.compile(key.replaceAll("\\[", "\\\\[").replaceAll("]", "\\\\]"));
            Matcher matcher = compile.matcher(str);

            while (matcher.find()) {
                int startIndex = matcher.start();
                System.out.println("startIndex：" + startIndex + "----endIndex：" + (startIndex + key.length()));
            }
        }

        String replay = "@" + "闇の火";
        int replayStartIndex = str.indexOf(replay);

        System.out.println("name startIndex：" + replayStartIndex + "----enIndex：" + (replayStartIndex + replay.length()));
    }
}