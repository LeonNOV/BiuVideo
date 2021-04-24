package com.leon.biuvideo.ui.resourcesFragment.video.bangumi;

import androidx.fragment.app.FragmentManager;

import com.dueeeke.videoplayer.ijk.IjkPlayer;
import com.dueeeke.videoplayer.player.VideoView;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoWithFlv;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.VideoInfoAndCommentsFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.VideoStatListener;
import com.leon.biuvideo.ui.resourcesFragment.video.videoControlComonents.VideoPlayerTitleView;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.ui.views.VideoPlayerController;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.wraps.VideoSpeedWrap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @Author Leon
 * @Time 2021/4/23
 * @Desc 番剧播放页面
 */
public class BangumiFragment extends BaseSupportFragment {
    private final String seasonId;
    private boolean isFirstVideo = true;
    private VideoPlayerController videoPlayerController;

    public BangumiFragment(String seasonId) {
        this.seasonId = seasonId;
    }

    private VideoView<IjkPlayer> videoPlayerViewContent;

    @Override
    protected int setLayout() {
        return R.layout.video_fragment;
    }

    @Override
    protected void initView() {
        // 注册监听
        EventBus.getDefault().register(this);

        videoPlayerViewContent = findView(R.id.video_player_content);

        VideoInfoAndCommentsFragment videoInfoAndCommentsFragment = new VideoInfoAndCommentsFragment(seasonId,  true);
        videoInfoAndCommentsFragment.setVideoStatListener(new VideoStatListener() {
            @Override
            public void playVideo(String title, VideoWithFlv videoWithFlv, int videoStreamIndex) {
                if (isFirstVideo) {
                    initVideoPlayer(videoWithFlv);
                    isFirstVideo = false;
                } else {
                    videoPlayerViewContent.release();
                    videoPlayerViewContent.setUrl(videoWithFlv.videoStreamInfoList.get(videoStreamIndex).url, HttpUtils.getHeaders());

                    // 重新设置弹幕
                    videoPlayerController.resetDanmaku(videoWithFlv.cid);
                    videoPlayerViewContent.start();
                }

                videoPlayerController.setTitle(title);
            }

            @Override
            public void onError() {
                SimpleSnackBar.make(getActivity().getWindow().getDecorView(), "获取数据失败", SimpleSnackBar.LENGTH_LONG).show();
                backPressed();
            }
        });

        if (findChildFragment(videoInfoAndCommentsFragment.getClass()) == null) {
            loadRootFragment(R.id.video_fragment_container, videoInfoAndCommentsFragment);
        }
    }

    /**
     * 第一次加载视频
     *
     * @param videoWithFlv 视频画质信息
     */
    private void initVideoPlayer(VideoWithFlv videoWithFlv) {
        videoPlayerViewContent.setUrl(videoWithFlv.videoStreamInfoList.get(0).url, HttpUtils.getHeaders());
        videoPlayerController = new VideoPlayerController(context, videoWithFlv);
        videoPlayerController.setOnBackListener(new VideoPlayerTitleView.OnBackListener() {
            @Override
            public void onBack() {
                backPressed();
            }
        });
        videoPlayerController.addDefaultControlComponent("BiliBili", videoWithFlv.cid);
        videoPlayerController.addControlComponent();

        videoPlayerViewContent.setVideoController(videoPlayerController);

        videoPlayerViewContent.start();
    }

    @Override
    public boolean onBackPressedSupport() {
        FragmentManager childFragmentManager = getChildFragmentManager();

        // 如果子Fragment的个数等于2，则意味着VideoCommentDetailFragment存在于子管理器当中
        // 等于1则弹出VideoFragment
        if (childFragmentManager.getFragments().size() == 2) {
            popChild();
        } else {
            onDestroy();
            pop();
        }

        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetSpeedMessage (VideoSpeedWrap videoSpeedWrap) {
        videoPlayerViewContent.setSpeed(videoSpeedWrap.speed);
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
