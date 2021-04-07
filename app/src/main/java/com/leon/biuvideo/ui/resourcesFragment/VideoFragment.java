package com.leon.biuvideo.ui.resourcesFragment;

import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.dueeeke.videoplayer.ijk.IjkPlayer;
import com.dueeeke.videoplayer.player.VideoView;
import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.ViewPager2Adapter;
import com.leon.biuvideo.beans.mediaBeans.videoBeans.VideoDetailInfo;
import com.leon.biuvideo.beans.mediaBeans.videoBeans.VideoWithFlv;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.ui.views.VideoPlayerController;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.ViewUtils;
import com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils.VideoDetailInfoParser;
import com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils.VideoWithFlvParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/2
 * @Desc 视频播放页面
 */
public class VideoFragment extends BaseSupportFragment implements View.OnClickListener {
    private VideoView<IjkPlayer> videoPlayerViewContent;

    private ImageView videoDanmakuStatus;
    private TabLayout videoTabLayout;
    private ViewPager2 videoViewPager;

    private final String bvid;
    private VideoWithFlvParser videoWithFlvParser;
    private VideoPlayerController videoPlayerController;
    private VideoWithFlv videoWithFlv;
    private MainActivity.OnTouchListener onTouchListener;

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

        findView(R.id.video_tab_container).setBackgroundResource(R.color.white);

        videoTabLayout = findView(R.id.video_tabLayout);

        findView(R.id.video_back).setOnClickListener(this);
        findView(R.id.video_send_danmaku).setOnClickListener(this);
        videoDanmakuStatus = findView(R.id.video_danmaku_status);
        videoDanmakuStatus.setOnClickListener(this);

        videoViewPager = findView(R.id.video_viewPager);

        String[] titles = {"简介", "评论"};
        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                if (msg.obj == null) {
                    SimpleSnackBar.make(getActivity().getWindow().getDecorView(), "获取数据失败", SimpleSnackBar.LENGTH_LONG).show();
                    backPressed();
                    return;
                }

                switch (msg.what) {
                    case 0:
                        List<Fragment> viewPagerFragments = new ArrayList<>(2);

                        VideoDetailInfo videoDetailInfo = (VideoDetailInfo) msg.obj;
                        viewPagerFragments.add(new VideoDetailFragment(videoDetailInfo));
                        viewPagerFragments.add(new VideoCommentFragment());
                        videoViewPager.setAdapter(new ViewPager2Adapter(VideoFragment.this, viewPagerFragments));

                        // 初始化ViewPager2和TabLayout
                        onTouchListener = ViewUtils.initTabLayoutAndViewPager2(getActivity(), videoTabLayout, videoViewPager, titles, 0);

                        // 获取第一个视频
                        VideoDetailInfo.AnthologyInfo anthologyInfo = videoDetailInfo.anthologyInfoList.get(0);
                        getVideoStreamUrl(anthologyInfo.cid);
                        break;
                    case 1:
                        // 播放第一个视频
                        videoWithFlv = (VideoWithFlv) msg.obj;
                        initVideoPlayer(videoWithFlv.videoStreamInfoList.get(0).url);
                        break;
                    default:
                        break;
                }
            }
        });

        initVideoData();
    }

    /**
     * 获取视频基本信息
     */
    private void initVideoData() {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                VideoDetailInfo videoDetailInfo = VideoDetailInfoParser.parseData(bvid);

                Message message = receiveDataHandler.obtainMessage(0);
                message.obj = videoDetailInfo;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    /**
     * 第一次加载视频
     *
     * @param videoUrl  视频链接
     */
    private void initVideoPlayer (String videoUrl) {
        videoPlayerViewContent.setUrl(videoUrl, HttpUtils.getHeaders());
        videoPlayerController = new VideoPlayerController(context);
        videoPlayerController.addDefaultControlComponent("BiliBili");
        videoPlayerViewContent.setVideoController(videoPlayerController);

        videoPlayerViewContent.start();
    }

    public void getVideoStreamUrl (String cid) {
        if (videoWithFlvParser == null) {
            videoWithFlvParser = new VideoWithFlvParser(bvid);
        }

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                VideoWithFlv videoWithFlv = videoWithFlvParser.parseData(cid);

                Message message = receiveDataHandler.obtainMessage(1);
                message.obj = videoWithFlv;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    @Override
    public void onClick(View v) {

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

        // 取消注册Touch事件
        ((MainActivity) getActivity()).unregisterTouchEvenListener(onTouchListener);
    }
}
