package com.leon.biuvideo.ui.resourcesFragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.mediaBeans.videoBeans.VideoDetailInfo;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.utils.ValueUtils;

/**
 * @Author Leon
 * @Time 2021/4/6
 * @Desc 视频介绍页面
 */
public class VideoDetailFragment extends BaseSupportFragment {
    private final VideoDetailInfo videoDetailInfo;
    private TextView videoDetailFollow;
    private ImageView videoDetailFace;

    private TextView videoDetailShare;
    private TextView videoDetailFavorite;
    private TextView videoDetailCoin;
    private TextView videoDetailLike;

    private RecyclerView videoDetailTags;
    private LoadingRecyclerView videoDetailVideoEps;
    private LoadingRecyclerView videoDetailRecommends;

    public VideoDetailFragment(VideoDetailInfo videoDetailInfo) {
        this.videoDetailInfo = videoDetailInfo;
    }

    @Override
    protected int setLayout() {
        return R.layout.video_detail_fragment;
    }

    @Override
    protected void initView() {
        videoDetailFace = findView(R.id.video_detail_face);
        videoDetailFollow = findView(R.id.video_detail_follow);

        new BindingUtils(view, context)
                .setText(R.id.video_detail_userName, videoDetailInfo.userInfo.userName)
                .setText(R.id.video_detail_play, ValueUtils.generateCN(videoDetailInfo.videoInfo.view))
                .setText(R.id.video_detail_danmaku, ValueUtils.generateCN(videoDetailInfo.videoInfo.danmaku))
                .setText(R.id.video_detail_pubTime, ValueUtils.generateTime(videoDetailInfo.pubTime, "yyyy-MM-dd", true))
                .setText(R.id.video_detail_bvid, videoDetailInfo.bvid);

        videoDetailLike = findView(R.id.video_detail_like);
        videoDetailCoin = findView(R.id.video_detail_coin);
        videoDetailFavorite = findView(R.id.video_detail_favorite);
        videoDetailShare = findView(R.id.video_detail_share);

        videoDetailTags = findView(R.id.video_detail_tags);

        videoDetailVideoEps = findView(R.id.video_detail_videoEps);
        if (videoDetailInfo.anthologyInfoList.size() > 1) {
            videoDetailVideoEps.setVisibility(View.GONE);
        }

        videoDetailRecommends = findView(R.id.video_detail_recommends);
    }
}
