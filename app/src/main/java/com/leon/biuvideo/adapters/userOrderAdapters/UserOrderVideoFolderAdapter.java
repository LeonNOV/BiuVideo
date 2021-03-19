package com.leon.biuvideo.adapters.userOrderAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.orderBeans.FavoriteVideoFolder;

import java.util.List;

/**
 * 用户收藏的视频文件夹适配器
 */
public class UserOrderVideoFolderAdapter extends BaseAdapter<FavoriteVideoFolder> {
    private final List<FavoriteVideoFolder> favoriteVideoFolders;

    public UserOrderVideoFolderAdapter(List<FavoriteVideoFolder> favoriteVideoFolders, Context context) {
        super(favoriteVideoFolders, context);
        this.favoriteVideoFolders = favoriteVideoFolders;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.item_favorite_video_folder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        FavoriteVideoFolder favoriteVideoFolder = favoriteVideoFolders.get(position);

        holder
                .setText(R.id.item_favorite_video_textView_folderName, favoriteVideoFolder.title)
                .setText(R.id.item_favorite_video_textView_folderTotal, String.valueOf(favoriteVideoFolder.count))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickFolderListener != null) {
                            onClickFolderListener.OnClick(favoriteVideoFolder.id);
                        }
                    }
                });
    }

    private OnClickFolderListener onClickFolderListener;

    public interface OnClickFolderListener {
        void OnClick(long folderId);
    }

    public void setOnClickFolderListener(OnClickFolderListener onClickFolderListener) {
        this.onClickFolderListener = onClickFolderListener;
    }
}
