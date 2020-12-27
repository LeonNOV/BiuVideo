package com.leon.biuvideo.adapters.downloadAdapter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedRecordsForVideo;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.List;

public class MediaListAdapter extends BaseAdapter<DownloadedRecordsForVideo> {
    private final List<DownloadedRecordsForVideo> downloadRecords;

    public MediaListAdapter(List<DownloadedRecordsForVideo> downloadRecords, Context context) {
        super(downloadRecords, context);
        this.downloadRecords = downloadRecords;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.downloaded_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        DownloadedRecordsForVideo downloadRecord = downloadRecords.get(position);

        holder
                .setImage(R.id.downloaded_item_imageView_cover, downloadRecord.cover, ImagePixelSize.COVER)
                .setText(R.id.downloaded_item_textView_title, downloadRecord.title);

        holder.setText(R.id.downloaded_item_textView_count, 0 + "P");
    }
}
