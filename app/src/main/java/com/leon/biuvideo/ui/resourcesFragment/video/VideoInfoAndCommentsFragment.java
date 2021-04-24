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
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.Bangumi;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoWithFlv;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.bangumi.BangumiCommentFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.bangumi.BangumiInfoFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.contribution.VideoInfoFragment;
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
    private final boolean isBangumi;
    private final String[] tabLayoutTitles = {"简介", "评论"};
    private final List<Fragment> viewPagerFragments = new ArrayList<>(2);

    private ImageView videoInfoAndCommentsDanmakuStatus;

    private VideoWithFlvParser videoWithFlvParser;
    private String cid;
    private String title;

    private VideoStatListener videoStatListener;
    private MainActivity.OnTouchListener onTouchListener;
    private VideoCommentFragment videoCommentFragment;
    private BangumiCommentFragment bangumiCommentFragment;

    /**
     * @param resourceId    视频（bvid）/番剧（seasonId）/电视剧（seasonId）
     * @param isBangumi 是否为番剧资源
     */
    public VideoInfoAndCommentsFragment(String resourceId, boolean isBangumi) {
        this.resourceId = resourceId;
        this.isBangumi = isBangumi;
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

        if (isBangumi) {
            addBangumiInfoFragment();
            bangumiCommentFragment = new BangumiCommentFragment();
//            bangumiCommentFragment.setToCommentDetailFragment(new VideoInfoAndCommentsFragment.ToCommentDetailFragment() {
//
//                @Override
//                public void toCommentDetail(Comment comment) {
//                    start(new VideoCommentDetailFragment(comment));
//                }
//            });
            viewPagerFragments.add(bangumiCommentFragment);
        } else {
            addVideoInfoFragment();
            addVideoCommentFragment(ValueUtils.bv2av(resourceId));
        }

        videoInfoAndCommentsViewPager.setAdapter(new ViewPager2Adapter(this, viewPagerFragments));
        onTouchListener = ViewUtils.initTabLayoutAndViewPager2(getActivity(), videoInfoAndCommentsTabLayout, videoInfoAndCommentsViewPager, tabLayoutTitles, 0);
    }

    /**
     * 番剧介绍页面
     */
    private void addBangumiInfoFragment() {
        BangumiInfoFragment bangumiInfoFragment = new BangumiInfoFragment(resourceId);
        bangumiInfoFragment.setOnBangumiInfoListener(new BangumiInfoFragment.OnBangumiInfoListener() {
            @Override
            public void onBangumiAnthologyListener(String aid, String cid, String title) {
                VideoInfoAndCommentsFragment.this.cid = cid;
                VideoInfoAndCommentsFragment.this.title = title;

                getVideoStreamUrl(VideoWithFlvParser.DEFAULT_QUALITY);

                // 番剧评论无论是首次加载还是二次加载都需要调用resetComments(aid)
                bangumiCommentFragment.setCommentDatas(aid);
            }
        });
        viewPagerFragments.add(bangumiInfoFragment);
    }

    /**
     * 投稿视频介绍页面
     */
    private void addVideoInfoFragment() {
        VideoInfoFragment videoInfoFragment = new VideoInfoFragment(resourceId);
        videoInfoFragment.setOnVideoAnthologyListener(new OnVideoAnthologyListener() {
            @Override
            public void onAnthology(String cid, String title) {
                VideoInfoAndCommentsFragment.this.cid = cid;
                VideoInfoAndCommentsFragment.this.title = title;

                getVideoStreamUrl(VideoWithFlvParser.DEFAULT_QUALITY);
            }
        });
        viewPagerFragments.add(videoInfoFragment);
    }

    /**
     * 视频评论页面
     *
     * 注意：番剧/电视剧等视频的评论是和其选集一一对应的
     * <br/>如果为投稿视频，则该处只需将BVID转换为aid，如果为番剧/电视剧等视频，则需要获取其选集数据后再调用该方法
     *
     * @param aid   视频aid
     */
    private void addVideoCommentFragment (String aid) {
        videoCommentFragment = new VideoCommentFragment(aid);
        videoCommentFragment.setToCommentDetailFragment(new ToCommentDetailFragment() {
            @Override
            public void toCommentDetail(Comment comment) {
                start(new VideoCommentDetailFragment(comment));
            }
        });
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
                VideoWithFlv videoWithFlv = videoWithFlvParser.parseData(cid, qualityId, isBangumi);

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
