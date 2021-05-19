package com.leon.biuvideo.ui.resourcesFragment.video.contribution;

import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;

import com.dueeeke.videoplayer.ijk.IjkPlayer;
import com.dueeeke.videoplayer.player.VideoView;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoWithFlv;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.OnVideoAnthologyListener;
import com.leon.biuvideo.ui.resourcesFragment.video.videoControlComonents.VideoPlayerTitleView;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.ui.views.VideoPlayerController;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.VideoWithFlvParser;
import com.leon.biuvideo.wraps.DanmakuWrap;
import com.leon.biuvideo.wraps.VideoQualityWrap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @Author Leon
 * @Time 2021/4/2
 * @Desc 视频播放页面
 */
public class VideoFragment extends BaseSupportFragment {
    private final String bvid;

    private VideoView<IjkPlayer> videoPlayerContent;

    private boolean isInitialize = false;
    private VideoPlayerController videoPlayerController;
    private ImageView videoDanmakuStatus;

    private VideoWithFlvParser videoWithFlvParser;
    private String cid;
    private String title;
    private boolean isMovie;

    public VideoFragment(String bvid) {
        this.bvid = bvid;
    }

    @Override
    protected int setLayout() {
        return R.layout.bangumi_fragment;
    }

    @Override
    protected void initView() {
        // 注册监听
        EventBus.getDefault().register(this);

        videoPlayerContent = findView(R.id.video_player_content);
        videoDanmakuStatus = findView(R.id.video_danmaku_status);
        videoDanmakuStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean selected = videoDanmakuStatus.isSelected();
                videoDanmakuStatus.setSelected(!selected);
                EventBus.getDefault().post(DanmakuWrap.getInstance(!selected));
            }
        });

        VideoInfoFragment videoInfoFragment = new VideoInfoFragment(bvid);
        videoInfoFragment.setOnVideoAnthologyListener(new OnVideoAnthologyListener() {
            @Override
            public void onAnthology(String cid, String title, boolean isMovie) {
                VideoFragment.this.cid = cid;
                VideoFragment.this.title = title;
                VideoFragment.this.isMovie = isMovie;

                getVideoStreamUrl(null);
            }
        });

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                if (msg.obj == null) {
                    SimpleSnackBar.make(getActivity().getWindow().getDecorView(), context.getString(R.string.snackBarDataErrorWarn), SimpleSnackBar.LENGTH_LONG).show();
                    backPressed();
                    return;
                }

                VideoWithFlv videoWithFlv = (VideoWithFlv) msg.obj;

                if (!isInitialize) {
                    initVideoPlayer(videoWithFlv);
                    isInitialize = true;
                } else {
                    videoPlayerContent.release();
                    videoPlayerContent.setUrl(videoWithFlv.videoStreamInfoList.get(0).url, HttpUtils.getVideoPlayHeaders(true, bvid));

                    // 重新设置弹幕
                    videoPlayerController.resetDanmaku(videoWithFlv.cid);
                    videoPlayerContent.start();
                }

                videoPlayerController.setTitle(title);
            }
        });

        if (findChildFragment(videoInfoFragment.getClass()) == null) {
            loadRootFragment(R.id.video_fragment_container, videoInfoFragment);
        }
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
        videoPlayerController = new VideoPlayerController(context, videoWithFlv, getMainActivity());
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

    /**
     * 获取视频流链接
     */
    public void getVideoStreamUrl (String qualityId) {
        if (videoWithFlvParser == null) {
            videoWithFlvParser = new VideoWithFlvParser(bvid);
        }

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                // 根据isMovie来决定使用的接口链接
                VideoWithFlv videoWithFlv = videoWithFlvParser.parseData(cid, qualityId, isMovie, true);

                Message message = receiveDataHandler.obtainMessage();
                message.obj = videoWithFlv;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage (DanmakuWrap danmakuWrap) {
        videoDanmakuStatus.setSelected(danmakuWrap.danmakuState);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetQualityMessage (VideoQualityWrap qualityWrap) {
        getVideoStreamUrl(String.valueOf(qualityWrap.qualityId));
    }

    @Override
    public boolean onBackPressedSupport() {
        FragmentManager childFragmentManager = getChildFragmentManager();

        if (childFragmentManager.getFragments().size() > 1) {
            popChild();
        } else {
            onDestroy();
            pop();
        }

        return true;
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
        EventBus.getDefault().unregister(this);
    }
}
