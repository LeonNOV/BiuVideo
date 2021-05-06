package com.leon.biuvideo.ui.resourcesFragment.video.contribution;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.RecommendAdapter;
import com.leon.biuvideo.beans.resourcesBeans.VideoRecommend;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoInfo;
import com.leon.biuvideo.greendao.dao.DaoBaseUtils;
import com.leon.biuvideo.greendao.dao.DownloadHistory;
import com.leon.biuvideo.greendao.dao.DownloadHistoryDao;
import com.leon.biuvideo.greendao.daoutils.DownloadHistoryUtils;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.DownloadBottomSheet;
import com.leon.biuvideo.ui.resourcesFragment.video.OnBottomSheetWithItemListener;
import com.leon.biuvideo.ui.resourcesFragment.video.OnVideoAnthologyListener;
import com.leon.biuvideo.ui.resourcesFragment.video.VideoAnthologyBottomSheet;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.ui.views.TagView;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.WarnDialog;
import com.leon.biuvideo.utils.BindingUtils;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.PermissionUtil;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.VideoInfoParser;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.VideoRecommendParser;
import com.leon.biuvideo.values.FeaturesName;
import com.leon.biuvideo.values.FragmentType;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

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
    private final String bvid;

    int easterEggSteps = 0;
    String easterEggWarn = "🎉";

    private ImageView videoInfoFace;
    private TextView videoInfoFollow;

    private TextView videoInfoLike;
    private TextView videoInfoCoin;
    private TextView videoInfoFavorite;
    private TextView videoInfoShare;

    private LinearLayout videoInfoAnthologyContainer;
    private TagView videoInfoNowAnthology;
    private LoadingRecyclerView videoInfoRecommends;

    private int anthologyIndex = 0;
    private OnVideoAnthologyListener onVideoAnthologyListener;
    private VideoInfo videoInfo;
    private LinearLayout videoInfoEasterEggContainer;
    private TextView videoInfoDownloadedRecord;
    private int downloadedRecordCount = 0;
    private DownloadBottomSheet<VideoInfo.VideoAnthology> downloadBottomSheet;

    public VideoInfoFragment(String bvid) {
        this.bvid = bvid;
    }

    public void setOnVideoAnthologyListener(OnVideoAnthologyListener onVideoAnthologyListener) {
        this.onVideoAnthologyListener = onVideoAnthologyListener;
    }

    @Override
    protected int setLayout() {
        return R.layout.video_info_fragment;
    }

    @Override
    protected void initView() {
        findView(R.id.video_fragment_container).setBackgroundResource(R.color.white);
        
        videoInfoFace = findView(R.id.video_info_face);
        videoInfoFace.setOnClickListener(this);

        videoInfoFollow = findView(R.id.video_info_follow);
        videoInfoFollow.setOnClickListener(this);

        videoInfoLike = findView(R.id.video_info_like);
        videoInfoLike.setOnClickListener(this);

        videoInfoCoin = findView(R.id.video_info_coin);
        videoInfoCoin.setOnClickListener(this);

        videoInfoFavorite = findView(R.id.video_info_favorite);
        videoInfoFavorite.setOnClickListener(this);

        videoInfoShare = findView(R.id.video_info_share);
        videoInfoShare.setOnClickListener(this);

        videoInfoAnthologyContainer = findView(R.id.video_info_anthology_container);
        videoInfoAnthologyContainer.setOnClickListener(this);

        videoInfoNowAnthology = findView(R.id.video_info_now_anthology);

        videoInfoRecommends = findView(R.id.video_info_recommends);
        videoInfoRecommends.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        videoInfoEasterEggContainer = findView(R.id.video_info_easterEgg_container);
        videoInfoEasterEggContainer.setOnClickListener(this);

        if (PreferenceUtils.getEasterEggStat()) {
            videoInfoEasterEggContainer.setVisibility(View.VISIBLE);
        }

        initHandler();
        getVideoInfo();
    }

    private void initHandler() {
        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                switch (msg.what) {
                    case 0:
                        videoInfo = (VideoInfo) msg.obj;

                        Glide
                                .with(context)
                                .load(videoInfo.userInfo.userFace += PreferenceUtils.getFeaturesStatus(FeaturesName.IMG_ORIGINAL_MODEL) ?
                                        ImagePixelSize.FACE.value : "")
                                .into(videoInfoFace);

                        new BindingUtils(view, context)
                                .setText(R.id.video_info_userName, videoInfo.userInfo.userName)
                                .setText(R.id.video_info_title, videoInfo.title)
                                .setText(R.id.video_info_play, ValueUtils.generateCN(videoInfo.videoStat.view))
                                .setText(R.id.video_info_danmaku, ValueUtils.generateCN(videoInfo.videoStat.danmaku))
                                .setText(R.id.video_info_pubTime, ValueUtils.generateTime(videoInfo.pubTime, "yyyy-MM-dd", true))
                                .setText(R.id.video_info_bvid, videoInfo.bvid)
                                .setOnClickListener(R.id.video_info_bvid, VideoInfoFragment.this);

                        videoInfoLike.setText(ValueUtils.generateCN(videoInfo.videoStat.like));
                        videoInfoCoin.setText(ValueUtils.generateCN(videoInfo.videoStat.like));
                        videoInfoFavorite.setText(ValueUtils.generateCN(videoInfo.videoStat.like));
                        videoInfoShare.setText(ValueUtils.generateCN(videoInfo.videoStat.like));

                        if (videoInfo.videoAnthologyList.size() == 0) {
                            videoInfoAnthologyContainer.setVisibility(View.GONE);
                        } else {
                            videoInfoNowAnthology.setRightValue(videoInfo.videoAnthologyList.get(anthologyIndex).part);
                        }

                        // 开始播放视频
                        if (onVideoAnthologyListener != null) {
                            VideoInfo.VideoAnthology videoAnthology = videoInfo.videoAnthologyList.get(anthologyIndex);
                            onVideoAnthologyListener.onAnthology(videoAnthology.cid, videoAnthology.part);
                        }

                        // 如果存在多个选集则获取已下载的选集数
                        if (videoInfo.isMultiAnthology) {
                            videoInfoDownloadedRecord = findView(R.id.video_info_downloaded_record);
                            DownloadHistoryUtils downloadHistoryUtils = new DownloadHistoryUtils(context);
                            DaoBaseUtils<DownloadHistory> downloadHistoryDaoUtils = downloadHistoryUtils.getDownloadHistoryDaoUtils();

                            // 获取该视频已下载的选集数
                            List<DownloadHistory> downloadHistoryList = downloadHistoryDaoUtils.
                                    queryByQueryBuilder(DownloadHistoryDao.Properties.IsCompleted.eq(true),
                                            DownloadHistoryDao.Properties.LevelOneId.eq(videoInfo.bvid));

                            downloadedRecordCount = downloadHistoryList.size();
                            String record = downloadedRecordCount + "/" + videoInfo.videos;
                            videoInfoDownloadedRecord.setText(record);
                        }

                        getOtherData();
                        break;
                    case 1:
                        JSONObject jsonObject = (JSONObject) msg.obj;

                        // 获取当前账户是否已关注当前UP主
                        boolean attention = jsonObject.getBooleanValue("attention");
                        videoInfoFollow.setSelected(attention);
                        if (attention) {
                            videoInfoFollow.setText("已关注");
                        } else {
                            videoInfoFollow.setText("关注");
                        }

                        // 获取投币状态
                        boolean coin = jsonObject.getIntValue("coin") > 0;
                        videoInfoCoin.setSelected(coin);

                        // 获取收藏状态
                        boolean favorite = jsonObject.getBooleanValue("favorite");
                        videoInfoFavorite.setSelected(favorite);

                        // 获取点赞状态
                        boolean like = jsonObject.getBooleanValue("like");
                        videoInfoLike.setSelected(like);

                        getComments();
                        break;
                    case 2:
                        List<VideoRecommend> videoRecommendList = (List<VideoRecommend>) msg.obj;

                        RecommendAdapter recommendAdapter = new RecommendAdapter(videoRecommendList, RecommendAdapter.SINGLE_COLUMN, context);
                        recommendAdapter.setHasStableIds(true);
                        videoInfoRecommends.setRecyclerViewAdapter(recommendAdapter);
                        videoInfoRecommends.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
                        videoInfoRecommends.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void getVideoInfo() {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                VideoInfo videoInfo = VideoInfoParser.parseData(bvid, context);

                Message message = receiveDataHandler.obtainMessage(0);
                message.obj = videoInfo;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    private void getOtherData() {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                // 获取当前视频与已登录用户的关系
                Map<String, String> params = new HashMap<>(1);
                params.put("bvid", videoInfo.bvid);
                JSONObject response = HttpUtils.getResponse(BiliBiliAPIs.VIDEO_STATUS, Headers.of(HttpUtils.getAPIRequestHeader()), params);
                JSONObject data = response.getJSONObject("data");

                Message message = receiveDataHandler.obtainMessage(1);
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
                List<VideoRecommend> videoRecommendList = VideoRecommendParser.parseData(videoInfo.bvid);

                Message message = receiveDataHandler.obtainMessage(2);
                message.obj = videoRecommendList;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_info_face:
                startPublicFragment(FragmentType.BILI_USER, videoInfo.userInfo.userMid);
                break;
            case R.id.video_info_follow:
            case R.id.video_info_like:
            case R.id.video_info_coin:
            case R.id.video_info_favorite:
            case R.id.video_info_share:
                SimpleSnackBar.make(v, getString(R.string.snackBarBuildingWarn), SimpleSnackBar.LENGTH_LONG).show();
                break;
            case R.id.video_info_anthology_container:
                VideoAnthologyBottomSheet videoAnthologyBottomSheet = new VideoAnthologyBottomSheet(context, anthologyIndex);
                videoAnthologyBottomSheet.setVideoAnthologyList(videoInfo.videoAnthologyList);
                videoAnthologyBottomSheet.setOnBottomSheetWithItemListener(new OnBottomSheetWithItemListener() {
                    @Override
                    public void onItem(int position) {
                        if (onVideoAnthologyListener != null) {
                            anthologyIndex = position;

                            VideoInfo.VideoAnthology videoAnthology = videoInfo.videoAnthologyList.get(anthologyIndex);

                            videoInfoNowAnthology.setRightValue(videoAnthology.part);
                            onVideoAnthologyListener.onAnthology(videoAnthology.cid, videoAnthology.part);
                            videoAnthologyBottomSheet.dismiss();
                        }
                    }
                });
                videoAnthologyBottomSheet.show();
                break;
            case R.id.video_info_bvid:
                if (PreferenceUtils.getEasterEggStat()) {
                    return;
                }

                if (easterEggSteps == 3) {
                    videoInfoEasterEggContainer.setVisibility(View.VISIBLE);
                    PreferenceUtils.setEasterEggStat();
                    SimpleSnackBar.make(v, "🎉🎉🎉Surprise!!! 请在视频信息界面找到不一样的地方!!!🎉🎉🎉", SimpleSnackBar.LENGTH_SHORT).show();
                    easterEggWarn = null;
                    return;
                } else {
                    SimpleSnackBar.make(v, easterEggWarn, SimpleSnackBar.LENGTH_SHORT).show();
                    easterEggWarn += "🎉";

                    easterEggSteps ++;
                }

                break;
            case R.id.video_info_easterEgg_container:
                // 验证读写权限
                if (!verifyIOPermission()) {
                    requestIOPermission();
                } else {
                    downloadBottomSheet = new DownloadBottomSheet<>(context);
                    downloadBottomSheet.setVideoAnthologyList(videoInfo.videoAnthologyList);
                    downloadBottomSheet.setOnClickDownloadItemListener(new DownloadBottomSheet.OnClickDownloadItemListener() {
                        @Override
                        public void onClickItem() {
                            downloadedRecordCount++;

                            String record = downloadedRecordCount + "/" + videoInfo.videos;
                            videoInfoDownloadedRecord.setText(record);
                        }
                    });
                    downloadBottomSheet.show();
                }
                break;
            default:
                break;
        }
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
                PermissionUtil permissionUtil = new PermissionUtil(context, VideoInfoFragment.this);
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
                downloadBottomSheet = new DownloadBottomSheet<>(context);
                downloadBottomSheet.setVideoAnthologyList(videoInfo.videoAnthologyList);
                downloadBottomSheet.setOnClickDownloadItemListener(new DownloadBottomSheet.OnClickDownloadItemListener() {
                    @Override
                    public void onClickItem() {
                        downloadedRecordCount++;

                        String record = downloadedRecordCount + "/" + videoInfo.videos;
                        videoInfoDownloadedRecord.setText(record);
                    }
                });
                downloadBottomSheet.show();
            } else {
                SimpleSnackBar.make(view, "请授予'读写权限',否则将不能下载~", SimpleSnackBar.LENGTH_SHORT).show();
            }
        }
    }
}
