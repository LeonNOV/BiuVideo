package com.leon.biuvideo.ui.resourcesFragment.video.contribution;

import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.ViewPager2Adapter;
import com.leon.biuvideo.beans.resourcesBeans.Comment;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoWithFlv;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.OnVideoAnthologyListener;
import com.leon.biuvideo.ui.resourcesFragment.video.VideoCommentDetailFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.VideoCommentFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.VideoStatListener;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.ViewUtils;
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
    private final String resourceId;
    private final String[] tabLayoutTitles = {"简介", "评论"};
    private final List<Fragment> viewPagerFragments = new ArrayList<>(2);

    private ImageView videoInfoAndCommentsDanmakuStatus;

    private VideoWithFlvParser videoWithFlvParser;
    private String cid;
    private String title;

    private VideoStatListener videoStatListener;
    private MainActivity.OnTouchListener onTouchListener;
    private VideoCommentFragment videoCommentFragment;

    /**
     * @param resourceId    视频bvid
     */
    public VideoInfoAndCommentsFragment(String resourceId) {
        this.resourceId = resourceId;
    }

    public interface ToCommentDetailFragment {
        /**
         * 查看评论详情
         *
         * @param comment   主评论
         */
        void toCommentDetail(Comment comment);
    }

    public void setVideoStatListener(VideoStatListener videoStatListener) {
        this.videoStatListener = videoStatListener;
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

        TabLayout videoInfoAndCommentsTabLayout = findView(R.id.video_info_and_comments_tabLayout);

        findView(R.id.video_info_and_comments_send_danmaku).setOnClickListener(this);
        videoInfoAndCommentsDanmakuStatus = findView(R.id.video_info_and_comments_danmaku_status);
        videoInfoAndCommentsDanmakuStatus.setSelected(true);
        videoInfoAndCommentsDanmakuStatus.setOnClickListener(this);

        ViewPager2 videoInfoAndCommentsViewPager = findView(R.id.video_info_and_comments_viewPager);

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                if (msg.obj == null) {
                    if (videoStatListener != null) {
                        videoStatListener.onError();
                    }
                }

                if (videoStatListener != null) {
                    VideoWithFlv videoWithFlv = (VideoWithFlv) msg.obj;
                    videoStatListener.playVideo(title, videoWithFlv, 0);
                }
            }
        });

        addSubFragments();

        videoInfoAndCommentsViewPager.setAdapter(new ViewPager2Adapter(this, viewPagerFragments));
        onTouchListener = ViewUtils.initTabLayoutAndViewPager2(getActivity(), videoInfoAndCommentsTabLayout, videoInfoAndCommentsViewPager, tabLayoutTitles, 0);
    }

    /**
     * 投稿视频介绍页面
     */
    private void addSubFragments() {
        VideoInfoFragment videoInfoFragment = new VideoInfoFragment(resourceId);
        videoInfoFragment.setOnVideoAnthologyListener(new OnVideoAnthologyListener() {
            @Override
            public void onAnthology(String cid, String title) {
                VideoInfoAndCommentsFragment.this.cid = cid;
                VideoInfoAndCommentsFragment.this.title = title;

                getVideoStreamUrl(VideoWithFlvParser.DEFAULT_QUALITY);
            }
        });

        videoCommentFragment = new VideoCommentFragment(ValueUtils.bv2av(resourceId));
        videoCommentFragment.setToCommentDetailFragment(new ToCommentDetailFragment() {
            @Override
            public void toCommentDetail(Comment comment) {
                start(new VideoCommentDetailFragment(comment));
            }
        });

        viewPagerFragments.add(videoInfoFragment);
        viewPagerFragments.add(videoCommentFragment);
    }

    /**
     * 获取视频流链接
     */
    public void getVideoStreamUrl (String qualityId) {
        if (videoWithFlvParser == null) {
            videoWithFlvParser = new VideoWithFlvParser(resourceId);
        }

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                VideoWithFlv videoWithFlv = videoWithFlvParser.parseData(cid, qualityId, false);

                Message message = receiveDataHandler.obtainMessage();
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
        getVideoStreamUrl(String.valueOf(qualityWrap.qualityId));
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
