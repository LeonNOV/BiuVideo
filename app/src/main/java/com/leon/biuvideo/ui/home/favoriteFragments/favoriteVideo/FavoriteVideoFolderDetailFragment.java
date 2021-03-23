package com.leon.biuvideo.ui.home.favoriteFragments.favoriteVideo;

import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.favoriteAdapters.FavoriteVideoFolderDetailAdapter;
import com.leon.biuvideo.beans.homeBeans.favoriteBeans.FavoriteVideoFolderDetail;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.userParseUtils.FavoriteVideoFolderDetailParser;
import com.leon.biuvideo.values.FeaturesName;
import com.leon.biuvideo.values.ImagePixelSize;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/19
 * @Desc 视频收藏夹详细内容页面
 */
public class FavoriteVideoFolderDetailFragment extends BaseSupportFragment {
    private final String title;
    private final long folderId;

    private final List<FavoriteVideoFolderDetail.Media> mediaList = new ArrayList<>();
    private FavoriteVideoFolderDetailAdapter favoriteVideoFolderDetailAdapter;
    private FavoriteVideoFolderDetailParser favoriteVideoFolderDetailParser;

    private ImageView favoritesVideoFolderDetailCover;
    private TextView favoritesVideoFolderDetailTitle;
    private TextView favoritesVideoFolderDetailCount;

    public FavoriteVideoFolderDetailFragment(String title, long folderId) {
        this.title = title;
        this.folderId = folderId;
    }

    @Override
    protected int setLayout() {
        return R.layout.favorites_video_folder_detail_fragment;
    }

    @Override
    protected void initView() {
        SimpleTopBar favoriteVideoFolderDetailTopBar = findView(R.id.favorite_video_folder_detail_topBar);
        favoriteVideoFolderDetailTopBar.setTopBarTitle(title);
        favoriteVideoFolderDetailTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {

            }
        });

        SmartRefreshRecyclerView<FavoriteVideoFolderDetail.Media> favoritesVideoFolderDetailSmartRefreshRecyclerView = findView(R.id.favorites_video_folder_detail_smartRefreshRecyclerView);
        favoritesVideoFolderDetailSmartRefreshRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        favoritesVideoFolderDetailSmartRefreshRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (!favoriteVideoFolderDetailParser.dataStatus) {
                    favoritesVideoFolderDetailSmartRefreshRecyclerView.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                } else {
                    getFavoriteVideoFolderDetail(1);
                }
            }
        });

        favoriteVideoFolderDetailAdapter = new FavoriteVideoFolderDetailAdapter(mediaList, context);
        favoriteVideoFolderDetailAdapter.setHasStableIds(true);
        favoritesVideoFolderDetailSmartRefreshRecyclerView.setRecyclerViewAdapter(favoriteVideoFolderDetailAdapter);
        favoritesVideoFolderDetailSmartRefreshRecyclerView.setRecyclerViewLayoutManager(new LinearLayoutManager(context));

        favoritesVideoFolderDetailCover = findView(R.id.favorites_video_folder_detail_cover);
        favoritesVideoFolderDetailTitle = findView(R.id.favorites_video_folder_detail_title);
        favoritesVideoFolderDetailCount = findView(R.id.favorites_video_folder_detail_count);

        // 获取初始数据
        getFavoriteVideoFolderDetail(0);

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                FavoriteVideoFolderDetail favoriteVideoFolderDetail = (FavoriteVideoFolderDetail) msg.obj;

                switch (msg.what) {
                    case 0:
                        Glide
                                .with(context)
                                .load(PreferenceUtils.getFeaturesStatus(FeaturesName.IMG_ORIGINAL_MODEL)
                                        ? favoriteVideoFolderDetail.cover
                                        : favoriteVideoFolderDetail.cover + ImagePixelSize.COVER.value)
                                .into(favoritesVideoFolderDetailCover);
                        favoritesVideoFolderDetailTitle.setText(title);
                        favoritesVideoFolderDetailCount.setText(favoriteVideoFolderDetail.count + "个内容");

                        if (favoriteVideoFolderDetail.medias.size() == 0 || favoriteVideoFolderDetail.medias == null) {
                            favoritesVideoFolderDetailSmartRefreshRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                            favoritesVideoFolderDetailSmartRefreshRecyclerView.setEnableLoadMore(false);
                        } else {
                            favoriteVideoFolderDetailAdapter.append(favoriteVideoFolderDetail.medias);
                            favoritesVideoFolderDetailSmartRefreshRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                            if (!favoriteVideoFolderDetailParser.dataStatus) {
                                favoritesVideoFolderDetailSmartRefreshRecyclerView.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                            }
                        }

                        break;
                    case 1:
                        if (favoriteVideoFolderDetail.medias.size() > 0) {
                            favoriteVideoFolderDetailAdapter.append(favoriteVideoFolderDetail.medias);
                            favoritesVideoFolderDetailSmartRefreshRecyclerView.setSmartRefreshStatus(SmartRefreshRecyclerView.LOADING_FINISHING);

                            // 每加载一次就检查当前页是否为最后一页
                            if (!favoriteVideoFolderDetailParser.dataStatus) {
                                favoritesVideoFolderDetailSmartRefreshRecyclerView.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                            }
                        }

                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 获取视频收藏夹数据
     *
     * @param what  what
     */
    private void getFavoriteVideoFolderDetail(int what) {
        if (favoriteVideoFolderDetailParser == null) {
            favoriteVideoFolderDetailParser = new FavoriteVideoFolderDetailParser(folderId);
        }

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                FavoriteVideoFolderDetail favoriteVideoFolderDetail = favoriteVideoFolderDetailParser.parseData();

                Message message = receiveDataHandler.obtainMessage(what);
                message.obj = favoriteVideoFolderDetail;
                receiveDataHandler.sendMessage(message);
            }
        });
    }
}
