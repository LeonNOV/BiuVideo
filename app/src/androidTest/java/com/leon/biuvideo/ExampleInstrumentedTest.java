package com.leon.biuvideo;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.leon.biuvideo.utils.IDUtils;
import com.leon.biuvideo.utils.LogTip;
import com.leon.biuvideo.utils.MediaUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

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

        Log.d(LogTip.blue, mid + "");

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
}