package com.leon.biuvideo.ui.fragments.bangumiFragments;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.Navigation;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.searchBean.bangumi.Bangumi;
import com.leon.biuvideo.beans.searchBean.bangumi.BangumiState;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.BangumiStateParse;

public class BangumiInfoFragment extends BaseFragment implements View.OnClickListener {
    private Bangumi bangumi;
    private BangumiState bangumiState;
    private int selectAnthologyIndex;

    public BangumiInfoFragment() {
    }

    public BangumiInfoFragment(Bangumi bangumi, int selectAnthologyIndex) {
        this.bangumi = bangumi;
        this.selectAnthologyIndex = selectAnthologyIndex;
    }

    @Override
    public int setLayout() {
        return R.layout.fragment_bangumi_info;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
//        getFragmentArguments();
        getBangumiState();

        bindingUtils.setText(R.id.fragment_bangumi_info_textView_title, bangumi.title)
                .setText(R.id.fragment_bangumi_info_textView_bangumiState, bangumi.eps.size() == bangumi.epSize ? "已完结" : "连载中")
                .setText(R.id.fragment_bangumi_info_textView_epSize, "共" + bangumi.epSize + "话")
                .setText(R.id.fragment_bangumi_info_textView_score, bangumi.score + "分")
                .setText(R.id.fragment_bangumi_info_textView_play, ValueFormat.generateCN(bangumiState.views) + "播放")
                .setText(R.id.fragment_bangumi_info_textView_follow, ValueFormat.generateCN(bangumiState.seriesFollow) + "系列追番")
                .setText(R.id.fragment_bangumi_info_textView_like, ValueFormat.generateCN(bangumiState.likes) + "点赞")
                .setText(R.id.fragment_bangumi_info_textView_coin, ValueFormat.generateCN(bangumiState.coins) + "投币")
                .setText(R.id.fragment_bangumi_info_textView_favorite, ValueFormat.generateCN(bangumiState.follow) + "收藏")
                .setOnClickListener(R.id.fragment_bangumi_info_textView_toDetail, this)
                .setOnClickListener(R.id.fragment_bangumi_info_imageView_download, this)
                .setOnClickListener(R.id.fragment_bangumi_info_imageView_favorite, this);
    }

    private void getBangumiState() {
        if (bangumi != null) {
            BangumiStateParse bangumiStateParse = new BangumiStateParse();
            bangumiState = bangumiStateParse.bangumiStateParse(bangumi.seasonId);
        }
    }

    private void getFragmentArguments() {
        Bundle arguments = getArguments();

        // 获取初始数据
        bangumi = (Bangumi) arguments.getSerializable("bangumi");
        selectAnthologyIndex = arguments.getInt("selectAnthologyIndex", 0);
    }

    @Override
    public void initValues() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_bangumi_info_textView_toDetail:
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("bangumi", bangumi);

                break;
            case R.id.fragment_bangumi_info_imageView_download:

                break;
            case R.id.fragment_bangumi_info_imageView_favorite:

                break;
            default:
                break;
        }
    }
}
