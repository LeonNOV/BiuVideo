package com.leon.biuvideo.adapters.UserFragmentAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.searchBean.bangumi.Bangumi;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.List;

public class BangumiAdapter extends BaseAdapter<Bangumi> {
    private final List<Bangumi> bangumiList;
    private final Context context;

    public BangumiAdapter(List<Bangumi> bangumiList, Context context) {
        super(bangumiList, context);

        this.bangumiList = bangumiList;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.search_result_bangumi_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Bangumi bangumi = bangumiList.get(position);
        holder
                .setImage(R.id.search_result_bangumi_imageView_cover, bangumi.cover, ImagePixelSize.COVER)
                .setText(R.id.search_result_bangumi_textView_title, bangumi.title)
                .setText(R.id.search_result_bangumi_textView_pubTime, ValueFormat.generateTime(bangumi.playTime, true, true, "-"))
                .setText(R.id.search_result_bangumi_textView_area, bangumi.area)
                .setText(R.id.search_result_bangumi_textView_style, bangumi.styles)
                .setText(R.id.search_result_bangumi_textView_score, String.valueOf(bangumi.score))
                .setText(R.id.search_result_bangumi_textView_reviewNum, ValueFormat.generateCN(bangumi.reviewNum) + "人点评")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fuck.blue("title:" + bangumi.title + "----mid:" + bangumi.mediaId + "----sid:" + bangumi.seasonId);
                    }
                });

        if (bangumi.angleTitle.equals("")) {
            holder.setVisibility(R.id.search_result_bangumi_textView_angleTitle, View.GONE);
        } else {
            holder.setText(R.id.search_result_bangumi_textView_angleTitle, bangumi.angleTitle);
        }

        RecyclerView recyclerView = holder.findById(R.id.search_result_bangumi_recyclerView_eps);
        recyclerView.setAdapter(new BangumiEpAdapter(bangumi.eps, context));

    }
}