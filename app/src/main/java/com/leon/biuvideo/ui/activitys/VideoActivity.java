package com.leon.biuvideo.ui.activitys;

import android.content.Context;
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
import com.leon.biuvideo.beans.upMasterBean.VideoPlayList;
import com.leon.biuvideo.beans.videoBean.play.Play;
import com.leon.biuvideo.beans.videoBean.view.SingleVideoInfo;
import com.leon.biuvideo.beans.videoBean.view.ViewPage;
import com.leon.biuvideo.ui.dialogs.SingleVideoQualityDialog;
import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.GeneralNotification;
import com.leon.biuvideo.utils.ImagePixelSize;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.MediaUtils;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.WebViewUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.utils.dataBaseUtils.Tables;
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
    private Button
            video_button_saveVideo,
            video_button_saveCover,
            video_button_saveFace;

    private ViewPage viewPage;
    public static Play play;

    private WebView webView;

    //选择的画质dialog
    private SingleVideoQualityDialog singleVideoQualityDialog;

    //当前webView中播放的选集索引，默认为0
    private int singleVideoSelectedIndex = 0;

    //视频保存状态
    private boolean saveState;

    private VideoListDatabaseUtils videoListDatabaseUtils;

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

        video_button_saveVideo = findViewById(R.id.video_button_saveVideo);
        video_button_saveVideo.setOnClickListener(this);

        video_button_saveCover = findViewById(R.id.video_button_saveCover);
        video_button_saveCover.setOnClickListener(this);

        video_button_saveFace = findViewById(R.id.video_button_saveFace);
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
                    return;
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
                    return;
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

                        //获取视频线程
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //获取对应选集的信息
                                SingleVideoInfo nowSingleVideoInfo = viewPage.singleVideoInfoList.get(singleVideoSelectedIndex);

                                //缓存视频
                                saveState = MediaUtils.saveVideo(getApplicationContext(), videoUrlBase, audioUrlBase, FileUtils.generateFileName(viewPage.bvid));

                                //创建推送通知
                                GeneralNotification notification = new GeneralNotification(getApplicationContext(), getSystemService(Context.NOTIFICATION_SERVICE), viewPage.bvid + "", "SaveVideo", (int) nowSingleVideoInfo.cid);

                                String title = saveState ? "视频已缓存完成" : "视频缓存失败";
                                notification.setNotificationOnSDK26(title, viewPage.title + "\t" + nowSingleVideoInfo.part, R.drawable.ic_menu_camera);
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
                        boolean coverSaveState = MediaUtils.savePicture(getApplicationContext(), coverUrl);

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

                        boolean faceSaveState = MediaUtils.savePicture(getApplicationContext(), faceUrl);

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

        videoListDatabaseUtils.close();
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