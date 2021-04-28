package com.leon.biuvideo.ui.resourcesFragment.video.bangumi;

import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.RecommendAdapter;
import com.leon.biuvideo.adapters.otherAdapters.BangumiRecommendAdapter;
import com.leon.biuvideo.adapters.otherAdapters.BangumiSectionContainerAdapter;
import com.leon.biuvideo.beans.resourcesBeans.BangumiRecommend;
import com.leon.biuvideo.beans.resourcesBeans.VideoRecommend;
import com.leon.biuvideo.beans.resourcesBeans.Comment;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.Bangumi;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.BangumiAnthologyStat;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.BangumiAnthology;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.OnBottomSheetWithItemListener;
import com.leon.biuvideo.ui.resourcesFragment.video.VideoAnthologyBottomSheet;
import com.leon.biuvideo.ui.resourcesFragment.video.VideoCommentDetailFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.VideoCommentFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.contribution.VideoInfoAndCommentsFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.ui.views.TagView;
import com.leon.biuvideo.utils.BindingUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.parseDataUtils.bangumiParsers.BangumiAnthologyStatParser;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.BangumiDetailParser;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.BangumiRecommendParser;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.VideoRecommendParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/23
 * @Desc 番剧介绍页面
 */
public class BangumiInfoFragment extends BaseSupportFragment implements View.OnClickListener {
    private String seasonId;

    private ImageView bangumiInfoOrder;

    private TextView bangumiInfoLike;
    private TextView bangumiInfoCoin;
    private TextView bangumiInfoFavorite;
    private TextView bangumiInfoShare;

    private TagView bangumiInfoComments;

    private LinearLayout bangumiInfoAnthologyContainer;
    private TagView bangumiInfoNowAnthology;

    private LinearLayout bangumiInfoSeriesContainer;
    private TagView bangumiInfoNowSeries;

    private RecyclerView bangumiInfoSectionContainerList;

    private LoadingRecyclerView bangumiInfoRecommends;

    private int seriesIndex = 0;
    private int anthologyIndex = 0;

    private BangumiAnthologyStat bangumiAnthologyStat;
    private List<BangumiRecommend> bangumiRecommendList = new ArrayList<>();
    private Bangumi bangumi;

    private OnBangumiInfoListener onBangumiInfoListener;
    private BangumiRecommendAdapter bangumiRecommendAdapter;

    public BangumiInfoFragment(String seasonId) {
        this.seasonId = seasonId;
    }

    public interface OnBangumiInfoListener {
        /**
         * 切换番剧选集
         *
         * @param aid   视频aid
         * @param cid   视频cid
         * @param title 视频标题
         */
        void onBangumiAnthologyListener (String aid, String cid, String title);
    }

    public void setOnBangumiInfoListener(OnBangumiInfoListener onBangumiInfoListener) {
        this.onBangumiInfoListener = onBangumiInfoListener;
    }

    @Override
    protected int setLayout() {
        return R.layout.bangumi_info_fragment;
    }

    @Override
    protected void initView() {
        findView(R.id.video_info_container).setBackgroundResource(R.color.white);

        bangumiInfoOrder = findView(R.id.bangumi_info_order);
        bangumiInfoOrder.setOnClickListener(this);

        bangumiInfoLike = findView(R.id.bangumi_info_like);
        bangumiInfoLike.setOnClickListener(this);

        bangumiInfoCoin = findView(R.id.bangumi_info_coin);
        bangumiInfoCoin.setOnClickListener(this);

        bangumiInfoFavorite = findView(R.id.bangumi_info_favorite);
        bangumiInfoFavorite.setOnClickListener(this);

        bangumiInfoShare = findView(R.id.bangumi_info_share);
        bangumiInfoShare.setOnClickListener(this);

        findView(R.id.bangumi_info_comments_container).setOnClickListener(this);
        bangumiInfoComments = findView(R.id.bangumi_info_comments);

        bangumiInfoAnthologyContainer = findView(R.id.bangumi_info_anthology_container);
        bangumiInfoAnthologyContainer.setOnClickListener(this);
        bangumiInfoNowAnthology = findView(R.id.bangumi_info_now_anthology);

        bangumiInfoSeriesContainer = findView(R.id.bangumi_info_series_container);
        bangumiInfoSeriesContainer.setOnClickListener(this);
        bangumiInfoNowSeries = findView(R.id.bangumi_info_nowSeries);

        bangumiInfoSectionContainerList = findView(R.id.bangumi_info_section_container_list);

        bangumiInfoRecommends = findView(R.id.bangumi_info_recommends);
        bangumiRecommendAdapter = new BangumiRecommendAdapter(bangumiRecommendList, context);
        bangumiInfoRecommends.setRecyclerViewAdapter(bangumiRecommendAdapter);
        bangumiInfoRecommends.setRecyclerViewLayoutManager(new LinearLayoutManager(context));

        initHandler();
        getBangumiInfo();
    }

