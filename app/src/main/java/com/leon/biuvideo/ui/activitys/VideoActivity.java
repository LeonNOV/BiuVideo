package com.leon.biuvideo.ui.activitys;

import android.content.pm.PackageManager;
import android.os.Looper;
import android.view.*;
import android.webkit.WebView;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.AnthologyAdapter;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedDetailMedia;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedRecordsForVideo;
import com.leon.biuvideo.beans.orderBeans.LocalOrder;
import com.leon.biuvideo.beans.orderBeans.LocalVideoFolder;
import com.leon.biuvideo.beans.videoBean.play.Media;
import com.leon.biuvideo.beans.videoBean.play.Play;
import com.leon.biuvideo.beans.videoBean.view.AnthologyInfo;
import com.leon.biuvideo.beans.videoBean.view.ViewPage;
import com.leon.biuvideo.ui.dialogs.AddVideoDialog;
import com.leon.biuvideo.ui.dialogs.AnthologyDownloadDialog;
import com.leon.biuvideo.ui.dialogs.SingleVideoQualityDialog;
import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.SimpleDownloadThread;
import com.leon.biuvideo.utils.SimpleThreadPool;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.LocalOrdersDatabaseUtils;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.downloadUtils.ResourceUtils;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.WebViewUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.values.LocalOrderType;
import com.leon.biuvideo.values.Tables;
import com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils.MediaParser;
import com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils.ViewParser;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.FutureTask;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 视频观看activity
 */
