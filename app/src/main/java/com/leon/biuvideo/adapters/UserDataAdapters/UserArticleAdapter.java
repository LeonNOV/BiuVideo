package com.leon.biuvideo.adapters.UserDataAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.articleBeans.Article;
import com.leon.biuvideo.ui.activitys.ArticleActivity;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.List;

public class UserArticleAdapter extends BaseAdapter<Article> {
    private final List<Article> articles;
    private final Context context;

    public UserArticleAdapter(List<Article> articles, Context context) {
        super(articles, context);
        this.articles = articles;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.user_data_article_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Article article = articles.get(position);

        holder.setText(R.id.user_article_item_title, article.title)
                .setImage(R.id.user_article_item_cover, article.coverUrl, ImagePixelSize.COVER)
                .setText(R.id.user_article_item_desc, article.summary)
                .setText(R.id.user_article_item_addTime, "收藏于 " + ValueFormat.generateTime(article.ctime, true, false, "-"))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到ArticleActiity
                        Intent intent = new Intent(context, ArticleActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("article", article);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
    }
}
