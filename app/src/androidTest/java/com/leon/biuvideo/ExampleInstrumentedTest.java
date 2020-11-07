package com.leon.biuvideo;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.leon.biuvideo.utils.IDUtils;
import com.leon.biuvideo.utils.Fuck;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.leon.biuvideo", appContext.getPackageName());
    }

    //音视频合成测试
    @Test
    public void mergeMedia () {
        String mid = IDUtils.getBvid("https://b23.tv/zHVBKd");

        Log.d(Fuck.blue, mid + "");

        /*File directory = Environment.getExternalStorageDirectory();
        String absolutePath = directory.getAbsolutePath();

        ///document/raw:/storage/emulated/0/Download/biuvideo/audio.mp4

        String video = absolutePath + "/Download/biuvideo/video.mp4";
        String audio = absolutePath + "/Download/biuvideo/audio.mp4";
        String outPutPath = absolutePath + "/Download/biuvideo/result.mp4";

        Log.d(LogTip.blue, "-------------------------------------------");
        Log.d(LogTip.blue, new File(video).exists() + "");
        Log.d(LogTip.blue, new File(audio).exists() + "");
        Log.d(LogTip.blue, new File(outPutPath).exists() + "");
        Log.d(LogTip.blue, "-------------------------------------------");

        long begin = System.currentTimeMillis();

        boolean b = MediaUtils.ComposeTrack(video, audio, outPutPath);

        long end = System.currentTimeMillis();

        Log.d(LogTip.blue, b + "");
        Log.d(LogTip.blue, end - begin + "");*/
    }

    @Test
    public void mediaPlayerTest() {
        MediaPlayer mediaPlayer = new MediaPlayer();

        //创建uri
        Uri uri = Uri.parse("https://upos-sz-mirrorks3.bilivideo.com/ugaxcode/i180417qn2bstvuhxpyf481s9ikopjnj-192k.m4a?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1603858364&gen=playurl&os=ks3bv&oi=3747315513&trid=6feaa096bf334e36af21f829d1e5fc33B&platform=pc&upsig=c7f142659f2416b2257ace439247ff9f&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&mid=0&orderid=0,1&logo=00000000");

        //设置headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Referer", "https://www.bilibili.com/");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36 Edg/86.0.622.51");
        headers.put("Host", uri.getHost());
    }
}