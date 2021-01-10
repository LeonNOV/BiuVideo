package com.leon.biuvideo.ui.fragments.bangumiFragments;

import android.os.Bundle;
import android.widget.RatingBar;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.searchBean.bangumi.Bangumi;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.values.ImagePixelSize;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class BangumiDetailFragment extends BaseFragment {
    private Bangumi bangumi;

    @Override
    public int setLayout() {
        return R.layout.fragment_bangumi_detail;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        getFragmentArguments();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        bindingUtils.setImage(R.id.fragment_bangumi_detail_imageView_cover, bangumi.cover, ImagePixelSize.COVER)
                .setText(R.id.fragment_bangumi_detail_textView_title, bangumi.title)
                .setText(R.id.fragment_bangumi_detail_textView_area, bangumi.area)
                .setText(R.id.fragment_bangumi_detail_textView_pubTime, simpleDateFormat.format(bangumi.playTime * 1000))
                .setText(R.id.fragment_bangumi_detail_textView_bangumiState, bangumi.eps.size() == bangumi.epSize ? "已完结" : "连载中")
                .setText(R.id.fragment_bangumi_detail_textView_epSize, "全" + bangumi.epSize + "话")
                .setText(R.id.fragment_bangumi_detail_textView_score, String.valueOf(bangumi.score))
                .setText(R.id.fragment_bangumi_detail_textView_reviews, ValueFormat.generateCN(bangumi.reviewNum) + "人点评")
                .setText(R.id.fragment_bangumi_detail_textView_originalName, bangumi.originalTitle)
                .setText(R.id.fragment_bangumi_detail_textView_otherInfo, bangumi.otherInfo)
                .setText(R.id.fragment_bangumi_detail_textView_desc, bangumi.desc);

        RatingBar ratingBar = findView(R.id.fragment_bangumi_detail_ratingBar);
        ratingBar.setRating(bangumi.score);
    }

    @Override
    public void initValues() {

    }

    private void getFragmentArguments() {
        Bundle arguments = getArguments();

        // 获取初始数据
        bangumi = (Bangumi) arguments.getSerializable("bangumi");
    }
}
