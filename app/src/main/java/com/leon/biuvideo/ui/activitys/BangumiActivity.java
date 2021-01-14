package com.leon.biuvideo.ui.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.UserFragmentAdapters.BangumiEpAdapter;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedDetailMedia;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedRecordsForVideo;
import com.leon.biuvideo.beans.searchBean.bangumi.Bangumi;
import com.leon.biuvideo.beans.searchBean.bangumi.BangumiState;
import com.leon.biuvideo.beans.searchBean.bangumi.Ep;
import com.leon.biuvideo.beans.videoBean.play.Media;
import com.leon.biuvideo.beans.videoBean.play.Play;
import com.leon.biuvideo.ui.dialogs.AnthologyDownloadDialog;
import com.leon.biuvideo.ui.dialogs.BangumiDetailDialog;
import com.leon.biuvideo.ui.dialogs.SingleVideoQualityDialog;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.SimpleDownloadThread;
import com.leon.biuvideo.utils.SimpleThreadPool;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.utils.downloadUtils.ResourceUtils;
import com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils.MediaParseUtils;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.BangumiParser;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.BangumiStateParse;
import com.leon.biuvideo.values.Tables;

import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;

public class BangumiActivity extends AppCompatActivity implements View.OnClickListener {
    private Bangumi bangumi;
    private BangumiState bangumiState;
    private int selectAnthologyIndex;

    private boolean isSingleAnthology  = false;

    private List<Map.Entry<Integer, Media>> videoEntries;
    private List<Map.Entry<Integer, Media>> audioEntries;

    private SimpleThreadPool simpleThreadPool;
    private DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bangumi);

        init();
        initView();
    }

    private void init() {
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
    }

    private void initView() {
        BindingUtils bindingUtils = new BindingUtils(getWindow().getDecorView(), getApplicationContext());
        bindingUtils.setText(R.id.bangumi_textView_title, bangumi.title)
                .setText(R.id.bangumi_textView_bangumiState, bangumi.eps.size() == bangumi.epSize ? "已完结" : "连载中")
                .setText(R.id.bangumi_textView_epSize, "共" + bangumi.epSize + "话")
                .setText(R.id.bangumi_textView_score, bangumi.score + "分")
                .setText(R.id.bangumi_textView_play, ValueFormat.generateCN(bangumiState.views) + "播放")
                .setText(R.id.bangumi_textView_follow, ValueFormat.generateCN(bangumiState.seriesFollow) + "系列追番")
                .setText(R.id.bangumi_textView_like, ValueFormat.generateCN(bangumiState.likes) + "点赞")
                .setText(R.id.bangumi_textView_coin, ValueFormat.generateCN(bangumiState.coins) + "投币")
                .setText(R.id.bangumi_textView_favorite, ValueFormat.generateCN(bangumiState.follow) + "收藏")
                .setOnClickListener(R.id.bangumi_imageView_back, this)
                .setOnClickListener(R.id.bangumi_textView_toDetail, this)
                .setOnClickListener(R.id.bangumi_imageView_download, this)
                .setOnClickListener(R.id.bangumi_imageView_favorite, this)
                .setOnClickListener(R.id.bangumi_textView_jumpToOriginal, this);

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
                        Toast.makeText(BangumiActivity.this, "选择的视频已经在播放了~~", Toast.LENGTH_SHORT).show();
                    } else {
                        selectAnthologyIndex = position;
                    }
                }
            });
            recyclerView.setAdapter(bangumiEpAdapter);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bangumi_imageView_back:
                this.finish();
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
                    Toast.makeText(getApplicationContext(), R.string.network_sign, Toast.LENGTH_SHORT).show();
                    break;
                }

                if (downloadRecordsDatabaseUtils == null) {
                    SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(getApplicationContext(), Tables.DownloadDetailsForVideo);
                    downloadRecordsDatabaseUtils = (DownloadRecordsDatabaseUtils) sqLiteHelperFactory.getInstance();
                }

                // 如果番剧选集个数为1，则直接显示SingleVideoQualityDialog
                if (isSingleAnthology) {
                    if (videoEntries == null) {
                        Play play = MediaParseUtils.parseMedia(bangumi.eps.get(selectAnthologyIndex).cid, true);

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
                            saveSingleVideo(mediaEntry);
                        }
                    });
                    singleVideoQualityDialog.show();
                } else {
                    AnthologyDownloadDialog anthologyDownloadDialog = new AnthologyDownloadDialog(BangumiActivity.this, bangumi.getAnthologyInfoList());
                    anthologyDownloadDialog.setOnDownloadListener(new AnthologyDownloadDialog.OnDownloadListener() {
                        @Override
                        public void onDownload(int qualityId, long cid, int position, String subTitle) {
                            // 保存选定视频
                            Fuck.blue("saveSingleVideo----cid:" + cid + "--qualityIndex:" + qualityId + "--subTitle:" + subTitle);

                            //获取视频选集信息
                            Play playWithDownload = MediaParseUtils.parseMedia(cid, true);

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

                            saveSingleVideo(videoEntry);
                        }

                        @Override
                        public void onSaveAll(int qualityId) {
                            // 保存所有视频
                            Toast.makeText(getApplicationContext(), "Sorry~该功能暂未进行开发，请谅解＞︿＜", Toast.LENGTH_SHORT).show();
                        }
                    });

                    anthologyDownloadDialog.show();
                }

                break;
            case R.id.bangumi_imageView_favorite:

                break;
            default:
                break;
        }
    }

    private void saveSingleVideo(Map.Entry<Integer, Media> mediaEntry) {
        if (simpleThreadPool == null) {
            simpleThreadPool = new SimpleThreadPool(SimpleThreadPool.DownloadTaskNum, SimpleThreadPool.DownloadTask);
        }

        //获取视频路径
        String videoUrlBase = mediaEntry.getValue().baseUrl;

        //获取音频路径,默认只获取第一个
        String audioUrlBase = audioEntries.get(0).getValue().baseUrl;

        // 添加至downloadedRecordsForVideo
        addToDownloadedRecordsForVideo();

        // 添加至DownloadDetailsForMedia
        String fileName = addToDownloadDetailMedia(mediaEntry, videoUrlBase, audioUrlBase);

        Toast.makeText(getApplicationContext(), "已加入缓存队列中", Toast.LENGTH_SHORT).show();

        SimpleDownloadThread simpleDownloadThread = new SimpleDownloadThread(getApplicationContext(), videoUrlBase, audioUrlBase, fileName);
        simpleThreadPool.submit(new FutureTask<>(simpleDownloadThread));
    }

    private void addToDownloadedRecordsForVideo() {
        DownloadedRecordsForVideo downloadedRecordsForVideo = new DownloadedRecordsForVideo();
        downloadedRecordsForVideo.title = bangumi.title;
        downloadedRecordsForVideo.upName = "bangumi";
        downloadedRecordsForVideo.mainId = String.valueOf(bangumi.seasonId);
        downloadedRecordsForVideo.cover = bangumi.cover;

        downloadRecordsDatabaseUtils.addVideo(downloadedRecordsForVideo);
    }

    private String addToDownloadDetailMedia(Map.Entry<Integer, Media> mediaEntry, String videoUrlBase, String audioUrlBase) {
        DownloadedDetailMedia downloadedDetailMedia = new DownloadedDetailMedia();
        Ep ep = bangumi.eps.get(selectAnthologyIndex);

        String fileName = bangumi.mediaId + "-" + ep.longTitle + "-" + mediaEntry.getValue().quality.split(" ")[1];

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
        super.onDestroy();
    }
}