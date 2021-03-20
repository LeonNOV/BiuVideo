package com.leon.biuvideo.adapters.userAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.homeBeans.favoriteBeans.FavoriteArticle;
import com.leon.biuvideo.ui.activitys.ArticleActivity;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.utils.InternetUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 文章列表适配器
 */
public class UserArticleAdapter extends BaseAdapter<FavoriteArticle> {
    private final List<FavoriteArticle> favoriteArticles;
    private final Context context;

    public UserArticleAdapter(List<FavoriteArticle> favoriteArticles, Context context) {
        super(favoriteArticles, context);
        this.favoriteArticles = favoriteArticles;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.user_article_list_view_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        FavoriteArticle favoriteArticle = favoriteArticles.get(position);

        //设置封面
        holder.setImage(R.id.article_imageView_cover, favoriteArticle.coverUrl, ImagePixelSize.COVER)

                //设置文章标题
                .setText(R.id.article_textView_title, favoriteArticle.title)

                //设置文章类型
                .setText(R.id.article_textView_category, favoriteArticle.category)

                //设置创建时间
                .setText(R.id.article_textView_ctime, new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(new Date(favoriteArticle.ctime * 1000)))

                //设置点击监听
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判断是否有网络
                        boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                        if (!isHaveNetwork) {
                            SimpleSnackBar.make(v, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();
                            return;
                        }

                        //跳转至ArticleActivity
                        FavoriteArticle favoriteArticle = favoriteArticles.get(position);

                        Intent intent = new Intent(context, ArticleActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("favoriteArticle", favoriteArticle);
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intent);
                    }
                });
    }
}