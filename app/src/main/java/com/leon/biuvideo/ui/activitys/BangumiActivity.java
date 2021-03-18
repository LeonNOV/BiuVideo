package com.leon.biuvideo.ui.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.userAdapters.BangumiEpAdapter;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedDetailMedia;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedRecordsForVideo;
import com.leon.biuvideo.beans.orderBeans.LocalOrder;
import com.leon.biuvideo.beans.searchBean.bangumi.Bangumi;
import com.leon.biuvideo.beans.searchBean.bangumi.BangumiState;
import com.leon.biuvideo.beans.searchBean.bangumi.Ep;
import com.leon.biuvideo.beans.videoBean.play.Media;
import com.leon.biuvideo.beans.videoBean.play.Play;
import com.leon.biuvideo.ui.dialogs.AnthologyDownloadDialog;
import com.leon.biuvideo.ui.dialogs.BangumiDetailDialog;
import com.leon.biuvideo.ui.dialogs.LoadingDialog;
import com.leon.biuvideo.ui.dialogs.SingleVideoQualityDialog;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.SimpleDownloadThread;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.SimpleThreadPool;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.LocalOrdersDatabaseUtils;
import com.leon.biuvideo.utils.downloadUtils.ResourceUtils;
import com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils.MediaParser;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.BangumiParser;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.BangumiStateParse;
import com.leon.biuvideo.values.LocalOrderType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;

