package com.leon.biuvideo.ui.resourcesFragment.video;

import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.ViewPager2Adapter;
import com.leon.biuvideo.beans.resourcesBeans.Comment;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoDetailInfo;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoWithFlv;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.ViewUtils;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.VideoDetailInfoParser;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.VideoWithFlvParser;
import com.leon.biuvideo.wraps.DanmakuWrap;
import com.leon.biuvideo.wraps.VideoQualityWrap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/8
 * @Desc 视频信息及评论内容
 */
public class VideoInfoAndCommentsFragment extends BaseSupportFragment implements View.OnClickListener {
    private final String bvid;
    private int videoIndex = 0;

    private ImageView videoInfoAndCommentsDanmakuStatus;
    private TabLayout videoInfoAndCommentsTabLayout;
    private ViewPager2 videoInfoAndCommentsViewPager;

    private VideoWithFlvParser videoWithFlvParser;
    private VideoWithFlv videoWithFlv;

    private VideoFragmentContainerListener videoFragmentContainerListener;

    private MainActivity.OnTouchListener onTouchListener;
    private VideoDetailInfo videoDetailInfo;

    public VideoInfoAndCommentsFragment(String bvid) {
        this.bvid = bvid;
    }

    public interface ToCommentDetailFragment {
        /**
         * 查看评论详情
         *
         * @param comment   主评论
         */
        void toCommentDetail(Comment comment);
    }

    public interface VideoFragmentContainerListener {
        /**
         * 播放视频
         *
         * @param title 选集标题
         * @param videoWithFlv  单集视频信息
         * @param videoStreamIndex    分段视频索引，默认只播放第一个（该值为0）
         */
        void playVideo (String title, VideoWithFlv videoWithFlv, int videoStreamIndex);

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
        // 注册监听
        EventBus.getDefault().register(this);

        findView(R.id.video_info_and_comments_tab_container).setBackgroundResource(R.color.white);

        videoInfoAndCommentsTabLayout = findView(R.id.video_info_and_comments_tabLayout);

        findView(R.id.video_info_and_comments_send_danmaku).setOnClickListener(this);
        videoInfoAndCommentsDanmakuStatus = findView(R.id.video_info_and_comments_danmaku_status);
        videoInfoAndCommentsDanmakuStatus.setSelected(true);
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

                        videoDetailInfo = (VideoDetailInfo) msg.obj;

                        VideoInfoFragment videoInfoFragment = new VideoInfoFragment(videoDetailInfo);
                        videoInfoFragment.setOnChangeVideoAnthologyListener(new VideoInfoFragment.OnChangeVideoAnthologyListener() {
                            @Override
                            public void onChangeAnthology(int position, String cid) {
                                videoIndex = position;
                                getVideoStreamUrl(cid, VideoWithFlvParser.DEFAULT_QUALITY);
                            }
                        });
                        viewPagerFragments.add(videoInfoFragment);

                        VideoCommentFragment videoCommentFragment = new VideoCommentFragment(videoDetailInfo.aid);
                        videoCommentFragment.setToCommentDetailFragment(new ToCommentDetailFragment() {
                            @Override
                            public void toCommentDetail(Comment comment) {
                                start(new VideoCommentDetailFragment(comment));
                            }
                        });
                        viewPagerFragments.add(videoCommentFragment);

                        videoInfoAndCommentsViewPager.setAdapter(new ViewPager2Adapter(VideoInfoAndCommentsFragment.this, viewPagerFragments));

                        // 初始化ViewPager2和TabLayout
                        onTouchListener = ViewUtils.initTabLayoutAndViewPager2(getActivity(), videoInfoAndCommentsTabLayout, videoInfoAndCommentsViewPager, titles, 0);

                        // 获取第一个视频，清晰度按照最高获取
                        VideoDetailInfo.AnthologyInfo anthologyInfo = videoDetailInfo.anthologyInfoList.get(videoIndex);
                        getVideoStreamUrl(anthologyInfo.cid, VideoWithFlvParser.DEFAULT_QUALITY);
                        break;
                    case 1:
                        videoWithFlv = (VideoWithFlv) msg.obj;
                        if (videoFragmentContainerListener != null) {
                            videoFragmentContainerListener.playVideo(videoDetailInfo.anthologyInfoList.get(videoIndex).part, videoWithFlv, 0);
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
    public void getVideoStreamUrl (String cid, String qualityId) {
        if (videoWithFlvParser == null) {
            videoWithFlvParser = new VideoWithFlvParser(bvid);
        }

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                VideoWithFlv videoWithFlv = videoWithFlvParser.parseData(cid, qualityId);

                Message message = receiveDataHandler.obtainMessage(1);
                message.obj = videoWithFlv;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage (DanmakuWrap danmakuWrap) {
        videoInfoAndCommentsDanmakuStatus.setSelected(danmakuWrap.danmakuState);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetQualityMessage (VideoQualityWrap qualityWrap) {
        getVideoStreamUrl(videoDetailInfo.anthologyInfoList.get(videoIndex).cid, String.valueOf(qualityWrap.qualityId));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_info_and_comments_danmaku_status:
                boolean selected = videoInfoAndCommentsDanmakuStatus.isSelected();
                videoInfoAndCommentsDanmakuStatus.setSelected(!selected);
                EventBus.getDefault().post(DanmakuWrap.getInstance(!selected));
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
