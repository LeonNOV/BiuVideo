package com.leon.biuvideo.adapters.orderAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.orderBeans.LocalVideoFolder;

import java.util.List;

public class LocalVideoFolderAdapter extends BaseAdapter<LocalVideoFolder> {
    private final List<LocalVideoFolder> localVideoFolderList;

    public LocalVideoFolderAdapter(Context context, List<LocalVideoFolder> localVideoFolderList) {
        super(localVideoFolderList, context);
        this.localVideoFolderList = localVideoFolderList;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.item_local_video_folder;
    }

    private OnVideoFolderClickListener onVideoFolderClickListener;

    public interface OnVideoFolderClickListener {
        void OnClick(LocalVideoFolder localVideoFolder);
    }

    public void setOnVideoFolderClickListener(OnVideoFolderClickListener onVideoFolderClickListener) {
        this.onVideoFolderClickListener = onVideoFolderClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        LocalVideoFolder localVideoFolder = localVideoFolderList.get(position);

        holder
                .setText(R.id.item_local_video_textView_folderName, localVideoFolder.folderName)
                .setText(R.id.item_local_video_textView_orderTotal, String.valueOf(localVideoFolder.videoCount))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onVideoFolderClickListener != null) {
                            onVideoFolderClickListener.OnClick(localVideoFolder);
                        }
                    }
                });
    }

    /**
     * 添加视频后刷新视频收藏夹显示的总个数
     *
     * @param localVideoFolder  localVideoFolder
     */
    public void refresh (LocalVideoFolder localVideoFolder) {
        int indexOf = this.localVideoFolderList.indexOf(localVideoFolder);

        LocalVideoFolder videoFolder = this.localVideoFolderList.get(indexOf);
        videoFolder.videoCount++;

        this.localVideoFolderList.set(indexOf, videoFolder);
        notifyDataSetChanged();
    }
}
