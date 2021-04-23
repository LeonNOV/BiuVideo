package com.leon.biuvideo.ui.resourcesFragment.video.bangumi;

import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.BangumiSectionContainerAdapter;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.Bangumi;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.BangumiAnthologyStat;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.BangumiEp;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoWithFlv;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.TagView;
import com.leon.biuvideo.utils.BindingUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.parseDataUtils.bangumiParsers.BangumiAnthologyStatParser;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.BangumiDetailParser;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.VideoWithFlvParser;

/**
 * @Author Leon
 * @Time 2021/4/23
 * @Desc
 */
public class BangumiInfoFragment extends BaseSupportFragment {
    private final String seasonId;

    private ImageView bangumiInfoOrder;

    private TextView bangumiInfoLike;
    private TextView bangumiInfoCoin;
    private TextView bangumiInfoFavorite;
    private TextView bangumiInfoShare;

    private LinearLayout bangumiInfoAnthologyContainer;
    private TagView bangumiInfoNowAnthology;

    private LinearLayout bangumiInfoSeriesContainer;
    private TagView bangumiInfoNowSeries;

    private RecyclerView bangumiInfoSectionContainerList;
    private LoadingRecyclerView bangumi_info_recommends;

    private int seriesIndex = 0;
    private int anthologyIndex = 0;

    private VideoWithFlv videoWithFlv;
    private BangumiAnthologyStat bangumiAnthologyStat;
    private VideoWithFlvParser videoWithFlvParser;
    private Bangumi bangumi;

    public BangumiInfoFragment(String seasonId) {
        this.seasonId = seasonId;
    }

    @Override
    protected int setLayout() {
        return R.layout.bangumi_info_fragment;
    }

    @Override
    protected void initView() {
        bangumiInfoOrder = findView(R.id.bangumi_info_order);

        bangumiInfoLike = findView(R.id.bangumi_info_like);
        bangumiInfoCoin = findView(R.id.bangumi_info_coin);
        bangumiInfoFavorite = findView(R.id.bangumi_info_favorite);
        bangumiInfoShare = findView(R.id.bangumi_info_share);

        bangumiInfoAnthologyContainer = findView(R.id.bangumi_info_anthology_container);
        bangumiInfoNowAnthology = findView(R.id.bangumi_info_now_anthology);

        bangumiInfoSeriesContainer = findView(R.id.bangumi_info_series_container);
        bangumiInfoNowSeries = findView(R.id.bangumi_info_nowSeries);

        bangumiInfoSectionContainerList = findView(R.id.bangumi_info_section_container_list);
        bangumi_info_recommends = findView(R.id.bangumi_info_recommends);

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

                        BindingUtils bindingUtils = new BindingUtils(view, context);
                        bindingUtils.setText(R.id.bangumi_info_title, bangumi.title);

                        if (bangumi.bangumiSeasonList.get(seriesIndex).badge != null) {
                            bindingUtils.setText(R.id.bangumi_info_badge, bangumi.bangumiSeasonList.get(seriesIndex).badge);
                        } else {
                            bindingUtils.setVisibility(R.id.bangumi_info_badge, View.GONE);
                        }

                        bindingUtils.setText(R.id.bangumi_info_state, bangumi.newEpDesc)
                                .setText(R.id.bangumi_info_score, bangumi.ratingScore + "分")
                                .setText(R.id.bangumi_info_play, ValueUtils.generateCN(bangumi.views) + "播放")
                                .setText(R.id.bangumi_info_orderTotal, ValueUtils.generateCN(bangumi.bangumiSeasonList.get(seriesIndex).seriesFollow) + "系列追番");

                        if (bangumi.bangumiEpList.size() < 2) {
                            bangumiInfoAnthologyContainer.setVisibility(View.GONE);
                        } else {
                            bangumiInfoNowAnthology.setLeftValue(bangumi.bangumiEpList.get(anthologyIndex).longTitle);
                        }

                        if (bangumi.bangumiSeasonList.size() < 2) {
                            bangumiInfoSeriesContainer.setVisibility(View.GONE);
                        } else {
                            bangumiInfoNowSeries.setLeftValue(bangumi.bangumiSeasonList.get(seriesIndex).seasonTitle);
                        }

                        if (bangumi.bangumiSectionList.size() != 0) {
                            BangumiSectionContainerAdapter bangumiSectionContainerAdapter = new BangumiSectionContainerAdapter(bangumi.bangumiSectionList, context);
                            bangumiSectionContainerAdapter.setHasStableIds(true);
                            bangumiInfoSectionContainerList.setAdapter(bangumiSectionContainerAdapter);
                        } else {
                            bangumiInfoSectionContainerList.setVisibility(View.GONE);
                        }

                        getFirstVideoAndState();
                        break;
                    case 1:
                        bangumiInfoLike.setText(ValueUtils.generateCN(bangumiAnthologyStat.like));
                        bangumiInfoLike.setSelected(bangumiAnthologyStat.isLike);

                        bangumiInfoCoin.setText(ValueUtils.generateCN(bangumiAnthologyStat.coin));
                        bangumiInfoCoin.setSelected(bangumiAnthologyStat.isCoin);

                        bangumiInfoFavorite.setText(ValueUtils.generateCN(bangumi.favorites));
                        bangumiInfoFavorite.setSelected(bangumiAnthologyStat.isFavorite);

                        bangumiInfoShare.setText(ValueUtils.generateCN(bangumi.share));


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

                Message message = receiveDataHandler.obtainMessage(0);
                message.obj = bangumi;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    private void getFirstVideoAndState() {
        if (videoWithFlvParser != null) {
            videoWithFlvParser = new VideoWithFlvParser();
        }

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                BangumiEp bangumiEp = bangumi.bangumiEpList.get(anthologyIndex);

                videoWithFlv = videoWithFlvParser.parseData(bangumiEp.cid, VideoWithFlvParser.DEFAULT_QUALITY, true);
                bangumiAnthologyStat = BangumiAnthologyStatParser.parseData(bangumiEp.id);

                Message message = receiveDataHandler.obtainMessage(1);
                receiveDataHandler.sendMessage(message);
            }
        });
    }
}
