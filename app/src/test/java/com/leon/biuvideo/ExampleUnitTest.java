package com.leon.biuvideo;

import com.leon.biuvideo.beans.resourcesBeans.Danmaku;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.DanmakuParser;

import org.junit.Test;

import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void test() {
        List<Danmaku> danmakuList = new DanmakuParser().parseData("325330556");

        for (Danmaku danmaku : danmakuList) {
            System.out.println(danmaku);
        }
    }
}