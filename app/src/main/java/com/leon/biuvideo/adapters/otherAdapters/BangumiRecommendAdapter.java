package com.leon.biuvideo.adapters.otherAdapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.resourcesBeans.BangumiRecommend;
import com.leon.biuvideo.values.FragmentType;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/28
 * @Desc 番剧推荐适配器
 */
public class BangumiRecommendAdapter extends BaseAdapter<BangumiRecommend> {
    private final List<BangumiRecommend> bangumiRecommendList;

    public BangumiRecommendAdapter(List<BangumiRecommend> beans, Context context) {
        super(beans, context);
        this.bangumiRecommendList = beans;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.bangumi_recommend_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        BangumiRecommend bangumiRecommend = bangumiRecommendList.get(position);

        TextView bangumiRecommendItemBadge = holder
                .setImage(R.id.bangumi_recommend_item_cover, bangumiRecommend.cover, ImagePixelSize.COVER)
                .setText(R.id.bangumi_recommend_item_title, bangumiRecommend.title)
                .setText(R.id.bangumi_recommend_item_newEp, bangumiRecommend.newEp)
                .setText(R.id.bangumi_recommend_item_view, bangumiRecommend.play)
                .setText(R.id.bangumi_recommend_item_follow, bangumiRecommend.follow)
                .setText(R.id.bangumi_recommend_item_rating_score, bangumiRecommend.ratingScore)
                .setText(R.id.bangumi_recommend_item_rating_count, bangumiRecommend.ratingCount + "点评")
                .findById(R.id.bangumi_recommend_item_badge);

        if (bangumiRecommend.badge != null) {
            bangumiRecommendItemBadge.setText(bangumiRecommend.badge);
        } else {
            bangumiRecommendItemBadge.setVisibility(View.GONE);
        }

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPublicFragment(FragmentType.BANGUMI, bangumiRecommend.seasonId);
            }
        });
    }
}
