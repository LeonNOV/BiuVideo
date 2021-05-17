package com.leon.biuvideo.ui.resourcesFragment.video.contribution;

import androidx.fragment.app.FragmentManager;

import com.dueeeke.videoplayer.ijk.IjkPlayer;
import com.dueeeke.videoplayer.player.VideoView;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoWithFlv;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.VideoStatListener;
import com.leon.biuvideo.ui.resourcesFragment.video.videoControlComonents.VideoPlayerTitleView;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.ui.views.VideoPlayerController;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.HttpUtils;

/**
 * @Author Leon
 * @Time 2021/4/2
 * @Desc 视频播放页面
 */
public class VideoFragment extends BaseSupportFragment {
    private final String bvid;

    private VideoView<IjkPlayer> videoPlayerContent;

    private boolean isFirstVideo = true;
    private VideoPlayerController videoPlayerController;

    public VideoFragment(String bvid) {
        this.bvid = bvid;
    }

    @Override
    protected int setLayout() {
        return R.layout.video_fragment;
    }

    @Override
    protected void initView() {
        videoPlayerContent = findView(R.id.video_player_content);

        VideoInfoAndCommentsFragment videoInfoAndCommentsFragment = new VideoInfoAndCommentsFragment(bvid);
        videoInfoAndCommentsFragment.setVideoStatListener(new VideoStatListener() {
            @Override
            public void playVideo(String title, VideoWithFlv videoWithFlv, int videoStreamIndex) {
                if (isFirstVideo) {
                    initVideoPlayer(videoWithFlv);

                    isFirstVideo = false;
                } else {
                    videoPlayerContent.release();
                    videoPlayerContent.setUrl(videoWithFlv.videoStreamInfoList.get(videoStreamIndex).url, HttpUtils.getVideoPlayHeaders(false, bvid));

                    // 重新设置弹幕
                    videoPlayerController.resetDanmaku(videoWithFlv.cid);
                    videoPlayerContent.start();
                }

                try {
                    videoPlayerController.setTitle(title);
                } catch (NullPointerException e) {
                    Fuck.red("bvid----" + bvid);
                    e.printStackTrace();
                }
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

    /**
     * 第一次加载视频
     *
     * @param videoWithFlv 视频画质信息
     */
    private void initVideoPlayer(VideoWithFlv videoWithFlv) {
        if (videoWithFlv == null) {
            SimpleSnackBar.make(getActivity().getWindow().getDecorView(), "获取数据失败", SimpleSnackBar.LENGTH_LONG).show();
            backPressed();
            return;
        }

        videoPlayerContent.setUrl(videoWithFlv.videoStreamInfoList.get(0).url, HttpUtils.getVideoPlayHeaders(false, bvid));
        videoPlayerController = new VideoPlayerController(context, videoWithFlv);
        videoPlayerController.setOnBackListener(new VideoPlayerTitleView.OnBackListener() {
            @Override
            public void onBack() {
                backPressed();
            }
        });
        videoPlayerController.addDefaultControlComponent("BiliBili", videoWithFlv.cid);
        videoPlayerController.addControlComponent();

        videoPlayerContent.setVideoController(videoPlayerController);

        videoPlayerContent.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (videoPlayerController != null) {
            videoPlayerController.pauseDanmaku();
        }
        videoPlayerContent.pause();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (videoPlayerController != null) {
            videoPlayerController.resumeDanmaku();
        }
        videoPlayerContent.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (videoPlayerController != null) {
            videoPlayerController.releaseDanmaku();
        }
        videoPlayerContent.release();
    }
}
