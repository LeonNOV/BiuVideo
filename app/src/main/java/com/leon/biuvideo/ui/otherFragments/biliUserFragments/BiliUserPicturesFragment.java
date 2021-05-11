package com.leon.biuvideo.ui.otherFragments.biliUserFragments;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.biliUserResourcesAdapters.BiliUserPicturesAdapter;
import com.leon.biuvideo.beans.biliUserResourcesBeans.BiliUserPicture;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.biliUserResourcesParseUtils.BiliUserPicturesParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/9
 * @Desc B站用户相簿数据
 */
public class BiliUserPicturesFragment extends BaseLazySupportFragment {
    private final String mid;

    private final List<BiliUserPicture> biliUserPictureList = new ArrayList<>();
    private SmartRefreshRecyclerView<BiliUserPicture> biliUserPicturesData;
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
        BiliUserPicturesAdapter biliUserPicturesAdapter = new BiliUserPicturesAdapter(biliUserPictureList, context);
        biliUserPicturesAdapter.setHasStableIds(true);
        biliUserPicturesData = findView(R.id.bili_user_pictures_data);
        biliUserPicturesData.setRecyclerViewAdapter(biliUserPicturesAdapter);
        biliUserPicturesData.setRecyclerViewLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        biliUserPicturesData.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                biliUserPictureDataLoader.insertData(false);
            }
        });

        biliUserPictureDataLoader = new DataLoader<>(new BiliUserPicturesParser(mid), biliUserPicturesData, biliUserPicturesAdapter, this);
    }

    @Override
    protected void onLazyLoad() {
        biliUserPicturesData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        biliUserPictureDataLoader.insertData(true);
    }
}
