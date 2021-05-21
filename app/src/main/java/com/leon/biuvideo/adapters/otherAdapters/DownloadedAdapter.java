package com.leon.biuvideo.adapters.otherAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.DownloadedRecord;
import com.leon.biuvideo.greendao.dao.DownloadHistory;
import com.leon.biuvideo.values.ImagePixelSize;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/5/14
 * @Desc 已下载资源适配器
 */
public class DownloadedAdapter extends BaseAdapter<DownloadedRecord> {
    private OnDownloadedItemListener onDownloadedItemListener;

    public DownloadedAdapter(Context context) {
        super(context);
    }

    public interface OnDownloadedItemListener {
        /**
         * 播放视频
         */
        void onPlay (DownloadHistory downloadHistory);

        /**
         * 查看文件夹详情
         */
        void onDetail (String title, List<DownloadHistory> downloadHistoryList);
    }

    public void setOnDownloadedItemListener(OnDownloadedItemListener onDownloadedItemListener) {
        this.onDownloadedItemListener = onDownloadedItemListener;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.downloaded_item;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BaseViewHolder holder, int position) {
        DownloadedRecord downloadedRecord = getAllData().get(position);

        holder
                .setImage(R.id.downloaded_item_cover, downloadedRecord.cover, ImagePixelSize.COVER)
                .setText(R.id.downloaded_item_title, downloadedRecord.title)
                .setText(R.id.downloaded_size, downloadedRecord.fileSize);

        if (downloadedRecord.count > 1) {
            holder
                    .setVisibility(R.id.downloaded_item_epCount, View.VISIBLE)
                    .setText(R.id.downloaded_item_epCount, downloadedRecord.count + "P");
        }

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDownloadedItemListener != null) {
                    if (downloadedRecord.count == 1) {
                        onDownloadedItemListener.onPlay(downloadedRecord.downloadHistory);
                    } else {
                        onDownloadedItemListener.onDetail(downloadedRecord.title, downloadedRecord.downloadHistoryList);
                    }
                }
            }
        });
    }
}