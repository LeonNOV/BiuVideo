package com.leon.biuvideo.ui.resourcesFragments;

import com.dueeeke.videoplayer.player.VideoView;
import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.VideoPlayerController;
import com.leon.biuvideo.utils.HttpUtils;

/**
 * @Author Leon
 * @Time 2021/4/2
 * @Desc 视频播放页面
 */
public class VideoPlayerFragment extends BaseSupportFragment {
    private VideoView<BiuVideoPlayer> videoPlayerViewContent;

    @Override
    protected int setLayout() {
        return R.layout.video_player_fragment;
    }

    @Override
    protected void initView() {
        String videoUrl = "https://cn-jxnc-cmcc-bcache-07.bilivideo.com/upgcxcode/95/80/318008095/318008095-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1617616102&gen=playurlv2&os=bcache&oi=3747315518&trid=548f782817cb4c25bc3ea8e10aa205c8u&platform=pc&upsig=caac5ed5242fdfdc05c838723d7dbac9&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&cdnid=6634&mid=0&orderid=0,3&agrr=1&logo=80000000";
        String audioUrl = "https://upos-sz-mirrorcoso1.bilivideo.com/upgcxcode/95/80/318008095/318008095-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1617616102&gen=playurlv2&os=coso1bv&oi=3747315518&trid=548f782817cb4c25bc3ea8e10aa205c8u&platform=pc&upsig=f0560519cabcce75d123bd43cd22e71b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&mid=0&orderid=0,3&agrr=1&logo=80000000";

        videoPlayerViewContent = findView(R.id.video_player_content);
        videoPlayerViewContent.setUrl(videoUrl, audioUrl, HttpUtils.getHeaders());

        VideoPlayerController videoPlayerController = new VideoPlayerController(context);
        videoPlayerController.addDefaultControlComponent("BiliBili");
        videoPlayerViewContent.setVideoController(videoPlayerController);

        videoPlayerViewContent.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        videoPlayerViewContent.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        videoPlayerViewContent.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        videoPlayerViewContent.release();
    }
}
