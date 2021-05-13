package com.leon.biuvideo.ui.home.favoriteFragments;

import com.leon.biuvideo.adapters.homeAdapters.favoriteAdapters.FavoriteArticleAdapter;
import com.leon.biuvideo.beans.homeBeans.favoriteBeans.FavoriteArticle;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragmentWithSrr;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.userDataParsers.FavoriteArticleParser;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 收藏页面-专栏收藏
 */
public class FavoriteArticleFragment extends BaseSupportFragmentWithSrr<FavoriteArticle> {
    private DataLoader<FavoriteArticle> favoriteArticleDataLoader;

    @Override
    protected void onLazyLoad() {
        favoriteArticleDataLoader.insertData(true);
    }

    @Override
    protected void initView() {
        favoriteArticleDataLoader = new DataLoader<>(context, new FavoriteArticleParser(),
                new FavoriteArticleAdapter(context), this);
        favoriteArticleDataLoader.insertData(false);
    }
}
