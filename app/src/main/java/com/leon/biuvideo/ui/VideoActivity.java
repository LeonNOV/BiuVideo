package com.leon.biuvideo.ui;

import android.view.*;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.AnthologyAdapter;
import com.leon.biuvideo.beans.play.Play;
import com.leon.biuvideo.beans.view.ViewPage;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.Paths;
import com.leon.biuvideo.utils.WebpSizes;
import com.leon.biuvideo.utils.parseUtils.MediaParseUtils;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.text.SimpleDateFormat;
import java.util.*;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private CircleImageView video_circleImageView_face;
    private ListView video_listView_singleVideoList;
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
        video_listView_singleVideoList = findViewById(R.id.video_listView_singleVideoList);

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
        //获取ViewPage实体类（视频基本信息）
        Intent intent = getIntent();
        viewPage = (ViewPage) intent.getExtras().getSerializable("viewPage");

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

        String response = HttpUtils.GETByParam(Paths.playUrl, param);
        play = MediaParseUtils.parseMedia(response);

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

        //设置上传时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        video_textView_upTime.setText(sdf.format(new Date(viewPage.upTime)));

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
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        //创建adapter
        AnthologyAdapter anthologyAdapter = new AnthologyAdapter(getApplicationContext(), viewPage);

        //设置ListView
        video_listView_singleVideoList.setAdapter(anthologyAdapter);

        //获取视频清晰度
        List<String> accept_description = play.accept_description;

        //设置spinner适配器
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, accept_description);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        video_spinner_quality.setAdapter(arrayAdapter);
        video_spinner_quality.setOnItemSelectedListener(this);

        //配置webView地址
        //默认第一个视频为singleVideoInfoList中的第一个，page为选集列表中的第一个视频的索引（从1开始）
        String aid = "aid=" + viewPage.aid;
        String cid = "cid=" + viewPage.singleVideoInfoList.get(0).cid;
        String videoPath = Paths.videoBaeUrl + aid + "&" + cid + "&" + "page=1";
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
            case R.id.video_button_saveVideo:
                Toast.makeText(getApplicationContext(), "选择的画质为" + play.accept_description.get(qualityIndex), Toast.LENGTH_SHORT).show();
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