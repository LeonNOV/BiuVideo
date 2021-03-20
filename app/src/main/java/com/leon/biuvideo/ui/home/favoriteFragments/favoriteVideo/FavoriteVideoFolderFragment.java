package com.leon.biuvideo.ui.home.favoriteFragments.favoriteVideo;

import android.os.Bundle;
import android.os.Message;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.favoriteAdapters.FavoriteVideoFolderAdapter;
import com.leon.biuvideo.beans.homeBeans.favoriteBeans.FavoriteVideoFolder;
import com.leon.biuvideo.beans.userBeans.Follow;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragmentWithSrr;
import com.leon.biuvideo.ui.home.FavoritesFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.userParseUtils.FavoriteVideoFolderParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 收藏页面-视频收藏
 */
public class FavoriteVideoFolderFragment extends BaseSupportFragmentWithSrr<FavoriteVideoFolder> {

    @Override
    protected void initView() {
        FavoriteVideoFolderAdapter favoriteVideoFolderAdapter = new FavoriteVideoFolderAdapter(new ArrayList<>(), context);
        favoriteVideoFolderAdapter.setOnClickVideoFolderListener(new FavoriteVideoFolderAdapter.OnClickVideoFolderListener() {
            @Override
            public void onClick(String title, long folderId) {
                ((FavoritesFragment)getParentFragment()).start(new FavoriteVideoFolderDetailFragment(title, folderId));
            }
        });
        favoriteVideoFolderAdapter.setHasStableIds(true);
        view.setRecyclerViewAdapter(favoriteVideoFolderAdapter);
        view.setRecyclerViewLayoutManager(new LinearLayoutManager(context));

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<FavoriteVideoFolder> favoriteVideoFolders = (List<FavoriteVideoFolder>) msg.obj;

                if (favoriteVideoFolders.size() > 0) {
                    favoriteVideoFolderAdapter.append(favoriteVideoFolders);
                    view.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                } else {
                    view.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                }
            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        view.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        Fuck.blue("one");

        // 获取所有视频收藏夹
        getVideoFolders();
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