package com.leon.biuvideo.ui.resourcesFragment.video.bangumi;

import android.content.pm.PackageManager;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.BangumiRecommendAdapter;
import com.leon.biuvideo.adapters.otherAdapters.BangumiSectionContainerAdapter;
import com.leon.biuvideo.beans.resourcesBeans.BangumiRecommend;
import com.leon.biuvideo.beans.resourcesBeans.Comment;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.Bangumi;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.BangumiAnthologyStat;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.BangumiAnthology;
import com.leon.biuvideo.greendao.dao.DaoBaseUtils;
import com.leon.biuvideo.greendao.dao.DownloadHistory;
import com.leon.biuvideo.greendao.dao.DownloadHistoryDao;
import com.leon.biuvideo.greendao.daoutils.DownloadHistoryUtils;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.DownloadBottomSheet;
import com.leon.biuvideo.ui.resourcesFragment.video.OnBottomSheetWithItemListener;
import com.leon.biuvideo.ui.resourcesFragment.video.VideoAnthologyBottomSheet;
import com.leon.biuvideo.ui.resourcesFragment.video.VideoCommentDetailFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.VideoCommentFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.contribution.VideoInfoAndCommentsFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.ui.views.TagView;
import com.leon.biuvideo.ui.views.WarnDialog;
import com.leon.biuvideo.utils.BindingUtils;
import com.leon.biuvideo.utils.PermissionUtil;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.parseDataUtils.bangumiParsers.BangumiAnthologyStatParser;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.BangumiDetailParser;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.BangumiRecommendParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/23
 * @Desc 番剧介绍页面
 */
public class BangumiInfoFragment extends BaseSupportFragment implements View.OnClickListener {
    private String seasonId;

    private TextView bangumiInfoTitle;
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

    private List<BangumiRecommend> bangumiRecommendList = new ArrayList<>();
    private Bangumi bangumi;

    private OnBangumiInfoListener onBangumiInfoListener;
    private BangumiRecommendAdapter bangumiRecommendAdapter;
    private LinearLayout bangumiInfoEasterEggContainer;
    private TextView bangumiInfoDownloadedRecord;

    private Long downloadedRecordCount = 0L;

    private int easterEggSteps;
    private String easterEggWarn;

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
        findView(R.id.bangumi_info_detail).setOnClickListener(this);

        bangumiInfoTitle = findView(R.id.bangumi_info_title);
        bangumiInfoTitle.setOnClickListener(this);

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

        bangumiInfoEasterEggContainer = findView(R.id.bangumi_info_easterEgg_container);
        bangumiInfoEasterEggContainer.setOnClickListener(this);

        bangumiInfoDownloadedRecord = findView(R.id.bangumi_info_downloaded_record);

        if (PreferenceUtils.getEasterEggStat()) {
            bangumiInfoEasterEggContainer.setVisibility(View.VISIBLE);
        }

        bangumiInfoRecommends = findView(R.id.bangumi_info_recommends);
        bangumiRecommendAdapter = new BangumiRecommendAdapter(bangumiRecommendList, context);
        bangumiInfoRecommends.setRecyclerViewAdapter(bangumiRecommendAdapter);
        bangumiInfoRecommends.setRecyclerViewLayoutManager(new LinearLayoutManager(context));