public class VideoActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView video_anthology_cardView;
    private RecyclerView video_recyclerView_singleVideoList;

    private CircleImageView video_circleImageView_face;
    private ImageView video_imageView_addFavorite;
    private ExpandableTextView expand_text_view;
    private TextView
            video_textView_name,
            video_textView_title,
            video_textView_view,
            video_textView_danmaku,
            video_textView_upTime,
            video_textView_like,
            video_textView_coin,
            video_textView_favorite,
            video_textView_share;

    private ViewPage viewPage;
    private Play play;

    private WebView webView;

    //当前webView中播放的选集索引，默认为0
    private int anthologySelectedIndex = 0;
    private WebViewUtils webViewUtils;

    private final static LocalOrderType localOrderType = LocalOrderType.VIDEO;
    private LocalOrdersDatabaseUtils localOrdersDatabaseUtils;
    private DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils;

    //视频在videoPlayList库中的状态
    private boolean isHaveLocalOrder;

    private List<Map.Entry<Integer, Media>> videoEntries = null;
    private List<Map.Entry<Integer, Media>> audioEntries = null;

    private SimpleThreadPool simpleThreadPool;
    private MediaParser mediaParser;
    private ViewParser viewParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        initView();
        initValues();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        video_anthology_cardView = findViewById(R.id.video_anthology_cardView);
        video_recyclerView_singleVideoList = findViewById(R.id.video_recyclerView_singleVideoList);

        video_circleImageView_face = findViewById(R.id.video_circleImageView_face);
        video_circleImageView_face.setOnClickListener(this);

        ImageView video_imageView_back = findViewById(R.id.video_imageView_back);
        video_imageView_back.setOnClickListener(this);

        video_imageView_addFavorite = findViewById(R.id.video_imageView_addFavorite);
        video_imageView_addFavorite.setOnClickListener(this);

        video_recyclerView_singleVideoList = findViewById(R.id.video_recyclerView_singleVideoList);

        video_textView_name = findViewById(R.id.video_textView_name);
        video_textView_title = findViewById(R.id.video_textView_title);
        video_textView_view = findViewById(R.id.video_textView_view);
        video_textView_danmaku = findViewById(R.id.video_textView_danmaku);
        video_textView_upTime = findViewById(R.id.video_textView_upTime);
        video_textView_like = findViewById(R.id.video_textView_like);
        video_textView_coin = findViewById(R.id.video_textView_coin);
        video_textView_favorite = findViewById(R.id.video_textView_favorite);
        video_textView_share = findViewById(R.id.video_textView_share);

        expand_text_view = findViewById(R.id.expand_text_view);

        Button video_button_saveVideo = findViewById(R.id.video_button_saveVideo);
        video_button_saveVideo.setOnClickListener(this);

        Button video_button_saveCover = findViewById(R.id.video_button_saveCover);
        video_button_saveCover.setOnClickListener(this);

        Button video_button_saveFace = findViewById(R.id.video_button_saveFace);
        video_button_saveFace.setOnClickListener(this);

        webView = findViewById(R.id.video_webView);
    }

    /**
     * 初始化数据
     */
    private void initValues() {
        //获取视频bvid
        Intent intent = getIntent();
        String bvid = intent.getStringExtra("bvid");

        Fuck.blue("query-----bvid:" + bvid);

        //获取ViewPage实体类（视频基本信息）
        if (viewParser == null) {
            viewParser = new ViewParser(getApplicationContext());
        }
        viewPage = viewParser.parseView(bvid);

        if (mediaParser == null) {
            mediaParser = new MediaParser(getApplicationContext());
        }

        //获取视频选集信息
        play = mediaParser.parseMedia(bvid, viewPage.anthologyInfoList.get(0).cid, false);

        SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(getApplicationContext(), Tables.LocalOrders);
        localOrdersDatabaseUtils = (LocalOrdersDatabaseUtils) sqLiteHelperFactory.getInstance();

        isHaveLocalOrder = localOrdersDatabaseUtils.queryLocalOrder(String.valueOf(viewPage.bvid), null, localOrderType);

        // 初始化视频信息
        initVideoInfo();

        // 如果为单个视频则不显示选集列表
        if (!(viewPage.anthologyInfoList.size() <= 1)) {
            //设置布局管理器
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为水平方向

            //创建adapter
            AnthologyAdapter anthologyAdapter = new AnthologyAdapter(viewPage, getApplicationContext());
            anthologyAdapter.setOnClickAnthologyListener(new AnthologyAdapter.OnClickAnthologyListener() {
                @Override
                public void onClick(int position, long cid) {
                    anthologySelectedIndex = position;

                    //判断是否有网络
                    boolean isHaveNetwork = InternetUtils.checkNetwork(getApplicationContext());

                    if (!isHaveNetwork) {
                        Toast.makeText(getApplicationContext(), R.string.network_sign, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (webViewUtils == null) {
                        webViewUtils = new WebViewUtils(webView);
                    }

                    //设置webView的链接
                    webViewUtils.setWebViewUrl(viewPage.aid, cid, position);

                    //重置nowPosition
                    anthologySelectedIndex = position;

                    //重置当前play变量
                    play = mediaParser.parseMedia(bvid, cid, false);
                }
            });

            //设置RecyclerView
            video_recyclerView_singleVideoList.setLayoutManager(layoutManager);
            video_recyclerView_singleVideoList.setAdapter(anthologyAdapter);
        } else {
            video_anthology_cardView.setVisibility(View.GONE);
        }
    }

    private void initVideoInfo() {
        Glide.with(getApplicationContext()).load(viewPage.userInfo.faceUrl + ImagePixelSize.FACE.value).into(video_circleImageView_face);
        video_textView_name.setText(viewPage.userInfo.name);
        video_imageView_addFavorite.setImageResource(isHaveLocalOrder ? R.drawable.favorite : R.drawable.no_favorite);
        video_textView_title.setText(viewPage.title);
        video_textView_view.setText(ValueFormat.generateCN(viewPage.videoInfo.view) + "次观看");
        video_textView_danmaku.setText(ValueFormat.generateCN(viewPage.videoInfo.danmaku) + "弹幕");
        video_textView_upTime.setText(ValueFormat.generateTime(viewPage.upTime, true, false, "-"));
        video_textView_like.setText(ValueFormat.generateCN(viewPage.videoInfo.like) + "点赞");
        video_textView_coin.setText(ValueFormat.generateCN(viewPage.videoInfo.coin) + "投币");
        video_textView_favorite.setText(ValueFormat.generateCN(viewPage.videoInfo.favorite) + "收藏");
        video_textView_share.setText(ValueFormat.generateCN(viewPage.videoInfo.share) + "分享");
        expand_text_view.setText(viewPage.desc);

        //设置默认的选集
        if (webViewUtils == null) {
            webViewUtils = new WebViewUtils(webView);
        }
        webViewUtils.setWebViewUrl(viewPage.aid, viewPage.anthologyInfoList.get(0).cid, 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_imageView_back:
                this.finish();
                break;
            case R.id.video_circleImageView_face:
                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(getApplicationContext());

                if (!isHaveNetwork) {
                    Toast.makeText(getApplicationContext(), R.string.network_sign, Toast.LENGTH_SHORT).show();
                    break;
                }

                //跳转到up主界面
                Intent intent = new Intent(this, UserActivity.class);
                intent.putExtra("mid", viewPage.userInfo.mid);
                startActivity(intent);
                break;
            case R.id.video_imageView_addFavorite:
                //根据videoState判断是否保存获取删除该video
                if (isHaveLocalOrder) {
                    //从videoPlayList中删除此video
                    boolean state = localOrdersDatabaseUtils.deleteLocalOrder(String.valueOf(viewPage.bvid), null, localOrderType);

                    if (state) {
                        video_imageView_addFavorite.setImageResource(R.drawable.no_favorite);
                        Toast.makeText(this, R.string.remFavoriteSign, Toast.LENGTH_SHORT).show();
                        isHaveLocalOrder = false;
                    }
                } else {
                    //添加该video至videoPlayList中
                    AddVideoDialog addVideoDialog = new AddVideoDialog(VideoActivity.this);
                    addVideoDialog.setOnAddOrderCallback(new AddVideoDialog.OnAddOrderCallback() {
                        @Override
                        public LocalOrder callBack(LocalVideoFolder localVideoFolder) {
                            LocalOrder localOrder = new LocalOrder();
                            Map<String, Object> map = new HashMap<>();
                            map.put("title", viewPage.title);
                            map.put("cover", viewPage.coverUrl);
                            map.put("duration", viewPage.anthologyInfoList.get(0).duration);
                            localOrder.jsonObject = new JSONObject(map);
                            localOrder.mainId = viewPage.bvid;
                            localOrder.orderType = localOrderType;
                            localOrder.addTime = System.currentTimeMillis();
                            localOrder.folderName = localVideoFolder.folderName;

                            return localOrder;
                        }

                        @Override
                        public void onFavoriteIcon(boolean addState) {
                            if (addState) {
                                video_imageView_addFavorite.setImageResource(R.drawable.favorite);
                                Toast.makeText(VideoActivity.this, R.string.addFavoriteSign, Toast.LENGTH_SHORT).show();
                                isHaveLocalOrder = true;
                            }
                        }
                    });
                    addVideoDialog.show();
                }

                break;
            case R.id.video_button_saveVideo://保存选定画质的视频
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

                // 如果选集数量大于1，就显示选集列表对话框
                if (viewPage.anthologyInfoList.size() > 1) {
                    AnthologyDownloadDialog anthologyDownloadDialog = new AnthologyDownloadDialog(VideoActivity.this, viewPage.anthologyInfoList);
                    anthologyDownloadDialog.setOnDownloadListener(new AnthologyDownloadDialog.OnDownloadListener() {
                        @Override
                        public void onDownload(int qualityId, long cid, int position, String subTitle) {
                            // 保存选定视频
                            Fuck.blue("saveSingleVideo----cid:" + cid + "--qualityIndex:" + qualityId + "--subTitle:" + subTitle);

                            //获取视频选集信息
                            Play playWithDownload = mediaParser.parseMedia(viewPage.bvid, cid, false);

                            anthologySelectedIndex = position;

                            videoEntries = playWithDownload.videoEntries();
                            for (Map.Entry<Integer, Media> entry : videoEntries) {
                                entry.getValue().isDownloaded = downloadRecordsDatabaseUtils
                                        .queryVideoDownloadState(String.valueOf(viewPage.anthologyInfoList.get(anthologySelectedIndex).cid + entry.getKey()));
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
                            Toast.makeText(VideoActivity.this, "Sorry~该功能暂未进行开发，请谅解＞︿＜", Toast.LENGTH_SHORT).show();
                        }
                    });
                    anthologyDownloadDialog.show();
                } else {
                    if (videoEntries == null) {
                        videoEntries = play.videoEntries();
                        for (Map.Entry<Integer, Media> entry : videoEntries) {
                            entry.getValue().isDownloaded = downloadRecordsDatabaseUtils
                                    .queryVideoDownloadState(String.valueOf(viewPage.anthologyInfoList.get(anthologySelectedIndex).cid + entry.getKey()));
                        }

                        audioEntries = play.audioEntries();
                    }

                    //创建清晰度选择dialog
                    SingleVideoQualityDialog singleVideoQualityDialog = new SingleVideoQualityDialog(VideoActivity.this, videoEntries);
                    singleVideoQualityDialog.setOnQualityClickListener(new SingleVideoQualityDialog.OnQualityClickListener() {
                        @Override
                        public void onClickListener(Map.Entry<Integer, Media> mediaEntry) {
                            singleVideoQualityDialog.dismiss();
                            saveSingleVideo(mediaEntry);
                        }
                    });

                    //显示选择清晰度dialog
                    singleVideoQualityDialog.show();
                }

                break;
            case R.id.video_button_saveCover:
                //获取权限
                FileUtils.verifyPermissions(this);

                //获取视频封面地址
                String coverUrl = viewPage.coverUrl;

                //进行保存
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean coverSaveState = ResourceUtils.savePicture(getApplicationContext(), coverUrl);

                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), coverSaveState ? "保存成功" : "保存失败", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }).start();

                break;
            case R.id.video_button_saveFace:
                //获取权限
                FileUtils.verifyPermissions(this);

                //获取链接
                String faceUrl = viewPage.userInfo.faceUrl;

                //进行保存
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        boolean faceSaveState = ResourceUtils.savePicture(getApplicationContext(), faceUrl);

                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), faceSaveState ? "保存成功" : "保存失败", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }).start();


                break;
            default:
                break;
        }
    }

    private void saveSingleVideo(Map.Entry<Integer, Media> mediaEntry) {
        //获取权限
        FileUtils.verifyPermissions(VideoActivity.this);

        //获取视频路径
        String videoUrlBase = mediaEntry.getValue().baseUrl;

        //获取音频路径,默认只获取第一个
        String audioUrlBase = audioEntries.get(0).getValue().baseUrl;

        Toast.makeText(getApplicationContext(), "已加入缓存队列中", Toast.LENGTH_SHORT).show();

        // 添加至downloadedRecordsForVideo
        addToDownloadedRecordsForVideo();

        // 添加至DownloadDetailsForMedia
        String fileName = addToDownloadDetailMedia(mediaEntry, videoUrlBase, audioUrlBase);

        if (simpleThreadPool == null) {
            simpleThreadPool = new SimpleThreadPool(SimpleThreadPool.DownloadTaskNum, SimpleThreadPool.DownloadTask);
        }

        SimpleDownloadThread simpleDownloadThread = new SimpleDownloadThread(getApplicationContext(), videoUrlBase, audioUrlBase, fileName);
        simpleThreadPool.submit(new FutureTask<>(simpleDownloadThread));
    }

    @NotNull
    private String addToDownloadDetailMedia(Map.Entry<Integer, Media> mediaEntry, String videoUrlBase, String audioUrlBase) {
        DownloadedDetailMedia downloadedDetailMedia = new DownloadedDetailMedia();

        AnthologyInfo anthologyInfo = viewPage.anthologyInfoList.get(anthologySelectedIndex);
        String fileName = viewPage.bvid + "-" + anthologyInfo.part + "-" + mediaEntry.getValue().quality.split(" ")[1];

        downloadedDetailMedia.fileName = fileName;
        downloadedDetailMedia.cover = viewPage.coverUrl;
        downloadedDetailMedia.title = viewPage.anthologyInfoList.get(anthologySelectedIndex).part;
        downloadedDetailMedia.videoUrl = videoUrlBase;
        downloadedDetailMedia.audioUrl = audioUrlBase;
        downloadedDetailMedia.qualityId = mediaEntry.getKey();

        // 获取视频和音频总大小
        downloadedDetailMedia.size = ResourceUtils.getResourcesSize(videoUrlBase) + ResourceUtils.getResourcesSize(audioUrlBase);
        downloadedDetailMedia.mainId = viewPage.bvid;
        downloadedDetailMedia.subId = viewPage.anthologyInfoList.get(anthologySelectedIndex).cid;
        downloadedDetailMedia.resourceMark = downloadedDetailMedia.subId + "-" + downloadedDetailMedia.qualityId;
        downloadedDetailMedia.isVideo = true;

        downloadRecordsDatabaseUtils.addMediaDetail(downloadedDetailMedia);
        return fileName;
    }

    private void addToDownloadedRecordsForVideo() {
        DownloadedRecordsForVideo downloadedRecordsForVideo = new DownloadedRecordsForVideo();
        downloadedRecordsForVideo.title = viewPage.title;
        downloadedRecordsForVideo.upName = viewPage.userInfo.name;
        downloadedRecordsForVideo.mainId = viewPage.bvid;
        downloadedRecordsForVideo.cover = viewPage.coverUrl;

        downloadRecordsDatabaseUtils.addVideo(downloadedRecordsForVideo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.resumeTimers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.pauseTimers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //清除网页访问留下的缓存
        //由于内核缓存是全局的因此这个方法不仅仅针对webView而是针对整个应用程序.
        webView.clearCache(true);

        //清除当前webview访问的历史记录
        //只会webview访问历史记录里的所有记录除了当前访问记录
        webView.clearHistory();

        //这个api仅仅清除自动完成填充的表单数据，并不会清除WebView存储到本地的数据
        webView.clearFormData();

        webView.destroy();

        if (localOrdersDatabaseUtils != null) {
            localOrdersDatabaseUtils.close();
        }

        if (downloadRecordsDatabaseUtils != null) {
            downloadRecordsDatabaseUtils.close();
        }
    }

    /**
     * 权限回调
     *
     * @param requestCode  请求码
     * @param permissions  文件读写权限
     * @param grantResults 授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1024) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(getApplicationContext(), "权限申请成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "权限申请失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}