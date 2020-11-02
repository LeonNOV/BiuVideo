package com.leon.biuvideo.ui.activitys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.view.*;
import android.webkit.WebSettings;
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
import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.GeneralNotification;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.MediaUtils;
import com.leon.biuvideo.utils.Paths;
import com.leon.biuvideo.utils.VideoListDatabaseUtils;
import com.leon.biuvideo.utils.WebpSizes;
import com.leon.biuvideo.utils.mediaParseUtils.MediaParseUtils;
import com.leon.biuvideo.utils.mediaParseUtils.ViewParseUtils;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.text.SimpleDateFormat;
import java.util.*;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 视频观看activity
 */
public class VideoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, AnthologyAdapter.OnItemClickListener {
    private CircleImageView video_circleImageView_face;
    private ImageView video_imageView_addFavorite;
    private RecyclerView video_recyclerView_singleVideoList;
    private Spinner video_spinner_quality;
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
    private Play play;

    private WebView webView;

    //选择的画质索引
    private int qualityIndex;

    //当前webView中播放的选集索引，默认为0
    private int nowPosition = 0;

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

        video_imageView_addFavorite = findViewById(R.id.video_imageView_addFavorite);
        video_imageView_addFavorite.setOnClickListener(this);

        video_recyclerView_singleVideoList = findViewById(R.id.video_recyclerView_singleVideoList);

        video_spinner_quality = findViewById(R.id.video_spinner_quality);

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

        //获取ViewPage实体类（视频基本信息）
        viewPage = ViewParseUtils.parseView(bvid);

        //获取视频选集信息
        Map<String, Object> param = new HashMap<>();
        param.put("avid", viewPage.aid);
        param.put("bvid", viewPage.bvid);
        param.put("cid", viewPage.singleVideoInfoList.get(0).cid);
        param.put("qn", 0);
        param.put("otype", "json");
        param.put("fourk", 1);
        param.put("fnver", 0);
        param.put("fnval", 80);

        String response_media = HttpUtils.GETByParam(Paths.playUrl, param);
        play = MediaParseUtils.parseMedia(response_media);

        //设置显示头像
        Glide.with(getApplicationContext()).load(viewPage.upInfo.faceUrl + WebpSizes.face).into(video_circleImageView_face);

        //设置up主昵称
        video_textView_name.setText(viewPage.upInfo.name);

        videoListDatabaseUtils = new VideoListDatabaseUtils(getApplicationContext());

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
        String view = viewPage.videoInfo.view + "次观看";
        video_textView_view.setText(view);

        //设置弹幕数
        String danmaku = viewPage.videoInfo.danmaku + "弹幕";
        video_textView_danmaku.setText(danmaku);

        //设置上传时间(UTC+8)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

        //秒转毫秒
        video_textView_upTime.setText(sdf.format(new Date(viewPage.upTime * 1000)));

        //设置点赞数
        String like = viewPage.videoInfo.like + "点赞";
        video_textView_like.setText(like);

        //设置投币数
        String coin = viewPage.videoInfo.coin + "投币";
        video_textView_coin.setText(coin);

        //设置收藏数
        String favorite = viewPage.videoInfo.favorite + "收藏";
        video_textView_favorite.setText(favorite);

        //设置分享数
        String share = viewPage.videoInfo.share + "分享";
        video_textView_share.setText(share);

        //设置视频说明
        expand_text_view.setText(viewPage.desc);

        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为水平方向

        //创建adapter
        AnthologyAdapter anthologyAdapter = new AnthologyAdapter(getApplicationContext(), viewPage);

        //设置RecyclerView
        video_recyclerView_singleVideoList.setLayoutManager(layoutManager);
        video_recyclerView_singleVideoList.setAdapter(anthologyAdapter);

        //设置RecyclerView的监听事件
        anthologyAdapter.setOnItemClickListener(this);

        //获取视频清晰度
        List<String> accept_description = play.accept_description;

