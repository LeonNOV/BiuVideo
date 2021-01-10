package com.leon.biuvideo.ui.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.searchBean.bangumi.Bangumi;
import com.leon.biuvideo.ui.fragments.bangumiFragments.BangumiInfoFragment;

public class BangumiActivity extends AppCompatActivity implements View.OnClickListener {
    private Bangumi bangumi;
    private int selectAnthologyIndex;

    private TextView bangumi_textView_jumpToOriginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bangumi);

        init();
        initView();
        initValue();
    }

    private void init() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        // 获取初始数据
        bangumi = (Bangumi) extras.getSerializable("bangumi");
        selectAnthologyIndex = extras.getInt("selectAnthologyIndex", 0);
    }

    private void initView() {
        bangumi_textView_jumpToOriginal = findViewById(R.id.bangumi_textView_jumpToOriginal);
    }

    private void initValue() {
        bangumi_textView_jumpToOriginal.setOnClickListener(this);

        BangumiInfoFragment bangumiInfoFragment = new BangumiInfoFragment(bangumi, selectAnthologyIndex);
        getSupportFragmentManager().beginTransaction().add(R.id.bangumi_fragment, bangumiInfoFragment).commit();
    }

    @Override
    public void onClick(View v) {
        //跳转到源网站
        Intent intentOriginUrl = new Intent();
        intentOriginUrl.setAction("android.intent.action.VIEW");
        Uri uri = Uri.parse(bangumi.eps.get(selectAnthologyIndex).url);
        intentOriginUrl.setData(uri);
        startActivity(intentOriginUrl);
    }
}