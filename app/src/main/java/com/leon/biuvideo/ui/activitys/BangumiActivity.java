package com.leon.biuvideo.ui.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.searchBean.bangumi.Bangumi;
import com.leon.biuvideo.beans.searchBean.bangumi.BangumiState;
import com.leon.biuvideo.ui.dialogs.BangumiDetailDialog;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.BangumiStateParse;

public class BangumiActivity extends AppCompatActivity implements View.OnClickListener {
    private Bangumi bangumi;
    private BangumiState bangumiState;
    private int selectAnthologyIndex;

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
        selectAnthologyIndex = extras.getInt("selectAnthologyIndex", 0);

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

                break;
            case R.id.bangumi_imageView_favorite:

                break;
            default:
                break;
        }
    }
}