public class BangumiActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView
            bangumi_textView_title,
            bangumi_textView_bangumiState,
            bangumi_textView_epSize,
            bangumi_textView_score,
            bangumi_textView_play,
            bangumi_textView_follow,
            bangumi_textView_like,
            bangumi_textView_coin,
            bangumi_textView_favorite;

    private Bangumi bangumi;
    private BangumiState bangumiState;
    private int selectAnthologyIndex;
    private TextView bangumi_textView_selectedBangumiName;

    private boolean isSingleAnthology  = false;

    private List<Map.Entry<Integer, Media>> videoEntries;
    private List<Map.Entry<Integer, Media>> audioEntries;

    private SimpleThreadPool simpleThreadPool;
    private DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils;
    private MediaParser mediaParser;
    private LocalOrdersDatabaseUtils localOrdersDatabaseUtils;
    private ImageView bangumi_imageView_favoriteMark;

    private final static String PROMPT = "当前已选择的选集为：";
    private final static LocalOrderType localOrderType = LocalOrderType.BANGUMI;
    private boolean isHaveLocalOrder = false;
    private LinearLayout bangumi_linearLayout;
    private LoadingDialog loadingDialog;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bangumi);

        initView();
    }

    private void initView() {
        bangumi_linearLayout = findViewById(R.id.bangumi_linearLayout);

        bangumi_textView_title = findViewById(R.id.bangumi_textView_title);
        bangumi_textView_bangumiState = findViewById(R.id.bangumi_textView_bangumiState);
        bangumi_textView_epSize = findViewById(R.id.bangumi_textView_epSize);
        bangumi_textView_score = findViewById(R.id.bangumi_textView_score);
        bangumi_textView_play = findViewById(R.id.bangumi_textView_play);
        bangumi_textView_follow = findViewById(R.id.bangumi_textView_follow);
        bangumi_textView_like = findViewById(R.id.bangumi_textView_like);
        bangumi_textView_coin = findViewById(R.id.bangumi_textView_coin);
        bangumi_textView_favorite = findViewById(R.id.bangumi_textView_favorite);

        BindingUtils bindingUtils = new BindingUtils(getWindow().getDecorView(), getApplicationContext());
        bindingUtils
                .setOnClickListener(R.id.bangumi_imageView_back, this)
                .setOnClickListener(R.id.bangumi_textView_toDetail, this)
                .setOnClickListener(R.id.bangumi_imageView_download, this)
                .setOnClickListener(R.id.bangumi_linearLayout_addFavorite, this)
                .setOnClickListener(R.id.bangumi_imageView_toDownloadList, this)
                .setOnClickListener(R.id.bangumi_textView_jumpToOriginal, this);

        bangumi_imageView_favoriteMark = findViewById(R.id.bangumi_imageView_favoriteMark);

        bangumi_textView_selectedBangumiName = findViewById(R.id.bangumi_textView_selectedBangumiName);

        loadingDialog = new LoadingDialog(BangumiActivity.this);
        loadingDialog.show();

        loadData();
    }

    private void loadData() {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();

                // 获取初始数据
                bangumi = (Bangumi) extras.getSerializable("bangumi");
                if (bangumi.epSize < 2) {
                    isSingleAnthology = true;
                }
                selectAnthologyIndex = extras.getInt("selectAnthologyIndex", 0);

                // 获取番剧选集cid及其各选集cover
                BangumiParser bangumiParser = new BangumiParser();
                bangumi.eps = bangumiParser.getEpCids(bangumi.seasonId, bangumi.eps);

                BangumiStateParse bangumiStateParse = new BangumiStateParse();
                bangumiState = bangumiStateParse.bangumiStateParse(bangumi.seasonId);

                localOrdersDatabaseUtils = new LocalOrdersDatabaseUtils(getApplicationContext());
                isHaveLocalOrder = localOrdersDatabaseUtils.queryLocalOrder(String.valueOf(bangumi.mediaId), String.valueOf(bangumi.seasonId), localOrderType);

                Message message = handler.obtainMessage();
                message.what = 0;

                Bundle bundle = new Bundle();
                bundle.putBoolean("loadState", true);

                message.setData(bundle);
                handler.sendMessage(message);
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                boolean loadState = msg.getData().getBoolean("loadState");

                if (loadState) {
                    initValue();
                }

                loadingDialog.dismiss();

                return true;
            }
        });
    }

    private void initValue() {
        bangumi_textView_title.setText(bangumi.title);
        bangumi_textView_bangumiState.setText(bangumi.eps.size() == bangumi.epSize ? "已完结" : "连载中");
        bangumi_textView_epSize.setText("共" + bangumi.epSize + "话");
        bangumi_textView_score.setText(bangumi.score + "分");
        bangumi_textView_play.setText(ValueUtils.generateCN(bangumiState.views) + "播放");
        bangumi_textView_follow.setText(ValueUtils.generateCN(bangumiState.seriesFollow) + "系列追番");
        bangumi_textView_like.setText(ValueUtils.generateCN(bangumiState.likes) + "点赞");
        bangumi_textView_coin.setText(ValueUtils.generateCN(bangumiState.coins) + "投币");
        bangumi_textView_favorite.setText(ValueUtils.generateCN(bangumiState.follow) + "收藏");

        bangumi_imageView_favoriteMark.setImageResource(isHaveLocalOrder ? R.drawable.icon_video_favorite : R.drawable.icon_video_no_favorite);

        setSelectedAnthologyName();

        CardView cardView = findViewById(R.id.video_anthology_cardView);
        if (isSingleAnthology) {
            cardView.setVisibility(View.GONE);
        } else {
            RecyclerView recyclerView = findViewById(R.id.bangumi_recyclerView_anthologyList);
            BangumiEpAdapter bangumiEpAdapter = new BangumiEpAdapter(getApplicationContext(), bangumi.eps);
            bangumiEpAdapter.setOnEpClickListener(new BangumiEpAdapter.OnEpClickListener() {
                @Override
                public void onEpClick(int position) {
                    if (position == selectAnthologyIndex) {
                        SimpleSnackBar.make(bangumi_linearLayout, R.string.isPlaying, SimpleSnackBar.LENGTH_SHORT).show();
                    } else {
                        if (!InternetUtils.checkNetwork(getApplicationContext())) {
                            SimpleSnackBar.make(bangumi_linearLayout, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();
                            return;
                        }

                        selectAnthologyIndex = position;
                        setSelectedAnthologyName();
                    }
                }
            });
            recyclerView.setAdapter(bangumiEpAdapter);
        }
    }

    /**
     * 设置已选择选集提示的文本信息
     */
    private void setSelectedAnthologyName() {
        Ep ep = bangumi.eps.get(selectAnthologyIndex);
        String selectedAnthologyName = PROMPT + ep.title + "-" + ep.longTitle;
        bangumi_textView_selectedBangumiName.setText(selectedAnthologyName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bangumi_imageView_back:
                this.finish();
                break;
            case R.id.bangumi_imageView_toDownloadList:
                Intent toDownloadIntent = new Intent(this, DownloadedActivity.class);
                startActivity(toDownloadIntent);
                break;
            case R.id.bangumi_textView_jumpToOriginal:
                //跳转到源网站
                Intent intentOriginUrl = new Intent();
                intentOriginUrl.setAction("android.intent.action.VIEW");
                Uri uri = Uri.parse(bangumi.eps.get(selectAnthologyIndex).url);
                intentOriginUrl.setData(uri);
                startActivity(intentOriginUrl);
                break;
            case R.id.bangumi_textView_toDetail:
                BangumiDetailDialog bangumiDetailDialog = new BangumiDetailDialog(BangumiActivity.this, bangumi);
                bangumiDetailDialog.show();
                break;
            case R.id.bangumi_imageView_download:
                //判断是否有网络
                boolean isHaveNetworkSaveVideo = InternetUtils.checkNetwork(getApplicationContext());

                if (!isHaveNetworkSaveVideo) {
                    SimpleSnackBar.make(v, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();
                    break;
                }

                if (downloadRecordsDatabaseUtils == null) {
                    downloadRecordsDatabaseUtils = new DownloadRecordsDatabaseUtils(getApplicationContext());
                }

                if (mediaParser == null) {
                    mediaParser = new MediaParser();
                }

                // 如果番剧选集个数为1，则直接显示SingleVideoQualityDialog
                if (isSingleAnthology) {
                    if (videoEntries == null) {

                        Play play = mediaParser.parseMedia(null, bangumi.eps.get(selectAnthologyIndex).cid, true);

                        videoEntries = play.videoEntries();
                        for (Map.Entry<Integer, Media> entry : videoEntries) {
                            entry.getValue().isDownloaded = downloadRecordsDatabaseUtils
                                    .queryVideoDownloadState(String.valueOf(bangumi.eps.get(selectAnthologyIndex).cid + entry.getKey()));
                        }

                        audioEntries = play.audioEntries();
                    }

                    SingleVideoQualityDialog singleVideoQualityDialog = new SingleVideoQualityDialog(BangumiActivity.this, videoEntries);
                    singleVideoQualityDialog.setOnQualityClickListener(new SingleVideoQualityDialog.OnQualityClickListener() {
                        @Override
                        public void onClickListener(Map.Entry<Integer, Media> mediaEntry) {
                            if (!InternetUtils.checkNetwork(getApplicationContext())) {
                                SimpleSnackBar.make(v, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();
                                return;
                            }
                            saveSingleVideo(mediaEntry, selectAnthologyIndex);
                        }
                    });
                    singleVideoQualityDialog.show();
                } else {
                    AnthologyDownloadDialog anthologyDownloadDialog = new AnthologyDownloadDialog(BangumiActivity.this, bangumi.getAnthologyInfoList());
                    anthologyDownloadDialog.setOnDownloadListener(new AnthologyDownloadDialog.OnDownloadListener() {
                        @Override
                        public void onDownload(int qualityId, long cid, int position, String subTitle) {
                            if (!InternetUtils.checkNetwork(getApplicationContext())) {
                                SimpleSnackBar.make(anthologyDownloadDialog.view, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();
                                return;
                            }

                            //获取视频选集信息
                            Play playWithDownload = mediaParser.parseMedia(null, cid, true);

                            if (playWithDownload == null) {
                                SimpleSnackBar.make(anthologyDownloadDialog.view, "获取不到该番剧的下载数据，可能该番剧有地区限制", SimpleSnackBar.LENGTH_SHORT).show();
                                return;
                            }

                            videoEntries = playWithDownload.videoEntries();
                            for (Map.Entry<Integer, Media> entry : videoEntries) {
                                entry.getValue().isDownloaded = downloadRecordsDatabaseUtils
                                        .queryVideoDownloadState(String.valueOf(bangumi.eps.get(selectAnthologyIndex).cid + entry.getKey()));
                            }

                            audioEntries = playWithDownload.audioEntries();

                            Map.Entry<Integer, Media> videoEntry = videoEntries.get(0);
                            for (Map.Entry<Integer, Media> entry : videoEntries) {
                                if (entry.getKey() == qualityId) {
                                    videoEntry = entry;
                                    break;
                                }
                            }

                            saveSingleVideo(videoEntry, position);
                        }

                        @Override
                        public void onSaveAll(int qualityId) {
                            // 保存所有视频
                            SimpleSnackBar.make(v, "Sorry~该功能暂未进行开发，请谅解＞︿＜", SimpleSnackBar.LENGTH_SHORT).show();
                        }
                    });

                    anthologyDownloadDialog.show();
                }

                break;
            case R.id.bangumi_linearLayout_addFavorite:
                boolean operatingStatus;

                if (isHaveLocalOrder) {
                    operatingStatus = localOrdersDatabaseUtils.deleteLocalOrder(String.valueOf(bangumi.mediaId), String.valueOf(bangumi.seasonId), localOrderType);

                    if (operatingStatus) {
                        bangumi_imageView_favoriteMark.setImageResource(R.drawable.icon_video_no_favorite);
                        SimpleSnackBar.make(v, R.string.remFavoriteSign, SimpleSnackBar.LENGTH_SHORT).show();
                        isHaveLocalOrder = false;
                    }
                } else {
                    LocalOrder localOrder = new LocalOrder();
                    Map<String, Object> map = new HashMap<>();
                    map.put("title", bangumi.title);
                    map.put("cover", bangumi.cover);
                    map.put("desc", bangumi.desc);
                    map.put("area", bangumi.area);
                    map.put("bangumiState", bangumi.bangumiState);
                    map.put("angleTitle", bangumi.angleTitle);
                    localOrder.jsonObject = new JSONObject(map);
                    localOrder.mainId = String.valueOf(bangumi.mediaId);
                    localOrder.subId = String.valueOf(bangumi.seasonId);
                    localOrder.orderType = localOrderType;
                    localOrder.addTime = System.currentTimeMillis();

                    operatingStatus = localOrdersDatabaseUtils.addLocalOrder(localOrder);

                    if (operatingStatus) {
                        bangumi_imageView_favoriteMark.setImageResource(R.drawable.icon_video_favorite);
                        SimpleSnackBar.make(v, R.string.addFavoriteSign, SimpleSnackBar.LENGTH_SHORT).show();
                        isHaveLocalOrder = true;
                    }
                }
                break;
            default:
                break;
        }
    }

    private void saveSingleVideo(Map.Entry<Integer, Media> mediaEntry, int position) {
        //获取视频路径
        String videoUrlBase = mediaEntry.getValue().baseUrl;

        //获取音频路径,默认只获取第一个
        String audioUrlBase = audioEntries.get(0).getValue().baseUrl;

        // 添加至downloadedRecordsForVideo
        addToDownloadedRecordsForVideo();

        // 添加至DownloadDetailsForMedia
        String fileName = addToDownloadDetailMedia(mediaEntry, videoUrlBase, audioUrlBase, position);

        SimpleSnackBar.make(bangumi_linearLayout, R.string.isDownloading, SimpleSnackBar.LENGTH_SHORT).show();

        SimpleDownloadThread simpleDownloadThread = new SimpleDownloadThread(getApplicationContext(),
                String.valueOf(bangumi.seasonId),
                bangumi.eps.get(position).cid,
                mediaEntry.getKey(),
                videoUrlBase,
                audioUrlBase,
                fileName);
        SimpleThreadPool.submit(new FutureTask<>(simpleDownloadThread), null);
    }

    private void addToDownloadedRecordsForVideo() {
        DownloadedRecordsForVideo downloadedRecordsForVideo = new DownloadedRecordsForVideo();
        downloadedRecordsForVideo.title = bangumi.title;
        downloadedRecordsForVideo.upName = "bangumi";
        downloadedRecordsForVideo.mainId = String.valueOf(bangumi.seasonId);
        downloadedRecordsForVideo.cover = bangumi.cover;

        downloadRecordsDatabaseUtils.addVideo(downloadedRecordsForVideo);
    }

    private String addToDownloadDetailMedia(Map.Entry<Integer, Media> mediaEntry, String videoUrlBase, String audioUrlBase, int position) {
        DownloadedDetailMedia downloadedDetailMedia = new DownloadedDetailMedia();
        Ep ep = bangumi.eps.get(position);

        String fileName = ep.cid + "-" + ep.longTitle + "-" + mediaEntry.getValue().quality.split(" ")[1];

        downloadedDetailMedia.fileName = fileName;
        downloadedDetailMedia.cover = ep.cover;
        downloadedDetailMedia.title = ep.longTitle;
        downloadedDetailMedia.videoUrl = videoUrlBase;
        downloadedDetailMedia.audioUrl = audioUrlBase;
        downloadedDetailMedia.qualityId = mediaEntry.getKey();

        // 获取视频和音频总大小
        downloadedDetailMedia.size = ResourceUtils.getResourcesSize(videoUrlBase) + ResourceUtils.getResourcesSize(audioUrlBase);
        downloadedDetailMedia.mainId = String.valueOf(bangumi.seasonId);
        downloadedDetailMedia.subId = ep.cid;
        downloadedDetailMedia.resourceMark = downloadedDetailMedia.subId + "-" + downloadedDetailMedia.qualityId;
        downloadedDetailMedia.isVideo = true;

        downloadRecordsDatabaseUtils.addMediaDetail(downloadedDetailMedia);
        return fileName;
    }

    @Override
    protected void onDestroy() {
        if (downloadRecordsDatabaseUtils != null) {
            downloadRecordsDatabaseUtils.close();
        }

        if (localOrdersDatabaseUtils != null) {
            localOrdersDatabaseUtils.close();
        }
        super.onDestroy();
    }
}