        initHandler();
        getBangumiInfo();
    }

    /**
     * 初始化Handler
     */
    private void initHandler() {
        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                Object msgObj = msg.obj;
                if (msgObj == null) {
                    SimpleSnackBar.make(view, getString(R.string.snackBarDataErrorWarn), SimpleSnackBar.LENGTH_LONG).show();
                    backPressed();
                    return;
                }

                switch (msg.what) {
                    case 0:
                        initInfo(msgObj);

                        // 获取第一个选集的状态数
                        getBangumiStateAndRecommend();
                        break;
                    case 1:
                        initStat(msgObj);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 初始化视频基本信息
     */
    private void initInfo(Object msgObj) {
        bangumi = (Bangumi) msgObj;
        BindingUtils bindingUtils = new BindingUtils(view, context);

        bangumiInfoRecommends.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        bangumiInfoTitle.setText(bangumi.title);

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
        bangumiInfoNowAnthology.setRightValue(bangumi.bangumiAnthologyList.get(anthologyIndex).subTitle);

        bangumiInfoOrder.setSelected(bangumi.isFollow);

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
            onBangumiInfoListener.onBangumiAnthologyListener(bangumiAnthology.aid, bangumiAnthology.cid, bangumiAnthology.subTitle);
        }

        DownloadHistoryUtils downloadHistoryUtils = new DownloadHistoryUtils(context);
        DaoBaseUtils<DownloadHistory> downloadHistoryDaoUtils = downloadHistoryUtils.getDownloadHistoryDaoUtils();

        downloadedRecordCount = downloadHistoryDaoUtils.count(DownloadHistoryDao.Properties.LevelOneId.eq(seasonId));

        String record = downloadedRecordCount + "/" + bangumi.bangumiAnthologyList.size();
        bangumiInfoDownloadedRecord.setText(record);
    }

    /**
     * 初始化状态数
     */
    private void initStat(Object msgObj) {
        BangumiAnthologyStat bangumiAnthologyStat = (BangumiAnthologyStat) msgObj;

        bangumiInfoLike.setText(ValueUtils.generateCN(bangumiAnthologyStat.like));
        bangumiInfoLike.setSelected(bangumiAnthologyStat.isLike);

        bangumiInfoCoin.setText(ValueUtils.generateCN(bangumiAnthologyStat.coin));
        bangumiInfoCoin.setSelected(bangumiAnthologyStat.isCoin);

        bangumiInfoFavorite.setText(ValueUtils.generateCN(bangumi.favorites));
        bangumiInfoFavorite.setSelected(bangumiAnthologyStat.isFavorite);

        bangumiInfoShare.setText(ValueUtils.generateCN(bangumi.share));

        bangumiInfoComments.setRightValue(ValueUtils.generateCN(bangumiAnthologyStat.reply));
    }

    /**
     * 获取番剧基本信息
     */
    private void getBangumiInfo() {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                BangumiDetailParser bangumiDetailParser = new BangumiDetailParser(seasonId);
                Bangumi bangumi = bangumiDetailParser.parseData(context);

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

    /**
     * 获取番剧状态数和推荐数据
     */
    private void getBangumiStateAndRecommend() {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                BangumiAnthologyStat bangumiAnthologyStat = BangumiAnthologyStatParser.parseData(bangumi.bangumiAnthologyList.get(anthologyIndex).id);

                Message message = receiveDataHandler.obtainMessage(1);
                message.obj = bangumiAnthologyStat;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bangumi_info_title:
                easterEgg(v);
                break;
            case R.id.bangumi_info_order:
            case R.id.bangumi_info_detail:
            case R.id.bangumi_info_like:
            case R.id.bangumi_info_coin:
            case R.id.bangumi_info_favorite:
            case R.id.bangumi_info_share:
                SimpleSnackBar.make(v, getString(R.string.snackBarBuildingWarn), SimpleSnackBar.LENGTH_LONG).show();
                break;
            case R.id.bangumi_info_comments_container:
                startVideoCommentFragment();
                break;
            case R.id.bangumi_info_anthology_container:
                showAnthologyBottomSheet();
                break;
            case R.id.bangumi_info_series_container:
                showSeriesBottomSheet();
                break;
            case R.id.bangumi_info_easterEgg_container:
                // 验证读写权限
                if (!verifyIOPermission()) {
                    requestIOPermission();
                } else {
                    showDownloadBottomSheet();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 彩蛋部分
     */
    private void easterEgg(View v) {
        if (PreferenceUtils.getEasterEggStat()) {
            return;
        }

        if (easterEggSteps == 3) {
            bangumiInfoEasterEggContainer.setVisibility(View.VISIBLE);
            PreferenceUtils.setEasterEggStat();
            SimpleSnackBar.make(v, getString(R.string.easterEggWarn), Gravity.CENTER, SimpleSnackBar.LENGTH_SHORT).show();
            easterEggWarn = null;
        } else {
            SimpleSnackBar.make(v, easterEggWarn, Gravity.CENTER, SimpleSnackBar.LENGTH_SHORT).show();
            easterEggWarn += "🎉";

            easterEggSteps++;
        }
    }

    /**
     * 跳转至评论页面
     */
    private void startVideoCommentFragment() {
        VideoCommentFragment videoCommentFragment = new VideoCommentFragment(bangumi.bangumiAnthologyList.get(anthologyIndex).aid);
        videoCommentFragment.setToCommentDetailFragment(new VideoInfoAndCommentsFragment.ToCommentDetailFragment() {
            @Override
            public void toCommentDetail(Comment comment) {
                start(new VideoCommentDetailFragment(comment));
            }
        });

        start(videoCommentFragment);
    }

    /**
     * 显示选集选择底部弹窗
     */
    private void showAnthologyBottomSheet() {
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
                    bangumiInfoNowAnthology.setRightValue(bangumiAnthology.subTitle);
                    onBangumiInfoListener.onBangumiAnthologyListener(bangumiAnthology.aid, bangumiAnthology.cid, bangumiAnthology.subTitle);

                    videoAnthologyBottomSheet.dismiss();
                }
            }
        });
        videoAnthologyBottomSheet.show();
    }

    /**
     * 显示系列选择底部弹出
     */
    private void showSeriesBottomSheet() {
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
    }

    /**
     * 请求读写权限
     */
    private void requestIOPermission () {
        WarnDialog warnDialog = new WarnDialog(context, "读写权限", "由于保存资源文件时需要用到'读写权限',否则将无法正常下载视频、音频等资源");
        warnDialog.setOnWarnActionListener(new WarnDialog.OnWarnActionListener() {
            @Override
            public void onConfirm() {
                warnDialog.dismiss();
                PermissionUtil permissionUtil = new PermissionUtil(context, BangumiInfoFragment.this);
                permissionUtil.verifyPermission(PermissionUtil.Permission.RW);
            }

            @Override
            public void onCancel() {
                warnDialog.dismiss();
            }
        });
        warnDialog.show();
    }

    /**
     * 验证读写权限是否已授予
     */
    private boolean verifyIOPermission() {
        return PermissionUtil.verifyPermission(context, PermissionUtil.Permission.RW);
    }

    /**
     * 读写权限回调
     *
     * @param requestCode  请求码
     * @param permissions  权限名称
     * @param grantResults 授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1025) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                showDownloadBottomSheet();
            } else {
                SimpleSnackBar.make(view, "请授予'读写权限',否则将不能下载~", SimpleSnackBar.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 显示下载弹窗
     */
    private void showDownloadBottomSheet () {
        DownloadBottomSheet<BangumiAnthology> downloadBottomSheet = new DownloadBottomSheet<>(context);
        downloadBottomSheet.setBangumiAnthologyList(bangumi.bangumiAnthologyList);
        downloadBottomSheet.setOnClickDownloadItemListener(new DownloadBottomSheet.OnClickDownloadItemListener() {
            @Override
            public void onClickItem() {
                downloadedRecordCount++;

                String record = downloadedRecordCount + "/" + bangumi.anthologyCount;
                bangumiInfoDownloadedRecord.setText(record);
            }
        });
        downloadBottomSheet.show();
    }
}