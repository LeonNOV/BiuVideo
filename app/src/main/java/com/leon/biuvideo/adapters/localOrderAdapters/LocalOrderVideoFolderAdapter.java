package com.leon.biuvideo.adapters.localOrderAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.orderBeans.LocalVideoFolder;

import java.util.List;

/**
 * 本地订阅-视频文件夹
 */
public class LocalOrderVideoFolderAdapter extends BaseAdapter<LocalVideoFolder> {
    private final List<LocalVideoFolder> localVideoFolderList;

    public LocalOrderVideoFolderAdapter(Context context, List<LocalVideoFolder> localVideoFolderList) {
        super(localVideoFolderList, context);
        this.localVideoFolderList = localVideoFolderList;
    }

    private OnClickFolderListener onClickFolderListener;

    public interface OnClickFolderListener {
        /**
         * 点击视频文件夹的回调方法
         */
        void OnClick(LocalVideoFolder localVideoFolder);
    }

    public void setOnClickFolderListener(OnClickFolderListener onClickFolderListener) {
        this.onClickFolderListener = onClickFolderListener;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.item_favorite_video_folder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        LocalVideoFolder videoFolder = localVideoFolderList.get(position);

        holder
                .setText(R.id.item_favorite_video_textView_folderName, videoFolder.folderName)
                .setText(R.id.item_favorite_video_textView_folderTotal, String.valueOf(videoFolder.videoCount))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickFolderListener != null) {
                            onClickFolderListener.OnClick(videoFolder);
                        }
                    }
                });
    }
}
