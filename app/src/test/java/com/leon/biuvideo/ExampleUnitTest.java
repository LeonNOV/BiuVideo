package com.leon.biuvideo;

import com.leon.biuvideo.beans.userBeans.UserFolderData;
import com.leon.biuvideo.utils.IDUtils;
import com.leon.biuvideo.utils.downloadUtils.ResourceUtils;
import com.leon.biuvideo.utils.parseDataUtils.userParseUtils.UserFolderParser;
import com.leon.biuvideo.values.Paths;
import com.leon.biuvideo.utils.ValueFormat;

import org.junit.Test;

import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    //avid转bvid测试
    @Test
    public void avidTobvid() {
//        String s1 = IDUtils.avidToBvid(373342471);
//        String s2 = IDUtils.avidToBvid(455017605);
//        String s3 = IDUtils.avidToBvid(882584971);

//        System.out.println(s1);
//        System.out.println(s2);
//        System.out.println(s3);
    }

    //解析输入的内容
    @Test
    public void parseValue() {
        //avid转bvid
//        String bvid = IDUtils.getBvid("av884666589");

        //解析存在于链接内的bvid
//        String bvid = IDUtils.getBvid("https://www.bilibili.com/video/BV1Jr4y1w7RZ");

        //解析存在于链接中的avid
//        String bvid = IDUtils.getBvid("https://www.bilibili.com/video/av455017605/");

        //解析短链
        String bvid = IDUtils.getBvid("https://b23.tv/zHVBKd");

        //其他情况,只返回null
//        String bvid = IDUtils.getBvid("卢本伟牛逼");
//        String bvid = IDUtils.getBvid("https://mvnrepository.com/artifact/com.alibaba/fastjson/1.2.74#gradle");
//        String bvid = IDUtils.getBvid("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=88093251_47_hao_pg&wd=Java%E7%9F%AD%E9%93%BE%E8%BF%98%E5%8E%9F&oq=%25E7%259F%25AD%25E9%2593%25BE%25E8%25BF%2598%25E5%258E%259F&rsv_pq=c475ebdd000bfc10&rsv_t=ed46ZgkjtBjoinygD90GFQFVqKjqr49usVjwEAdBq1yFisGW50PdSB%2FpyxlZFxPwnl2MPpp0PRM7&rqlang=cn&rsv_enter=1&rsv_dl=tb&rsv_btype=t&inputT=865&rsv_sug3=30&rsv_sug1=15&rsv_sug7=100&rsv_sug2=0&rsv_sug4=1739&rsv_sug=1");
//        String bvid = IDUtils.getBvid("https://show.bilibili.com/platform/home.html?msource=pc_web");
//        String bvid = IDUtils.getBvid("https://www.bilibili.com/bangumi/play/ss34425/");
//        String bvid = IDUtils.getBvid("BV1Jr4y1w7RZ -");
//
        if (bvid == null) {
            System.out.println("error");
        }

        System.out.println(bvid);


        //mid的获取
//        long mid = IDUtils.getMid("https://space.bilibili.com/357612002");
//        long mid = IDUtils.getMid("https://space.bilibili.com/43707221?share_medium=android&share_source=copy_link&bbid=XY6E7E663EF9E1BF2EDD5BD83C77E9B1C33CE&ts=1603558072014\n");

//        long mid = IDUtils.getMid("https://space.bilibili.com/2946474/");
//        long mid = IDUtils.getMid("2946474");
//
//        System.out.println(mid);

        /*
        _uuid=4FCBEFFE-37E1-F245-CB10-7CF738E72D6380257infoc;
        buvid3=6FD9D7A0-3E12-4F30-9F9D-4631FCF582C9143074infoc;
        sid=7ek06vbc;
        rpdid=|(kRk~l)kkJ0J'ulmYmYYl|m;
        PVID=1;
        blackside_state=1;
        CURRENT_FNVAL=80;
        LIVE_BUVID=AUTO8415999728803861;
        bp_t_offset_49405324=448570039417205401;
        CURRENT_QUALITY=112;
        DedeUserID=49405324;
        DedeUserID__ckMd5=f8c809abf9ac0926;
        SESSDATA=1ef0257a%2C1620630626%2C16d0e*b1;
        bili_jct=714fc3e3db95ece3fc7d892a447b7292;
        bp_video_offset_49405324=457166987692434151;
        bfe_id=393becc67cde8e85697ff111d724b3c8
        */
    }

    @Test
    public void numTest() {
        //12345.6万
        String s = ValueFormat.generateCN(12345678);
        System.out.println(s);
    }

    //网页源码获取测试
    @Test
    public void HtmlSource() {
        String path = Paths.articleWebPage + "8107744";
    }

    //size转换测试
    @Test
    public void sizeFormat() {
        //4653470B

        //2,719,458,939
//        long size = 2719458939L;

        long size = 465347 * 1024 * 10;

//        String s = ValueFormat.sizeFormat(size);

//        System.out.println(s);
    }

    @Test
    public void HistoryTest() {
        UserFolderParser userFolderParser = new UserFolderParser();
//        List<UserFolder> userFolders = userFolderParser.parseUserFolder(49405324, null);
//
//        for (UserFolder userFolder : userFolders) {
//            System.out.println(userFolder.id + "----" + userFolder.title);
//        }

        UserFolderData userFolderData = userFolderParser.parseUserFolderData(null, 153608924, 1);
        List<UserFolderData.Media> medias = userFolderData.medias;

        for (UserFolderData.Media media : medias) {
            System.out.println(media.title);
        }
    }

    @Test
    public void ContentSize() {
        long resourcesSize1 = ResourceUtils.getResourcesSize("https://upos-sz-mirrorks3.bilivideo.com/upgcxcode/77/38/205363877/205363877-1-30011.m4s");
        long resourcesSize2 = ResourceUtils.getResourcesSize("https://upos-sz-mirrorkodo.bilivideo.com/upgcxcode/77/38/205363877/205363877_nb2-1-30280.m4s");

        System.out.println(resourcesSize1);
        System.out.println(resourcesSize2);
    }
}