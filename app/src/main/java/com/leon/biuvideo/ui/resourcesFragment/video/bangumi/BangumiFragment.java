package com.leon.biuvideo.ui.resourcesFragment.video.bangumi;

import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;

import com.dueeeke.videoplayer.ijk.IjkPlayer;
import com.dueeeke.videoplayer.player.VideoView;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoWithFlv;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.videoControlComonents.VideoPlayerTitleView;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.ui.views.VideoPlayerController;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.VideoWithFlvParser;
import com.leon.biuvideo.wraps.DanmakuWrap;
import com.leon.biuvideo.wraps.VideoQualityWrap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @Author Leon
 * @Time 2021/4/23
 * @Desc 番剧播放页面
 */
public class BangumiFragment extends BaseSupportFragment implements View.OnClickListener {
    private final String seasonId;
    private boolean isInitialize = false;
    private String cid;
    private String title;

    private VideoPlayerController videoPlayerController;
    private ImageView videoDanmakuStatus;

    private VideoWithFlvParser videoWithFlvParser;

    public BangumiFragment(String seasonId) {
        this.seasonId = seasonId;
    }

    private VideoView<IjkPlayer> videoPlayerContent;

    @Override
    protected int setLayout() {
        return R.layout.bangumi_fragment;
    }

    @Override
    protected void initView() {
        // 注册监听
        EventBus.getDefault().register(this);

        videoPlayerContent = findView(R.id.video_player_content);

        findView(R.id.video_danmaku_style).setOnClickListener(this);
        findView(R.id.video_send_danmaku).setOnClickListener(this);

        videoDanmakuStatus = findView(R.id.video_danmaku_status);
        videoDanmakuStatus.setSelected(true);
        videoDanmakuStatus.setOnClickListener(this);

        BangumiInfoFragment bangumiInfoFragment = new BangumiInfoFragment(seasonId);
        bangumiInfoFragment.setOnBangumiInfoListener(new BangumiInfoFragment.OnBangumiInfoListener() {
            @Override
            public void onBangumiAnthologyListener(String aid, String cid, String title) {
                BangumiFragment.this.cid = cid;
                BangumiFragment.this.title = title;

                getVideoStreamUrl(cid, null);
            }
        });

        if (findChildFragment(bangumiInfoFragment.getClass()) == null) {
            loadRootFragment(R.id.video_fragment_container, bangumiInfoFragment);
        }

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
                    videoPlayerContent.setUrl(videoWithFlv.videoStreamInfoList.get(0).url, HttpUtils.getHeaders());

                    // 重新设置弹幕
                    videoPlayerController.resetDanmaku(videoWithFlv.cid);
                    videoPlayerContent.start();
                }

                videoPlayerController.setTitle(title);
            }
        });
    }

    /**
     * 第一次加载视频
     *
     * @param videoWithFlv 视频画质信息
     */
    private void initVideoPlayer(VideoWithFlv videoWithFlv) {
        videoPlayerContent.setUrl(videoWithFlv.videoStreamInfoList.get(0).url, HttpUtils.getHeaders());
        videoPlayerController = new VideoPlayerController(context, videoWithFlv);
        videoPlayerController.setOnBackListener(new VideoPlayerTitleView.OnBackListener() {
            @Override
            public void onBack() {
                backPressed();
            }
        });
        videoPlayerController.addDefaultControlComponent("BiliBili", videoWithFlv.cid);

        videoPlayerContent.setVideoController(videoPlayerController);
        videoPlayerContent.start();
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

    /**
     * 获取视频流链接
     */
    public void getVideoStreamUrl (String cid, String qualityId) {
        if (videoWithFlvParser == null) {
            videoWithFlvParser = new VideoWithFlvParser(seasonId);
        }

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                VideoWithFlv videoWithFlv = videoWithFlvParser.parseData(cid, qualityId, true, true);

                Message message = receiveDataHandler.obtainMessage();
                message.obj = videoWithFlv;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_danmaku_status:
                boolean selected = videoDanmakuStatus.isSelected();
                videoDanmakuStatus.setSelected(!selected);
                EventBus.getDefault().post(DanmakuWrap.getInstance(!selected));
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage (DanmakuWrap danmakuWrap) {
        videoDanmakuStatus.setSelected(danmakuWrap.danmakuState);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetQualityMessage (VideoQualityWrap qualityWrap) {
        getVideoStreamUrl(this.cid, String.valueOf(qualityWrap.qualityId));
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