    private void initHandler() {
        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                switch (msg.what) {
                    case 0:
                        bangumi = (Bangumi) msg.obj;

                        bangumiInfoRecommends.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

                        BindingUtils bindingUtils = new BindingUtils(view, context);
                        bindingUtils.setText(R.id.bangumi_info_title, bangumi.title);

                        if (bangumi.bangumiSeasonList.get(seriesIndex).badge != null) {
                            bindingUtils.setText(R.id.bangumi_info_badge, bangumi.bangumiSeasonList.get(seriesIndex).badge);
                        } else {
                            bindingUtils.setVisibility(R.id.bangumi_info_badge, View.GONE);
                        }

                        bindingUtils
                                .setText(R.id.bangumi_info_state, bangumi.newEpDesc)
                                .setText(R.id.bangumi_info_score, bangumi.ratingScore + "分")
                                .setText(R.id.bangumi_info_play, ValueUtils.generateCN(bangumi.views) + "播放")
                                .setText(R.id.bangumi_info_orderTotal, ValueUtils.generateCN(bangumi.bangumiSeasonList.get(seriesIndex).seriesFollow) + "系列追番");

                        if (bangumi.bangumiAnthologyList == null || bangumi.bangumiAnthologyList.size() < 2) {
                            bangumiInfoAnthologyContainer.setVisibility(View.GONE);
                        } else {
                            bangumiInfoNowAnthology.setRightValue(bangumi.bangumiAnthologyList.get(anthologyIndex).longTitle);
                        }

                        if (bangumi.bangumiSeasonList == null || bangumi.bangumiSeasonList.size() < 2) {
                            bangumiInfoSeriesContainer.setVisibility(View.GONE);
                        } else {
                            bangumiInfoNowSeries.setRightValue(bangumi.bangumiSeasonList.get(seriesIndex).seasonTitle);
                        }

                        if (bangumi.bangumiSectionList == null || bangumi.bangumiSectionList.size() < 2) {
                            bangumiInfoSectionContainerList.setVisibility(View.GONE);
                        } else {
                            BangumiSectionContainerAdapter bangumiSectionContainerAdapter = new BangumiSectionContainerAdapter(bangumi.bangumiSectionList, context);
                            bangumiSectionContainerAdapter.setHasStableIds(true);
                            bangumiInfoSectionContainerList.setAdapter(bangumiSectionContainerAdapter);
                        }

                        bangumiRecommendAdapter.notifyItemRangeChanged(0, bangumiRecommendList.size() - 1);

                        bangumiInfoRecommends.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);

                        // 播放第一个视频
                        if (onBangumiInfoListener != null) {
                            BangumiAnthology bangumiAnthology = bangumi.bangumiAnthologyList.get(anthologyIndex);
                            onBangumiInfoListener.onBangumiAnthologyListener(bangumiAnthology.aid, bangumiAnthology.cid, bangumiAnthology.longTitle);
                        }

                        // 获取第一个选集的状态数
                        getBangumiStateAndRecommend();
                        break;
                    case 1:
                        bangumiInfoLike.setText(ValueUtils.generateCN(bangumiAnthologyStat.like));
                        bangumiInfoLike.setSelected(bangumiAnthologyStat.isLike);

                        bangumiInfoCoin.setText(ValueUtils.generateCN(bangumiAnthologyStat.coin));
                        bangumiInfoCoin.setSelected(bangumiAnthologyStat.isCoin);

                        bangumiInfoFavorite.setText(ValueUtils.generateCN(bangumi.favorites));
                        bangumiInfoFavorite.setSelected(bangumiAnthologyStat.isFavorite);

                        bangumiInfoShare.setText(ValueUtils.generateCN(bangumi.share));

                        bangumiInfoComments.setRightValue(ValueUtils.generateCN(bangumiAnthologyStat.reply));
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void getBangumiInfo() {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                BangumiDetailParser bangumiDetailParser = new BangumiDetailParser(seasonId);
                Bangumi bangumi = bangumiDetailParser.parseData();

                if (bangumiRecommendList != null) {
                    if (bangumiRecommendList.size() > 0) {
                        bangumiRecommendList.clear();
                    }

                    bangumiRecommendList.addAll(BangumiRecommendParser.parseData(seasonId));
                } else {
                    bangumiRecommendList = BangumiRecommendParser.parseData(seasonId);;
                }

                Message message = receiveDataHandler.obtainMessage(0);
                message.obj = bangumi;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    private void getBangumiStateAndRecommend() {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                bangumiAnthologyStat = BangumiAnthologyStatParser.parseData(bangumi.bangumiAnthologyList.get(anthologyIndex).id);

                Message message = receiveDataHandler.obtainMessage(1);
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bangumi_info_order:
            case R.id.bangumi_info_detail:
            case R.id.bangumi_info_like:
            case R.id.bangumi_info_coin:
            case R.id.bangumi_info_favorite:
            case R.id.bangumi_info_share:
                SimpleSnackBar.make(v, getString(R.string.snackBarBuildingWarn), SimpleSnackBar.LENGTH_LONG).show();
                break;
            case R.id.bangumi_info_comments_container:
                VideoCommentFragment videoCommentFragment = new VideoCommentFragment(bangumi.bangumiAnthologyList.get(anthologyIndex).aid);
                videoCommentFragment.setToCommentDetailFragment(new VideoInfoAndCommentsFragment.ToCommentDetailFragment() {
                    @Override
                    public void toCommentDetail(Comment comment) {
                        start(new VideoCommentDetailFragment(comment));
                    }
                });

                start(videoCommentFragment);
                break;
            case R.id.bangumi_info_anthology_container:
                VideoAnthologyBottomSheet videoAnthologyBottomSheet = new VideoAnthologyBottomSheet(context, anthologyIndex);
                videoAnthologyBottomSheet.setBangumiAnthologyList(bangumi.bangumiAnthologyList);
                videoAnthologyBottomSheet.setOnBottomSheetWithItemListener(new OnBottomSheetWithItemListener() {
                    @Override
                    public void onItem(int position) {
                        if (onBangumiInfoListener != null) {
                            anthologyIndex = position;

                            // 刷新状态数
                            getBangumiStateAndRecommend();

                            BangumiAnthology bangumiAnthology = bangumi.bangumiAnthologyList.get(anthologyIndex);
                            bangumiInfoNowAnthology.setRightValue(bangumiAnthology.longTitle);
                            onBangumiInfoListener.onBangumiAnthologyListener(bangumiAnthology.aid, bangumiAnthology.cid, bangumiAnthology.longTitle);

                            videoAnthologyBottomSheet.dismiss();
                        }
                    }
                });
                videoAnthologyBottomSheet.show();
                break;
            case R.id.bangumi_info_series_container:
                VideoSeriesBottomSheet videoSeriesBottomSheet = new VideoSeriesBottomSheet(context, seriesIndex, bangumi.bangumiSeasonList);
                videoSeriesBottomSheet.setOnBottomSheetWithItemListener(new OnBottomSheetWithItemListener() {
                    @Override
                    public void onItem(int position) {
                        seriesIndex = position;
                        anthologyIndex = 0;

                        seasonId = bangumi.bangumiSeasonList.get(seriesIndex).seasonId;
                        videoSeriesBottomSheet.dismiss();
                        getBangumiInfo();
                    }
                });
                videoSeriesBottomSheet.show();
                break;
            default:
                break;
        }
    }
}
