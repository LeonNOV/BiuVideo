package com.leon.biuvideo.ui.resourcesFragment.video;

import androidx.fragment.app.FragmentManager;

import com.dueeeke.videoplayer.ijk.IjkPlayer;
import com.dueeeke.videoplayer.player.VideoView;
import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.videoControlComonents.VideoPlayerTitleView;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.ui.views.VideoPlayerController;
import com.leon.biuvideo.utils.HttpUtils;

/**
 * @Author Leon
 * @Time 2021/4/2
 * @Desc 视频播放页面
 */
public class VideoFragment extends BaseSupportFragment {
    private final String bvid;

    private VideoView<IjkPlayer> videoPlayerViewContent;

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
        videoPlayerViewContent = findView(R.id.video_player_content);

        VideoInfoAndCommentsFragment videoInfoAndCommentsFragment = new VideoInfoAndCommentsFragment(bvid);

        videoInfoAndCommentsFragment.setVideoFragmentContainerListener(new VideoInfoAndCommentsFragment.VideoFragmentContainerListener() {
            @Override
            public void playVideo(String videoUrl, String cid) {
                if (isFirstVideo) {
                    initVideoPlayer(videoUrl, cid);

                    isFirstVideo = false;
                } else {
                    videoPlayerViewContent.setUrl(videoUrl);
                    videoPlayerViewContent.start();
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
     * @param videoUrl 视频链接
     */
    private void initVideoPlayer(String videoUrl, String cid) {
        videoPlayerViewContent.setUrl(videoUrl, HttpUtils.getHeaders());
        videoPlayerController = new VideoPlayerController(context);
        videoPlayerController.setOnBackListener(new VideoPlayerTitleView.OnBackListener() {
            @Override
            public void onBack() {
                backPressed();
            }
        });
        videoPlayerController.addDefaultControlComponent("BiliBili", cid);
        videoPlayerController.addControlComponent();
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
