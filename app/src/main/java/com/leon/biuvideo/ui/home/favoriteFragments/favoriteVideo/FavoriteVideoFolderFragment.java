package com.leon.biuvideo.ui.home.favoriteFragments.favoriteVideo;

import com.leon.biuvideo.adapters.homeAdapters.favoriteAdapters.FavoriteVideoFolderAdapter;
import com.leon.biuvideo.beans.homeBeans.favoriteBeans.FavoriteVideoFolder;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragmentWithSrr;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.userDataParsers.FavoriteVideoFolderParser;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 收藏页面-视频收藏
 */
public class FavoriteVideoFolderFragment extends BaseSupportFragmentWithSrr<FavoriteVideoFolder> {

    private DataLoader<FavoriteVideoFolder> dataLoader;

    @Override
    protected void onLazyLoad() {
        dataLoader.insertData(true);
    }

    @Override
    protected void initView() {
        dataLoader = new DataLoader<>(context, new FavoriteVideoFolderParser(),
                new FavoriteVideoFolderAdapter(getMainActivity(), context), this);
    }
}
