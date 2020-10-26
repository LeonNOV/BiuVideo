package com.leon.biuvideo;

import android.os.Environment;

import com.leon.biuvideo.beans.upMasterBean.UpAudio;
import com.leon.biuvideo.beans.upMasterBean.UpPicture;
import com.leon.biuvideo.beans.upMasterBean.UpVideo;
import com.leon.biuvideo.utils.IDUtils;
import com.leon.biuvideo.utils.MediaUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.resourcesParseUtils.UpAudioParseUtils;
import com.leon.biuvideo.utils.resourcesParseUtils.UpPictureParseUtils;
import com.leon.biuvideo.utils.resourcesParseUtils.UpVideoParseUtils;

import org.junit.Test;

import java.io.File;
import java.util.List;

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

    //avid转bvid测试
    @Test
    public void avidTobvid() {
//        String s1 = BvidUtils.avidToBvid(170001);
//        String s2 = BvidUtils.avidToBvid(455017605);
//        String s3 = BvidUtils.avidToBvid(882584971);

//        System.out.println(s1);
//        System.out.println(s2);
//        System.out.println(s3);
    }

    //解析输入的内容
    @Test
    public void parseValue() {
        //avid转bvid
//        String bvid = BvidUtils.getBvid("av884666589");

        //解析存在于链接内的bvid
//        String bvid = BvidUtils.getBvid("https://www.bilibili.com/video/BV1Jr4y1w7RZ");

        //解析存在于链接中的avid
//        String bvid = BvidUtils.getBvid("https://www.bilibili.com/video/av455017605/");

        //解析短链
//        String bvid = BvidUtils.getBvid("https://b23.tv/zHVBKd");

        //其他情况,只返回null
//        String bvid = BvidUtils.getBvid("卢本伟牛逼");
//        String bvid = BvidUtils.getBvid("https://mvnrepository.com/artifact/com.alibaba/fastjson/1.2.74#gradle");
//        String bvid = BvidUtils.getBvid("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=88093251_47_hao_pg&wd=Java%E7%9F%AD%E9%93%BE%E8%BF%98%E5%8E%9F&oq=%25E7%259F%25AD%25E9%2593%25BE%25E8%25BF%2598%25E5%258E%259F&rsv_pq=c475ebdd000bfc10&rsv_t=ed46ZgkjtBjoinygD90GFQFVqKjqr49usVjwEAdBq1yFisGW50PdSB%2FpyxlZFxPwnl2MPpp0PRM7&rqlang=cn&rsv_enter=1&rsv_dl=tb&rsv_btype=t&inputT=865&rsv_sug3=30&rsv_sug1=15&rsv_sug7=100&rsv_sug2=0&rsv_sug4=1739&rsv_sug=1");
//        String bvid = BvidUtils.getBvid("https://show.bilibili.com/platform/home.html?msource=pc_web");
//        String bvid = BvidUtils.getBvid("https://www.bilibili.com/bangumi/play/ss34425/");
//        String bvid = BvidUtils.getBvid("BV1Jr4y1w7RZ -");

//        if (bvid == null) {
//            System.out.println("error");
//        }
//
//        System.out.println(bvid);


        //mid的获取
//        long mid = BvidUtils.getMid("https://space.bilibili.com/357612002");
        long mid = IDUtils.getMid("https://space.bilibili.com/43707221?share_medium=android&share_source=copy_link&bbid=XY6E7E663EF9E1BF2EDD5BD83C77E9B1C33CE&ts=1603558072014\n");
    }

    @Test
    public void numTest() {
        //12345.6万
        String s = ValueUtils.generateCN(12345678);
        System.out.println(s);
    }
}