package com.leon.biuvideo.adapters.homeAdapters.popularAdapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.homeBeans.popularBeans.PopularTopList;
import com.leon.biuvideo.ui.views.TagView;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/25
 * @Desc 排行榜数据适配器
 */
public class PopularTopListAdapter extends BaseAdapter<PopularTopList> {
    /**
     * 其他视频是否已展开显示
     */
    private boolean isExpand = false;

    /**
     * 是否已初始化（设置Adapter）其他视频
     */
    private boolean isInitExpand = false;

    private final List<PopularTopList> popularTopLists;

    public PopularTopListAdapter(List<PopularTopList> beans, Context context) {
        super(beans, context);

        this.popularTopLists = beans;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.popular_top_list_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        PopularTopList popularTopList = popularTopLists.get(position);

        initContent(holder, popularTopList, position);
        initExpandView(holder, popularTopList);
    }

    private void initContent(BaseViewHolder holder, PopularTopList popularTopList, int position) {
        TextView popularTopListItemDuration = holder.findById(R.id.popular_top_list_item_duration);
        TextView popularTopListItemBadge = holder.findById(R.id.popular_top_list_item_badge);

        if (popularTopList.duration != null) {
            popularTopListItemDuration.setVisibility(View.VISIBLE);
            popularTopListItemDuration.setText(popularTopList.duration);
        } else {
            popularTopListItemDuration.setVisibility(View.GONE);
        }

        if (popularTopList.duration != null) {
            popularTopListItemBadge.setVisibility(View.VISIBLE);
            popularTopListItemBadge.setText(popularTopList.badge);
        } else {
            popularTopListItemBadge.setVisibility(View.GONE);
        }

        holder
                .setImage(R.id.popular_top_list_item_cover, popularTopList.cover, ImagePixelSize.COVER)
                .setText(R.id.popular_top_list_item_title, popularTopList.title)
                .setText(R.id.popular_top_list_item_extra1, popularTopList.extra1)
                .setOnClickListener(R.id.popular_top_list_item_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

        TextView popularTopListItemExtra2 = holder.findById(R.id.popular_top_list_item_extra2);
        if (popularTopList.extra2 == null) {
            popularTopListItemExtra2.setVisibility(View.GONE);
        } else {
            popularTopListItemExtra2.setVisibility(View.VISIBLE);
            popularTopListItemExtra2.setText(popularTopList.extra2);
        }

        ((TagView)holder.findById(R.id.popular_top_list_item_score)).setRightValue(popularTopList.score);

        ImageView popularTopListItemRankBg = holder.findById(R.id.popular_top_list_item_rank_bg);
        TextView popularTopListItemRank = holder.findById(R.id.popular_top_list_item_rank);

        Drawable wrap = DrawableCompat.wrap(ContextCompat.getDrawable(context,R.drawable.ic_rank));
        switch (position) {
            case 0:
                DrawableCompat.setTint(wrap, ContextCompat.getColor(context,R.color.popular_top_list_first_img_color));
                popularTopListItemRankBg.setImageDrawable(wrap);
                popularTopListItemRank.setTextColor(ContextCompat.getColor(context, R.color.popular_top_list_first_text_color));
                break;
            case 1:
                DrawableCompat.setTint(wrap, ContextCompat.getColor(context,R.color.popular_top_list_second_img_color));
                popularTopListItemRankBg.setImageDrawable(wrap);
                popularTopListItemRank.setTextColor(ContextCompat.getColor(context, R.color.popular_top_list_second_text_color));
                break;
            case 2:
                DrawableCompat.setTint(wrap, ContextCompat.getColor(context,R.color.popular_top_list_third_img_color));
                popularTopListItemRankBg.setImageDrawable(wrap);
                popularTopListItemRank.setTextColor(ContextCompat.getColor(context, R.color.popular_top_list_third_text_color));
                break;
            default:
                DrawableCompat.setTint(wrap, ContextCompat.getColor(context,R.color.popular_top_list_other_img_color));
                popularTopListItemRankBg.setImageDrawable(wrap);
                popularTopListItemRank.setTextColor(ContextCompat.getColor(context, R.color.popular_top_list_other_text_color));
                break;
        }

        popularTopListItemRank.setText(String.valueOf(position + 1));
    }

    private void initExpandView(@NonNull BaseViewHolder holder, PopularTopList popularTopList) {
        LinearLayout popularTopListExpandOther = holder.findById(R.id.popular_top_list_expand_other);
        if (popularTopList.otherVideoList == null || popularTopList.otherVideoList.size() == 0) {
            popularTopListExpandOther.setVisibility(View.GONE);
        } else {
            RecyclerView popularTopListItemExpandOtherVideos = holder.findById(R.id.popular_top_list_item_expand_otherVideos);
            TextView popularTopListItemExpandText = holder.findById(R.id.popular_top_list_item_expand_text);
            ImageView popularTopListItemExpandImg = holder.findById(R.id.popular_top_list_item_expand_img);

            holder.setOnClickListener(R.id.popular_top_list_expand, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isExpand) {
                        if (!isInitExpand) {
                            // popularTopListItemExpandOtherVideos设置Adapter
                            PopularTopListExpandAdapter popularTopListExpandAdapter = new PopularTopListExpandAdapter(popularTopList.otherVideoList, context);
                            popularTopListExpandAdapter.setHasStableIds(true);
                            popularTopListItemExpandOtherVideos.setAdapter(popularTopListExpandAdapter);

                            isInitExpand = true;
                        }
                        popularTopListItemExpandOtherVideos.setVisibility(View.VISIBLE);
                        popularTopListItemExpandText.setText(context.getText(R.string.popular_top_list_hide_otherVideos));
                        isExpand = true;
                    } else {
                        popularTopListItemExpandOtherVideos.setVisibility(View.GONE);
                        popularTopListItemExpandText.setText(context.getText(R.string.popular_top_list_show_otherVideos));
                        isExpand = false;
                    }
                }
            });
        }
    }
}
