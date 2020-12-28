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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.AnthologyAdapter;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedDetailMedia;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedRecordsForVideo;
import com.leon.biuvideo.beans.upMasterBean.VideoPlayList;
import com.leon.biuvideo.beans.videoBean.play.Play;
import com.leon.biuvideo.beans.videoBean.view.SingleVideoInfo;
import com.leon.biuvideo.beans.videoBean.view.ViewPage;
import com.leon.biuvideo.ui.dialogs.SingleVideoQualityDialog;
import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;
import com.leon.biuvideo.utils.downloadUtils.MediaUtils;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.downloadUtils.ResourceUtils;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.WebViewUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.values.Tables;
import com.leon.biuvideo.utils.dataBaseUtils.VideoListDatabaseUtils;
import com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils.MediaParseUtils;
import com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils.ViewParseUtils;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.text.SimpleDateFormat;
import java.util.*;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 视频观看activity
 */
public class VideoActivity extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView video_circleImageView_face;
    private ImageView video_imageView_back, video_imageView_addFavorite;
    private RecyclerView video_recyclerView_singleVideoList;
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
    public static Play play;

    private WebView webView;

    //选择的画质dialog
    private SingleVideoQualityDialog singleVideoQualityDialog;

    //当前webView中播放的选集索引，默认为0
    private int singleVideoSelectedIndex = 0;

    private VideoListDatabaseUtils videoListDatabaseUtils;
    private DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils;

    //视频在videoPlayList库中的状态
    private boolean videoState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        initView();
        initValues();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        video_circleImageView_face = findViewById(R.id.video_circleImageView_face);
        video_circleImageView_face.setOnClickListener(this);

        video_imageView_back = findViewById(R.id.video_imageView_back);
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

        webView = findViewById(R.id.webView);
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
        viewPage = ViewParseUtils.parseView(bvid);

        //获取视频选集信息
        play = MediaParseUtils.parseMedia(viewPage.bvid, viewPage.aid, viewPage.singleVideoInfoList.get(0).cid);

        //设置显示头像
        Glide.with(getApplicationContext()).load(viewPage.userInfo.faceUrl + ImagePixelSize.FACE.value).into(video_circleImageView_face);

        //设置up主昵称
        video_textView_name.setText(viewPage.userInfo.name);

        SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(getApplicationContext(), Tables.VideoPlayList);
        videoListDatabaseUtils = (VideoListDatabaseUtils) sqLiteHelperFactory.getInstance();

        //查询是否已添加至播放列表
        videoState = videoListDatabaseUtils.queryFavoriteVideo(viewPage.bvid);
        if (videoState) {
            video_imageView_addFavorite.setImageResource(R.drawable.favorite);
        } else {
            video_imageView_addFavorite.setImageResource(R.drawable.no_favorite);
        }

        //设置标题
        video_textView_title.setText(viewPage.title);

        //设置观看数
        String view = ValueFormat.generateCN(viewPage.videoInfo.view) + "次观看";
        video_textView_view.setText(view);

        //设置弹幕数
        String danmaku = ValueFormat.generateCN(viewPage.videoInfo.danmaku) + "弹幕";
        video_textView_danmaku.setText(danmaku);

        //设置上传时间(UTC+8)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

        //秒转毫秒
        video_textView_upTime.setText(sdf.format(new Date(viewPage.upTime * 1000)));

        //设置点赞数
        String like = ValueFormat.generateCN(viewPage.videoInfo.like) + "点赞";
        video_textView_like.setText(like);

        //设置投币数
        String coin = ValueFormat.generateCN(viewPage.videoInfo.coin) + "投币";
        video_textView_coin.setText(coin);

        //设置收藏数
        String favorite = ValueFormat.generateCN(viewPage.videoInfo.favorite) + "收藏";
        video_textView_favorite.setText(favorite);

        //设置分享数
        String share = ValueFormat.generateCN(viewPage.videoInfo.share) + "分享";
        video_textView_share.setText(share);

        //设置视频说明
        expand_text_view.setText(viewPage.desc);

        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为水平方向

        //创建adapter
        AnthologyAdapter anthologyAdapter = new AnthologyAdapter(viewPage, getApplicationContext(), webView);
        anthologyAdapter.setOnClickAnthologyListener(new AnthologyAdapter.OnClickAnthologyListener() {
            @Override
            public void onClick(int position) {
                singleVideoSelectedIndex = position;
            }
        });

        //设置RecyclerView
        video_recyclerView_singleVideoList.setLayoutManager(layoutManager);
        video_recyclerView_singleVideoList.setAdapter(anthologyAdapter);

        //设置默认的选集
        new WebViewUtils(webView).setWebViewUrl(viewPage.aid, viewPage.singleVideoInfoList.get(0).cid, 0);

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
                if (videoState) {
                    //从videoPlayList中删除此video
                    boolean state = videoListDatabaseUtils.removeVideo(viewPage.bvid);

                    Toast.makeText(this, state ? "已从播放列表中删除" : "删除失败~~~", Toast.LENGTH_SHORT).show();

                    if (state) {
                        video_imageView_addFavorite.setImageResource(R.drawable.no_favorite);
                    }
                } else {
                    //添加该video至videoPlayList中
                    VideoPlayList videoPlayList = new VideoPlayList();

                    videoPlayList.bvid = viewPage.bvid;
                    videoPlayList.uname = viewPage.userInfo.name;
                    videoPlayList.title = viewPage.title;
                    videoPlayList.coverUrl = viewPage.coverUrl;
                    videoPlayList.length = viewPage.singleVideoInfoList.get(0).duration;//只获取第一个视频的长度
                    videoPlayList.play = viewPage.videoInfo.view;
                    videoPlayList.danmaku = viewPage.videoInfo.danmaku;

                    boolean state = videoListDatabaseUtils.addFavoriteVideo(videoPlayList);

                    Toast.makeText(getApplicationContext(), state ? "已成功添加到播放列表中" : "添加失败~~~", Toast.LENGTH_SHORT).show();

                    if (state) {
                        video_imageView_addFavorite.setImageResource(R.drawable.favorite);
                    }
                }

                break;
            case R.id.video_button_saveVideo://保存选定画质的视频
                //判断是否有网络
                boolean isHaveNetworkSaveVideo = InternetUtils.checkNetwork(getApplicationContext());

                if (!isHaveNetworkSaveVideo) {
                    Toast.makeText(getApplicationContext(), R.string.network_sign, Toast.LENGTH_SHORT).show();
                    break;
                }
                
                //创建清晰度选择dialog
                singleVideoQualityDialog = new SingleVideoQualityDialog(VideoActivity.this, play.videoQualitys);
                SingleVideoQualityDialog.onQualityItemListener = new SingleVideoQualityDialog.OnQualityItemListener() {
                    @Override
                    public void onItemClickListener(int position) {
                        //获取权限
                        FileUtils.verifyPermissions(VideoActivity.this);

                        //获取视频路径
                        String videoUrlBase = play.videos.get(position).baseUrl;

                        //获取音频路径,默认只获取第一个
                        String audioUrlBase = play.audios.get(0).baseUrl;

                        Toast.makeText(getApplicationContext(), "已加入缓存队列中", Toast.LENGTH_SHORT).show();

                        //隐藏dialog
                        singleVideoQualityDialog.dismiss();

                        // 添加至
                        DownloadedRecordsForVideo downloadedRecordsForVideo = new DownloadedRecordsForVideo();
                        downloadedRecordsForVideo.title = viewPage.title;
                        downloadedRecordsForVideo.upName = viewPage.userInfo.name;
                        downloadedRecordsForVideo.mainId = viewPage.bvid;
                        downloadedRecordsForVideo.cover = viewPage.coverUrl;

                        // 添加至DownloadDetailsForMedia
                        DownloadedDetailMedia downloadedDetailMedia = new DownloadedDetailMedia();

                        SingleVideoInfo singleVideoInfo = viewPage.singleVideoInfoList.get(singleVideoSelectedIndex);
                        String fileName = viewPage.bvid + "-" + singleVideoInfo.part + "-" + play.videoQualitys.get(position).split(" ")[1];

                        downloadedDetailMedia.fileName = fileName;
                        downloadedDetailMedia.cover = viewPage.coverUrl;
                        downloadedDetailMedia.title = viewPage.singleVideoInfoList.get(singleVideoSelectedIndex).part;
                        downloadedDetailMedia.videoUrl = videoUrlBase;
                        downloadedDetailMedia.audioUrl = audioUrlBase;

                        // 获取视频和音频总大小
                        downloadedDetailMedia.size = ResourceUtils.getResourcesSize(videoUrlBase) + ResourceUtils.getResourcesSize(audioUrlBase);
                        downloadedDetailMedia.mainId = viewPage.bvid;
                        downloadedDetailMedia.subId = viewPage.singleVideoInfoList.get(singleVideoSelectedIndex).cid;
                        downloadedDetailMedia.isVideo = true;

                        if (downloadRecordsDatabaseUtils == null) {
                            SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(getApplicationContext(), Tables.DownloadDetailsForVideo);
                            downloadRecordsDatabaseUtils = (DownloadRecordsDatabaseUtils) sqLiteHelperFactory.getInstance();
                        }

                        downloadRecordsDatabaseUtils.addVideo(downloadedRecordsForVideo);
                        downloadRecordsDatabaseUtils.addSubVideo(downloadedDetailMedia);

                        //获取视频线程
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MediaUtils mediaUtils = new MediaUtils(getApplicationContext());

                                //缓存视频
                                mediaUtils.saveVideo(videoUrlBase, audioUrlBase, fileName);
                            }
                        }).start();
                    }
                };

                //显示选择清晰度dialog
                singleVideoQualityDialog.show();

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

        if (videoListDatabaseUtils != null) {
            videoListDatabaseUtils.close();
        }

        if (downloadRecordsDatabaseUtils != null) {
            downloadRecordsDatabaseUtils.close();
        }
    }



    /**
     * 权限回调
     *
     * @param requestCode   请求码
     * @param permissions   文件读写权限
     * @param grantResults  授权结果
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