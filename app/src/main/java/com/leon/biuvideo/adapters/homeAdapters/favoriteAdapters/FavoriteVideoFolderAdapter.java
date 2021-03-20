package com.leon.biuvideo.adapters.homeAdapters.favoriteAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.homeBeans.favoriteBeans.FavoriteVideoFolder;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/19
 * @Desc 用户视频收藏夹适配器
 */
public class FavoriteVideoFolderAdapter extends BaseAdapter<FavoriteVideoFolder> {
    private final List<FavoriteVideoFolder> favoriteVideoFolders;

    private OnClickVideoFolderListener onClickVideoFolderListener;

    public interface OnClickVideoFolderListener {
        /**
         * 点击事件，跳转至FavoriteVideoFolderDetailFragment
         *
         * @param title 收藏夹标题
         * @param folderId 收藏夹ID
         */
        void onClick(String title, long folderId);
    }

    public void setOnClickVideoFolderListener(OnClickVideoFolderListener onClickVideoFolderListener) {
        this.onClickVideoFolderListener = onClickVideoFolderListener;
    }

    public FavoriteVideoFolderAdapter(List<FavoriteVideoFolder> beans, Context context) {
        super(beans, context);

        this.favoriteVideoFolders = beans;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.favorite_video_folder_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        FavoriteVideoFolder favoriteVideoFolder = favoriteVideoFolders.get(position);

        holder
                .setText(R.id.favorite_video_folder_item_name, favoriteVideoFolder.title)
                .setText(R.id.favorite_video_folder_item_count, String.valueOf(favoriteVideoFolder.count))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickVideoFolderListener != null) {
                            onClickVideoFolderListener.onClick(favoriteVideoFolder.title, favoriteVideoFolder.id);
                        }
                    }
                });
    }
}
