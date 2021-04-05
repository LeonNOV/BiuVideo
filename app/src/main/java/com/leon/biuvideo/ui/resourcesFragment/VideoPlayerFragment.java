package com.leon.biuvideo.ui.resourcesFragment;

import com.dueeeke.videoplayer.ijk.IjkPlayer;
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
    private VideoView<IjkPlayer> videoPlayerViewContent;

    @Override
    protected int setLayout() {
        return R.layout.video_player_fragment;
    }

    @Override
    protected void initView() {
        String videoUrl = "https://cn-hbwh-cmcc-bcache-04.bilivideo.com/upgcxcode/95/80/318008095/318008095-1-32.flv?e=ig8euxZM2rNcNbNHhwdVhoMgnWdVhwdEto8g5X10ugNcXBlqNxHxNEVE5XREto8KqJZHUa6m5J0SqE85tZvEuENvNo8g2ENvNo8i8o859r1qXg8xNEVE5XREto8GuFGv2U7SuxI72X6fTr859r1qXg8gNEVE5XREto8z5JZC2X2gkX5L5F1eTX1jkXlsTXHeux_f2o859IB_&uipk=5&nbs=1&deadline=1617641492&gen=playurlv2&os=bcache&oi=3747315555&trid=9cb960e57e90466e98577e0e1b7c8d45u&platform=pc&upsig=4742b7376bd42cc1cc41d3cbbbddf94a&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&cdnid=10200&mid=0&orderid=0,3&agrr=1&logo=80000000";

        videoPlayerViewContent = findView(R.id.video_player_content);
        videoPlayerViewContent.setUrl(videoUrl, HttpUtils.getHeaders());

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