        //设置spinner适配器
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, accept_description);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        video_spinner_quality.setAdapter(arrayAdapter);
        video_spinner_quality.setOnItemSelectedListener(this);

        //设置默认的选集
        setWebViewUrl(viewPage.singleVideoInfoList.get(0).cid, 0);
    }

    /**
     * 选集列表监听事件
     *
     * @param position  选集索引
     */
    @Override
    public void onImageViewClicked(int position) {
        //判断当前观看的视频cid是否和选择的一样
        if (nowPosition != position) {
            //设置webView的链接
            setWebViewUrl(viewPage.singleVideoInfoList.get(position).cid, position);

            //重置nowPosition
            nowPosition = position;
        } else {
            Toast.makeText(VideoActivity.this, "选择的视频已经在播放了~~", Toast.LENGTH_SHORT).show();
        }
    }

    //设置webView的链接
    private void setWebViewUrl(long cid, int pageIndex) {
        //配置webView地址
        String parameter_aid = "aid=" + viewPage.aid;
        String parameter_cid = "cid=" + cid;

        //默认第一个视频为singleVideoInfoList中的第一个，page为选集列表中的第一个视频的索引（从1开始,所以要进行加1）
        String videoPath = Paths.videoBaeUrl + parameter_aid + "&" + parameter_cid + "&" + "page=" + pageIndex + 1;
        configWebView(videoPath);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        qualityIndex = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * 配置网页控件
     * @param videoUrl  设置网页地址
     */
    @SuppressLint("SetJavaScriptEnabled")//忽略SetJavaScriptEnabled的警告
    private void configWebView (String videoUrl) {
        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setAllowFileAccess(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);

        webView.loadUrl(videoUrl);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_circleImageView_face:
                //跳转到up主界面
                Intent intent = new Intent(this, UpMasterActivity.class);
                intent.putExtra("mid", viewPage.upInfo.mid);
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
                    videoPlayList.uname = viewPage.upInfo.name;
                    videoPlayList.desc = viewPage.desc;
                    videoPlayList.coverUrl = viewPage.coverUrl;
                    videoPlayList.length = viewPage.singleVideoInfoList.get(0).duration;//只获取第一个视频的长度
                    videoPlayList.play = viewPage.videoInfo.view;
                    videoPlayList.danmaku = viewPage.videoInfo.danmaku;


                    boolean state = videoListDatabaseUtils.addFavoriteVideo(videoPlayList);

                    Toast.makeText(this, state ? "已成功添加到播放列表中" : "添加失败~~~", Toast.LENGTH_SHORT).show();

                    if (state) {
                        video_imageView_addFavorite.setImageResource(R.drawable.favorite);
                    }
                }

                break;
            case R.id.video_button_saveVideo://保存选定画质的视频
                //获取权限
                FileUtils.verifyPermissions(this);

                //判断当前的选集个数是否大于1
                if (viewPage.singleVideoInfoList.size() > 1) {
                    //获取当前选集的信息
                    Map<String, Object> param = new HashMap<>();
                    param.put("avid", viewPage.aid);
                    param.put("bvid", viewPage.bvid);
                    param.put("cid", viewPage.singleVideoInfoList.get(nowPosition).cid);
                    param.put("qn", 0);
                    param.put("otype", "json");
                    param.put("fourk", 1);
                    param.put("fnver", 0);
                    param.put("fnval", 80);

                    String response_media = HttpUtils.GETByParam(Paths.playUrl, param);
                    play = MediaParseUtils.parseMedia(response_media);
                }

                //获取视频路径
                String videoUrlBase = play.videos.get(qualityIndex).baseUrl;

                //获取音频路径,默认只获取第一个
                String audioUrlBase = play.audios.get(0).baseUrl;

                Toast.makeText(this, "已加入缓存队列中", Toast.LENGTH_SHORT).show();

                //获取视频线程
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //获取对应nowPosition的选集信息
                        SingleVideoInfo nowSingleVideoInfo = viewPage.singleVideoInfoList.get(nowPosition);

                        //获取视频
                        saveState = MediaUtils.saveVideo(getApplicationContext(), videoUrlBase, audioUrlBase, FileUtils.generateFileName(viewPage.bvid));

                        //创建推送通知
                        GeneralNotification notification = new GeneralNotification(getApplicationContext(), getSystemService(Context.NOTIFICATION_SERVICE), viewPage.bvid + "", "SaveVideo", (int) nowSingleVideoInfo.cid);

                        String title = saveState ? "视频已缓存完成" : "视频缓存失败";
                        notification.setNotificationOnSDK26(title, viewPage.title + "\t" + nowSingleVideoInfo.part, R.drawable.ic_menu_camera);
                    }
                }).start();
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
                String faceUrl = viewPage.upInfo.faceUrl;

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
            default:break;
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

        //该Activity销毁时，清除缓存
        webView.clearCache(true);
        webView.destroy();
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