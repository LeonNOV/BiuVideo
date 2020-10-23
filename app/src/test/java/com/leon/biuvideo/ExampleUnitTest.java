package com.leon.biuvideo;

import com.leon.biuvideo.beans.upMasterBean.UpAudio;
import com.leon.biuvideo.beans.upMasterBean.UpPicture;
import com.leon.biuvideo.beans.upMasterBean.UpVideo;
import com.leon.biuvideo.utils.resourcesParseUtils.UpAudioParseUtils;
import com.leon.biuvideo.utils.resourcesParseUtils.UpPictureParseUtils;
import com.leon.biuvideo.utils.resourcesParseUtils.UpVideoParseUtils;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    //video接口测试
    @Test
    public void video() {
        List<UpVideo> upVideos = UpVideoParseUtils.parseVideo(49405324, 1);

        System.out.println("page1count:" + upVideos.size());

        for (UpVideo upVideo : upVideos) {
            System.out.println(upVideo);
        }
    }

    //audio接口测试
    @Test
    public void audio() {
        List<UpAudio> upAudios = UpAudioParseUtils.parseAudio(49405324, 1);

        System.out.println("page1count:" + upAudios.size());

        for (UpAudio upAudio : upAudios) {
            System.out.println(upAudio);
        }
    }

    //picture接口测试
    @Test
    public void picture() {
        List<UpPicture> upPictures = UpPictureParseUtils.parsePicture(49405324, 0);

        System.out.println("page0count:" + upPictures.size());

        for (UpPicture upPicture : upPictures) {
            System.out.println(upPicture);
        }
    }
}