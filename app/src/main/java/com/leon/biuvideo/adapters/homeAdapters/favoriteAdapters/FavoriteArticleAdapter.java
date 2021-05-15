package com.leon.biuvideo.adapters.homeAdapters.favoriteAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.homeBeans.favoriteBeans.FavoriteArticle;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.views.TagView;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.FragmentType;
import com.leon.biuvideo.values.ImagePixelSize;

/**
 * @Author Leon
 * @Time 2020/12/15
 * @Desc 用户收藏的专栏列表适配器
 */
public class FavoriteArticleAdapter extends BaseAdapter<FavoriteArticle> {
    private final MainActivity mainActivity;

    public FavoriteArticleAdapter(MainActivity mainActivity, Context context) {
        super(context);
        this.mainActivity = mainActivity;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.favorite_article_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        FavoriteArticle favoriteArticle = getAllData().get(position);

        TagView favoriteArticleItemAddTime = holder.findById(R.id.favorite_article_item_addTime);
        favoriteArticleItemAddTime.setRightValue(ValueUtils.generateTime(favoriteArticle.ctime, "yyyy-MM-dd HH:mm:ss", true));

        holder
                .setText(R.id.favorite_article_item_title, favoriteArticle.title)
                .setImage(R.id.favorite_article_item_cover, favoriteArticle.coverUrl, ImagePixelSize.COVER)
                .setText(R.id.favorite_article_item_desc, favoriteArticle.summary)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (InternetUtils.checkNetwork(v)) {
                            startPublicFragment(mainActivity, FragmentType.ARTICLE, String.valueOf(favoriteArticle.articleId));
                        }
                    }
                });
    }
}
