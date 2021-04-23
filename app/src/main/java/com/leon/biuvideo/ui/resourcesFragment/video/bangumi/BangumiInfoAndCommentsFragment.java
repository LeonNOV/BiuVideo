package com.leon.biuvideo.ui.resourcesFragment.video.bangumi;

import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;

/**
 * @Author Leon
 * @Time 2021/4/23
 * @Desc
 */
public class BangumiInfoAndCommentsFragment extends BaseSupportFragment implements View.OnClickListener {
    private final String seasonId;

    private ImageView videoInfoAndCommentsDanmakuStatus;
    private TabLayout videoInfoAndCommentsTabLayout;
    private ViewPager2 videoInfoAndCommentsViewPager;

    public BangumiInfoAndCommentsFragment(String seasonId) {
        this.seasonId = seasonId;
    }

    @Override
    protected int setLayout() {
        return R.layout.video_info_and_comments_fragment;
    }

    @Override
    protected void initView() {
        findView(R.id.video_info_and_comments_tab_container).setBackgroundResource(R.color.white);
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

            }
        });

        getBangumiDetail();
    }

    private void getBangumiDetail() {

    }

    @Override
    public void onClick(View v) {

    }
}
