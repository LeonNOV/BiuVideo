package com.leon.biuvideo.adapters.UserFragmentAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapter.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapter.BaseViewHolder;
import com.leon.biuvideo.beans.articleBeans.Article;
import com.leon.biuvideo.ui.activitys.ArticleActivity;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.utils.InternetUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 文章列表适配器
 */
public class UserArticleAdapter extends BaseAdapter<Article> {
    private List<Article> articles;
    private final Context context;

    public UserArticleAdapter(List<Article> articles, Context context) {
        super(articles, context);
        this.articles = articles;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.user_article_list_view_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Article article = articles.get(position);

        //设置封面
        holder.setImage(R.id.article_imageView_cover, article.coverUrl, ImagePixelSize.COVER)

                //设置文章标题
                .setText(R.id.article_textView_title, article.title)

                //设置文章类型
                .setText(R.id.article_textView_category, article.category)

                //设置创建时间
                .setText(R.id.article_textView_ctime, new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(new Date(article.ctime * 1000)))

                //设置点击监听
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判断是否有网络
                        boolean isHaveNetwork = InternetUtils.checkNetwork(context);

                        if (!isHaveNetwork) {
                            Toast.makeText(context, R.string.network_sign, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //跳转至ArticleActivity
                        Article article = articles.get(position);

                        Intent intent = new Intent(context, ArticleActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("article", article);
                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intent);
                    }
                });
    }
}