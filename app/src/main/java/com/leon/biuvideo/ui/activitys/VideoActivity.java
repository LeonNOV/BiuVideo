package com.leon.biuvideo.ui.activitys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.*;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.AnthologyAdapter;
import com.leon.biuvideo.beans.videoBean.play.Play;
import com.leon.biuvideo.beans.videoBean.view.SingleVideoInfo;
import com.leon.biuvideo.beans.videoBean.view.ViewPage;
import com.leon.biuvideo.utils.GeneralNotification;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.MediaUtils;
import com.leon.biuvideo.utils.Paths;
import com.leon.biuvideo.utils.WebpSizes;
import com.leon.biuvideo.utils.mediaParseUtils.MediaParseUtils;
import com.leon.biuvideo.utils.mediaParseUtils.ViewParseUtils;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.text.SimpleDateFormat;
import java.util.*;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, AnthologyAdapter.OnItemClickListener {
    private CircleImageView video_circleImageView_face;
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
    boolean saveState;

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
        Map<String, Object> map = new HashMap<>();
        map.put("bvid", bvid);
        String response_viewPage = HttpUtils.GETByParam(Paths.view, map);
        viewPage = ViewParseUtils.parseView(response_viewPage);

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

        //设置标题
        video_textView_title.setText(viewPage.title);

        //设置观看数
        video_textView_view.setText(viewPage.videoInfo.view + "次观看");

        //设置弹幕数
        video_textView_danmaku.setText(viewPage.videoInfo.danmaku + "弹幕");

        //设置上传时间(UTC+8)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

        //秒转毫秒
        video_textView_upTime.setText(sdf.format(new Date(viewPage.upTime * 1000)));

        //设置点赞数
        video_textView_like.setText(viewPage.videoInfo.like + "点赞");

        //设置投币数
        video_textView_coin.setText(viewPage.videoInfo.coin + "投币");

        //设置收藏数
        video_textView_favorite.setText(viewPage.videoInfo.favorite + "收藏");

        //设置分享数
        video_textView_share.setText(viewPage.videoInfo.share + "分享");

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
            case R.id.video_button_saveVideo://保存选定画质的视频

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

                //获取音频路径,默认只获取品质最高的
                String audioUrlBase = play.audios.get(0).baseUrl;

                //获取保存路径
                String videoPath = MediaUtils.folderState(MediaUtils.ResourcesFolder.VIDEOS);

                //文件名组成,以视频bvid为基本名称
                String fileName = MediaUtils.generateFileName(viewPage.bvid);

                Toast.makeText(this, "已加入缓存队列中", Toast.LENGTH_SHORT).show();

                //262144
                //1048576
                //51809695
                //获取视频线程
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //获取对应nowPosition的选集信息
                        SingleVideoInfo nowSingleVideoInfo = viewPage.singleVideoInfoList.get(nowPosition);

                        //获取视频
                        saveState = MediaUtils.ComposeTrack(videoUrlBase, audioUrlBase, videoPath + "/" + fileName + ".mp4");

                        //创建推送通知
                        GeneralNotification notification = new GeneralNotification(getApplicationContext(), getSystemService(Context.NOTIFICATION_SERVICE), viewPage.bvid + "", "SaveVideo", (int) nowSingleVideoInfo.cid);

                        String title = saveState ? "视频已缓存完成" : "视频缓存失败";
                        notification.setNotificationOnSDK26(title, viewPage.title + "\t" + nowSingleVideoInfo.part, R.drawable.ic_menu_camera);
                    }
                }).start();
                break;
            case R.id.video_button_saveCover:
                Toast.makeText(getApplicationContext(), "点击了保存封面", Toast.LENGTH_SHORT).show();
                break;
            case R.id.video_button_saveFace:
                Toast.makeText(getApplicationContext(), "点击了保存UP头像", Toast.LENGTH_SHORT).show();
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
}