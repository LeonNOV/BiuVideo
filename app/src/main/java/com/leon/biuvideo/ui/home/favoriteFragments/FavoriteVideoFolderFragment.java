package com.leon.biuvideo.ui.home.favoriteFragments;

import android.os.Message;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.home.FavoriteVideoFolderAdapter;
import com.leon.biuvideo.beans.orderBeans.FavoriteVideoFolder;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.home.FavoritesFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.userParseUtils.FavoriteVideoFolderParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 收藏页面-视频收藏
 */
public class FavoriteVideoFolderFragment extends BaseSupportFragment {
    @Override
    protected int setLayout() {
        return R.layout.favorites_video_folder_fragment;
    }

    @Override
    protected void initView() {
        LoadingRecyclerView favoritesVideoFolderLoadingRecyclerView = findView(R.id.favorites_video_folder_loadingRecyclerView);
        favoritesVideoFolderLoadingRecyclerView.setStatus(LoadingRecyclerView.LOADING);

        FavoriteVideoFolderAdapter favoriteVideoFolderAdapter = new FavoriteVideoFolderAdapter(new ArrayList<>(), context);
        favoriteVideoFolderAdapter.setOnClickVideoFolderListener(new FavoriteVideoFolderAdapter.OnClickVideoFolderListener() {
            @Override
            public void onClick(String title, long folderId) {
                ((FavoritesFragment)getParentFragment()).start(new FavoriteVideoFolderDetailFragment(title, folderId));
            }
        });
        favoriteVideoFolderAdapter.setHasStableIds(true);
        favoritesVideoFolderLoadingRecyclerView.setRecyclerViewAdapter(favoriteVideoFolderAdapter);
        favoritesVideoFolderLoadingRecyclerView.setRecyclerViewLayoutManager(new LinearLayoutManager(context));

        // 获取所有视频收藏夹
        getVideoFolders();

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<FavoriteVideoFolder> favoriteVideoFolders = (List<FavoriteVideoFolder>) msg.obj;

                if (favoriteVideoFolders.size() > 0) {
                    favoriteVideoFolderAdapter.append(favoriteVideoFolders);
                    favoritesVideoFolderLoadingRecyclerView.setStatus(LoadingRecyclerView.LOADING_FINISH);
                } else {
                    favoritesVideoFolderLoadingRecyclerView.setStatus(LoadingRecyclerView.NO_DATA);
                }
            }
        });
    }

    /**
     * 获取视频收藏夹数据
     */
    private void getVideoFolders() {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                List<FavoriteVideoFolder> favoriteVideoFolderList = new FavoriteVideoFolderParser().parseData();

                Message message = receiveDataHandler.obtainMessage();
                message.obj = favoriteVideoFolderList;
                receiveDataHandler.sendMessage(message);
            }
        });
    }
}
