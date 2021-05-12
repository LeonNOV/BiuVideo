package com.leon.biuvideo.adapters.discoverAdapters.searchResultAdapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.searchResultBeans.SearchResultArticle;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.values.FeaturesName;
import com.leon.biuvideo.values.FragmentType;
import com.leon.biuvideo.values.ImagePixelSize;

/**
 * @Author Leon
 * @Time 2021/3/31
 * @Desc 专栏搜索结果适配器
 */
public class SearchResultArticleAdapter extends BaseAdapter<SearchResultArticle> {
    public SearchResultArticleAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.search_result_article_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        SearchResultArticle searchResultArticle = getAllData().get(position);

        holder
                .setText(R.id.search_result_item_article_title, searchResultArticle.title)
                .setText(R.id.search_result_item_article_userName, searchResultArticle.userName)
                .setText(R.id.search_result_item_article_view, searchResultArticle.view)
                .setText(R.id.search_result_item_article_like, searchResultArticle.like)
                .setText(R.id.search_result_item_article_comment, searchResultArticle.comment)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startPublicFragment(FragmentType.ARTICLE, searchResultArticle.articleId);
                    }
                });

        ImageView searchResultItemArticleCover1 = holder.findById(R.id.search_result_item_article_cover1);
        ImageView searchResultItemArticleCover2 = holder.findById(R.id.search_result_item_article_cover2);
        ImageView searchResultItemArticleCover3 = holder.findById(R.id.search_result_item_article_cover3);

        if (searchResultArticle.images.length == 1) {
            Glide
                    .with(context)
                    .load(searchResultArticle.images[0] += PreferenceUtils.getFeaturesStatus(FeaturesName.IMG_ORIGINAL_MODEL) ? ImagePixelSize.COVER.value : "")
                    .into(searchResultItemArticleCover1);

            searchResultItemArticleCover2.setVisibility(View.GONE);
            searchResultItemArticleCover3.setVisibility(View.GONE);
        } else if (searchResultArticle.images.length == 2) {
            Glide
                    .with(context)
                    .load(searchResultArticle.images[0] += PreferenceUtils.getFeaturesStatus(FeaturesName.IMG_ORIGINAL_MODEL) ? ImagePixelSize.COVER.value : "")
                    .into(searchResultItemArticleCover1);
            Glide
                    .with(context)
                    .load(searchResultArticle.images[1] += PreferenceUtils.getFeaturesStatus(FeaturesName.IMG_ORIGINAL_MODEL) ? ImagePixelSize.COVER.value : "")
                    .into(searchResultItemArticleCover2);
            searchResultItemArticleCover3.setVisibility(View.GONE);
        } else if (searchResultArticle.images.length == 3) {
            Glide
                    .with(context)
                    .load(searchResultArticle.images[0] += PreferenceUtils.getFeaturesStatus(FeaturesName.IMG_ORIGINAL_MODEL) ? ImagePixelSize.COVER.value : "")
                    .into(searchResultItemArticleCover1);
            Glide
                    .with(context)
                    .load(searchResultArticle.images[1] += PreferenceUtils.getFeaturesStatus(FeaturesName.IMG_ORIGINAL_MODEL) ? ImagePixelSize.COVER.value : "")
                    .into(searchResultItemArticleCover2);
            Glide
                    .with(context)
                    .load(searchResultArticle.images[2] += PreferenceUtils.getFeaturesStatus(FeaturesName.IMG_ORIGINAL_MODEL) ? ImagePixelSize.COVER.value : "")
                    .into(searchResultItemArticleCover3);
        }
    }
}
