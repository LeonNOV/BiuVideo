package com.leon.biuvideo.ui.resourcesFragments;

import android.view.SurfaceHolder;

import com.dueeeke.videoplayer.player.VideoView;
import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.VideoPlayerController;

/**
 * @Author Leon
 * @Time 2021/4/2
 * @Desc
 */
public class VideoPlayerFragment extends BaseSupportFragment {
    private VideoView videoPlayerContent;

    @Override
    protected int setLayout() {
        return R.layout.video_player_fragment;
    }

    @Override
    protected void initView() {
        videoPlayerContent = findView(R.id.video_player_content);
        videoPlayerContent.setUrl("https://vd4.bdstatic.com/mda-md121r3dkiba9afy/sc/cae_h264_clips/1617327122/mda-md121r3dkiba9afy.mp4");

        VideoPlayerController videoPlayerController = new VideoPlayerController(context);
        videoPlayerController.addDefaultControlComponent("BiliBili");
        videoPlayerContent.setVideoController(videoPlayerController);

        videoPlayerContent.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        videoPlayerContent.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        videoPlayerContent.release();
    }
}
