package com.leon.biuvideo.ui.resourcesFragment;

import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.ViewPager2Adapter;
import com.leon.biuvideo.beans.mediaBeans.Comment;
import com.leon.biuvideo.beans.mediaBeans.videoBeans.VideoDetailInfo;
import com.leon.biuvideo.beans.mediaBeans.videoBeans.VideoWithFlv;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.ViewUtils;
import com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils.VideoDetailInfoParser;
import com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils.VideoWithFlvParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/8
 * @Desc 视频信息及评论内容
 */
public class VideoInfoAndCommentsFragment extends BaseSupportFragment implements View.OnClickListener {
    private final String bvid;

    private ImageView videoInfoAndCommentsDanmakuStatus;
    private TabLayout videoInfoAndCommentsTabLayout;
    private ViewPager2 videoInfoAndCommentsViewPager;

    private VideoWithFlvParser videoWithFlvParser;
    private VideoWithFlv videoWithFlv;

    private VideoFragmentContainerListener videoFragmentContainerListener;
    private MainActivity.OnTouchListener onTouchListener;

    public VideoInfoAndCommentsFragment(String bvid) {
        this.bvid = bvid;
    }

    public interface VideoFragmentContainerListener {
        /**
         * 播放视频
         *
         * @param videoUrl  视频链接
         */
        void playVideo (String videoUrl);

        /**
         * 控制弹幕 显示/隐藏
         *
         * @param status    显示/隐藏
         */
        void danmakuStatus (boolean status);

        /**
         * 错误事件
         */
        void onError();
    }

    public void setVideoFragmentContainerListener(VideoFragmentContainerListener videoFragmentContainerListener) {
        this.videoFragmentContainerListener = videoFragmentContainerListener;
    }

    @Override
    protected int setLayout() {
        return R.layout.video_info_and_comments_fragment;
    }

    @Override
    protected void initView() {
        findView(R.id.video_info_and_comments_tab_container).setBackgroundResource(R.color.white);

        videoInfoAndCommentsTabLayout = findView(R.id.video_info_and_comments_tabLayout);

        findView(R.id.video_info_and_comments_send_danmaku).setOnClickListener(this);
        videoInfoAndCommentsDanmakuStatus = findView(R.id.video_info_and_comments_danmaku_status);
        videoInfoAndCommentsDanmakuStatus.setOnClickListener(this);

        videoInfoAndCommentsViewPager = findView(R.id.video_info_and_comments_viewPager);

        String[] titles = {"简介", "评论"};
        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                if (msg.obj == null) {
                    if (videoFragmentContainerListener != null) {
                        videoFragmentContainerListener.onError();
                    }
                }

                switch (msg.what) {
                    case 0:
                        List<Fragment> viewPagerFragments = new ArrayList<>(2);

                        VideoDetailInfo videoDetailInfo = (VideoDetailInfo) msg.obj;
                        viewPagerFragments.add(new VideoDetailFragment(videoDetailInfo));
                        VideoCommentFragment videoCommentFragment = new VideoCommentFragment(videoDetailInfo.aid);
                        videoCommentFragment.setOnCommentListener(new OnCommentListener() {
                            @Override
                            public void onClick(Comment comment) {
                                start(new VideoCommentDetailFragment(comment));
                            }

                            @Override
                            public void navUserFragment(String mid) {

                            }
                        });
                        viewPagerFragments.add(videoCommentFragment);

                        videoInfoAndCommentsViewPager.setAdapter(new ViewPager2Adapter(VideoInfoAndCommentsFragment.this, viewPagerFragments));

                        // 初始化ViewPager2和TabLayout
                        onTouchListener = ViewUtils.initTabLayoutAndViewPager2(getActivity(), videoInfoAndCommentsTabLayout, videoInfoAndCommentsViewPager, titles, 0);

                        // 获取第一个视频
                        VideoDetailInfo.AnthologyInfo anthologyInfo = videoDetailInfo.anthologyInfoList.get(0);
                        getVideoStreamUrl(anthologyInfo.cid);
                        break;
                    case 1:
                        // 播放第一个视频
                        videoWithFlv = (VideoWithFlv) msg.obj;
                        if (videoFragmentContainerListener != null) {
                            videoFragmentContainerListener.playVideo(videoWithFlv.videoStreamInfoList.get(0).url);
                        }
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
     * 获取视频流链接
     *
     * @param cid   视频cid
     */
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
        switch (v.getId()) {
            case R.id.video_info_and_comments_danmaku_status:
                if (videoFragmentContainerListener != null) {
                    videoFragmentContainerListener.danmakuStatus(false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 取消注册Touch事件
        ((MainActivity) getActivity()).unregisterTouchEvenListener(onTouchListener);
    }
}
