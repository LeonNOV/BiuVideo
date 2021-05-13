package com.leon.biuvideo.ui.otherFragments.biliUserFragments;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.biliUserResourcesAdapters.BiliUserPicturesAdapter;
import com.leon.biuvideo.beans.biliUserResourcesBeans.BiliUserPicture;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.biliUserResourcesParseUtils.BiliUserPicturesParser;

/**
 * @Author Leon
 * @Time 2021/4/9
 * @Desc B站用户相簿数据
 */
public class BiliUserPicturesFragment extends BaseLazySupportFragment {
    private final String mid;
    private DataLoader<BiliUserPicture> biliUserPictureDataLoader;

    public BiliUserPicturesFragment(String mid) {
        this.mid = mid;
    }

    @Override
    protected int setLayout() {
        return R.layout.bili_user_pictures_fragment;
    }

    @Override
    protected void initView() {
        biliUserPictureDataLoader = new DataLoader<>(new BiliUserPicturesParser(mid), R.id.bili_user_pictures_data,
                new BiliUserPicturesAdapter(context), this, new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL), context);
    }

    @Override
    protected void onLazyLoad() {
        biliUserPictureDataLoader.insertData(true);
    }
}
