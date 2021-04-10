package com.leon.biuvideo.adapters.biliUserResourcesAdapters;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.biliUserResourcesBeans.BiliUserArticle;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/10
 * @Desc B站用户专栏数据适配器
 */
public class BiliUserArticlesAdapter extends BaseAdapter<BiliUserArticle> {
    private final List<BiliUserArticle> biliUserArticleList;

    public BiliUserArticlesAdapter(List<BiliUserArticle> beans, Context context) {
        super(beans, context);
        this.biliUserArticleList = beans;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.bili_user_article_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        BiliUserArticle biliUserArticle = biliUserArticleList.get(position);

        holder
                .setText(R.id.bili_user_article_item_title, biliUserArticle.title)
                .setImage(R.id.bili_user_article_item_cover, biliUserArticle.cover, ImagePixelSize.COVER)
                .setText(R.id.bili_user_article_item_summary, biliUserArticle.summary)
                .setText(R.id.bili_user_article_item_view, biliUserArticle.view)
                .setText(R.id.bili_user_article_item_like, biliUserArticle.like)
                .setText(R.id.bili_user_article_item_comment, biliUserArticle.comment)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "ID:" + biliUserArticle.id, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
