package com.leon.biuvideo.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.searchBean.bangumi.Bangumi;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.ImagePixelSize;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class BangumiDetailDialog extends AlertDialog {
    private final Context context;
    private final Bangumi bangumi;

    public BangumiDetailDialog(@NonNull Context context, Bangumi bangumi) {
        super(context);

        this.context = context;
        this.bangumi = bangumi;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bangumi_detail);

        initView();
    }

    private void initView() {
        View view = getWindow().getDecorView();
        BindingUtils bindingUtils = new BindingUtils(view, context);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        bindingUtils
                .setImage(R.id.bangumi_detail_dialog_imageView_cover, bangumi.cover, ImagePixelSize.COVER)
                .setText(R.id.bangumi_detail_dialog_textView_title, bangumi.title)
                .setText(R.id.bangumi_detail_dialog_textView_area, bangumi.area)
                .setText(R.id.bangumi_detail_dialog_textView_pubTime, simpleDateFormat.format(bangumi.playTime * 1000))
                .setText(R.id.bangumi_detail_dialog_textView_bangumiState, bangumi.bangumiState)
                .setText(R.id.bangumi_detail_dialog_textView_score, String.valueOf(bangumi.score))
                .setText(R.id.bangumi_detail_dialog_textView_reviews, ValueUtils.generateCN(bangumi.reviewNum) + "人点评")
                .setText(R.id.bangumi_detail_dialog_textView_originalName, bangumi.originalTitle)
                .setText(R.id.bangumi_detail_dialog_textView_otherInfo, bangumi.otherInfo)
                .setText(R.id.bangumi_detail_dialog_textView_desc, bangumi.desc)
                .setOnClickListener(R.id.bangumi_detail_dialog_imageView_back, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });

        RatingBar ratingBar = view.findViewById(R.id.bangumi_detail_dialog_ratingBar);
        ratingBar.setRating(bangumi.score / 2);
    }
}
