package com.leon.biuvideo.ui.resourcesFragment;

import android.graphics.drawable.Drawable;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.VideoTagsAdapter;
import com.leon.biuvideo.adapters.homeAdapters.RecommendAdapter;
import com.leon.biuvideo.beans.homeBeans.Recommend;
import com.leon.biuvideo.beans.mediaBeans.videoBeans.VideoDetailInfo;
import com.leon.biuvideo.beans.mediaBeans.videoBeans.VideoTag;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.utils.BindingUtils;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.VideoRecommendParser;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.VideoTagsParser;
import com.leon.biuvideo.values.FeaturesName;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2021/4/6
 * @Desc 视频介绍页面
 */
public class VideoInfoFragment extends BaseSupportFragment implements View.OnClickListener {
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
    private List<VideoTag> videoTagList;

    public VideoInfoFragment(VideoDetailInfo videoDetailInfo) {
        this.videoDetailInfo = videoDetailInfo;
    }

    @Override
    protected int setLayout() {
        return R.layout.video_detail_fragment;
    }

    @Override
    protected void initView() {
        findView(R.id.video_detail_container).setBackgroundResource(R.color.white);

        videoDetailFace = findView(R.id.video_detail_face);
        videoDetailFace.setOnClickListener(this);

        Glide
                .with(context)
                .load(videoDetailInfo.userInfo.userFace += PreferenceUtils.getFeaturesStatus(FeaturesName.IMG_ORIGINAL_MODEL) ?
                        ImagePixelSize.FACE.value : "")
                .into(videoDetailFace);

        videoDetailFollow = findView(R.id.video_detail_follow);
        videoDetailFollow.setOnClickListener(this);

        ((ExpandableTextView) findView(R.id.video_detail_desc)).setText(videoDetailInfo.desc);

        new BindingUtils(view, context)
                .setText(R.id.video_detail_userName, videoDetailInfo.userInfo.userName)
                .setText(R.id.video_detail_play, ValueUtils.generateCN(videoDetailInfo.videoInfo.view))
                .setText(R.id.video_detail_danmaku, ValueUtils.generateCN(videoDetailInfo.videoInfo.danmaku))
                .setText(R.id.video_detail_pubTime, ValueUtils.generateTime(videoDetailInfo.pubTime, "yyyy-MM-dd", true))
                .setText(R.id.video_detail_title, videoDetailInfo.title)
                .setText(R.id.video_detail_bvid, videoDetailInfo.bvid);

        videoDetailLike = findView(R.id.video_detail_like);
        videoDetailLike.setOnClickListener(this);
        videoDetailLike.setText(ValueUtils.generateCN(videoDetailInfo.videoInfo.like));

        videoDetailCoin = findView(R.id.video_detail_coin);
        videoDetailCoin.setOnClickListener(this);
        videoDetailCoin.setText(ValueUtils.generateCN(videoDetailInfo.videoInfo.coin));

        videoDetailFavorite = findView(R.id.video_detail_favorite);
        videoDetailFavorite.setOnClickListener(this);
        videoDetailFavorite.setText(ValueUtils.generateCN(videoDetailInfo.videoInfo.favorite));

        videoDetailShare = findView(R.id.video_detail_share);
        videoDetailShare.setOnClickListener(this);
        videoDetailShare.setText(ValueUtils.generateCN(videoDetailInfo.videoInfo.share));

        videoDetailTags = findView(R.id.video_detail_tags);

        videoDetailVideoEps = findView(R.id.video_detail_videoEps);
        if (videoDetailInfo.anthologyInfoList.size() <= 1) {
            videoDetailVideoEps.setVisibility(View.GONE);
        }

        videoDetailRecommends = findView(R.id.video_detail_recommends);
        videoDetailRecommends.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                switch (msg.what) {
                    case 0:
                        JSONObject jsonObject = (JSONObject) msg.obj;

                        // 获取当前账户是否已关注当前UP主
                        boolean attention = jsonObject.getBooleanValue("attention");
                        videoDetailFollow.setSelected(attention);
                        Drawable wrap = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.ripple_round_corners6dp_bg));
                        if (attention) {
                            DrawableCompat.setTint(wrap, context.getColor(R.color.infoColor));
                            videoDetailFollow.setBackground(wrap);
                            videoDetailFollow.setText("已关注");
                        } else {
                            DrawableCompat.setTint(wrap, context.getColor(R.color.BiliBili_pink));
                            videoDetailFollow.setBackground(wrap);
                            videoDetailFollow.setText("关注");
                        }

                        // 获取投币状态
                        boolean coin = jsonObject.getIntValue("coin") > 0;
                        videoDetailCoin.setSelected(coin);

                        // 获取收藏状态
                        boolean favorite = jsonObject.getBooleanValue("favorite");
                        videoDetailFavorite.setSelected(favorite);

                        // 获取点赞状态
                        boolean like = jsonObject.getBooleanValue("like");
                        videoDetailLike.setSelected(like);

                        if (videoTagList != null && videoTagList.size() > 0) {
                            VideoTagsAdapter videoTagsAdapter = new VideoTagsAdapter(videoTagList, context);
                            videoTagsAdapter.setHasStableIds(true);
                            videoDetailTags.setAdapter(videoTagsAdapter);
                        } else {
                            videoDetailTags.setVisibility(View.GONE);
                        }
                        getComments();
                        break;
                    case 1:
                        List<Recommend> recommendList = (List<Recommend>) msg.obj;

                        if (recommendList != null && recommendList.size() > 0) {
                            RecommendAdapter recommendAdapter = new RecommendAdapter(recommendList, RecommendAdapter.SINGLE_COLUMN, context);
                            recommendAdapter.setHasStableIds(true);
                            videoDetailRecommends.setRecyclerViewAdapter(recommendAdapter);
                            videoDetailRecommends.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
                        } else {
                            videoDetailRecommends.setVisibility(View.GONE);
                        }

                        videoDetailRecommends.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                        break;
                    default:
                        break;
                }
            }
        });

        initData();
    }

    private void initData () {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                // 获取当前视频与已登录用户的关系
                Map<String, String> params = new HashMap<>();
                params.put("bvid", videoDetailInfo.bvid);
                JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.VIDEO_STATUS, Headers.of(HttpUtils.getAPIRequestHeader()), params);
                JSONObject data = response.getJSONObject("data");

                // 获取当前视频的tags（频道）
                videoTagList = VideoTagsParser.parseData(videoDetailInfo.bvid);

                Message message = receiveDataHandler.obtainMessage(0);
                message.obj = data;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    /**
     * 获取推荐视频
     */
    private void getComments() {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                List<Recommend> recommendList = VideoRecommendParser.parseData(videoDetailInfo.bvid);

                Message message = receiveDataHandler.obtainMessage(1);
                message.obj = recommendList;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_detail_face:

                break;
            case R.id.video_detail_follow:

                break;
            case R.id.video_detail_like:

                break;
            case R.id.video_detail_coin:

                break;
            case R.id.video_detail_favorite:

                break;
            case R.id.video_detail_share:

                break;
            default:
                break;
        }
    }
}
