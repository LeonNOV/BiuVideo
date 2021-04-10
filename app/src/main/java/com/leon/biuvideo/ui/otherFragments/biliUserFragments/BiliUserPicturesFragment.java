package com.leon.biuvideo.ui.otherFragments.biliUserFragments;

import android.os.Message;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.biliUserResourcesAdapters.BiliUserPicturesAdapter;
import com.leon.biuvideo.beans.biliUserResourcesBeans.BiliUserPicture;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
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

    private BiliUserPicturesParser biliUserPicturesParser;
    private final List<BiliUserPicture> biliUserPictureList = new ArrayList<>();
    private SmartRefreshRecyclerView<BiliUserPicture> biliUserPicturesData;
    private BiliUserPicturesAdapter biliUserPicturesAdapter;

    public BiliUserPicturesFragment(String mid) {
        this.mid = mid;
    }

    @Override
    protected int setLayout() {
        return R.layout.bili_user_pictures_fragment;
    }

    @Override
    protected void initView() {
        biliUserPicturesAdapter = new BiliUserPicturesAdapter(biliUserPictureList, context);
        biliUserPicturesAdapter.setHasStableIds(true);
        biliUserPicturesData = findView(R.id.bili_user_pictures_data);
        biliUserPicturesData.setRecyclerViewAdapter(biliUserPicturesAdapter);
        biliUserPicturesData.setRecyclerViewLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        biliUserPicturesData.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                getPictures(1);
            }
        });

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<BiliUserPicture> biliUserPictureList = (List<BiliUserPicture>) msg.obj;

                switch (msg.what) {
                    case 0:
                        if (biliUserPictureList != null && biliUserPictureList.size() > 0) {
                            biliUserPicturesAdapter.append(biliUserPictureList);
                            biliUserPicturesData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                        } else {
                            biliUserPicturesData.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                            biliUserPicturesData.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        }
                        break;

                    case 1:
                        if (biliUserPictureList != null && biliUserPictureList.size() > 0) {
                            biliUserPicturesAdapter.append(biliUserPictureList);

                            if (!biliUserPicturesParser.dataStatus) {
                                biliUserPicturesData.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                            }
                        } else {
                            biliUserPicturesData.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onLazyLoad() {
        biliUserPicturesData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        getPictures(0);
    }

    private void getPictures(int what) {
        if (biliUserPicturesParser == null) {
            biliUserPicturesParser = new BiliUserPicturesParser(mid);
        }

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                List<BiliUserPicture> biliUserPictureList = biliUserPicturesParser.parseData();

                Message message = receiveDataHandler.obtainMessage(what);
                message.obj = biliUserPictureList;
                receiveDataHandler.sendMessage(message);
            }
        });
    }
}
