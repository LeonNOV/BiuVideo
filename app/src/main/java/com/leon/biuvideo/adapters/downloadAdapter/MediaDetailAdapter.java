package com.leon.biuvideo.adapters.downloadAdapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedDetailMedia;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.List;

public class MediaDetailAdapter extends BaseAdapter<DownloadedDetailMedia> {
    private final List<DownloadedDetailMedia> downloadedDetailMedias;

    public MediaDetailAdapter(List<DownloadedDetailMedia> downloadedDetailMedias, Context context) {
        super(downloadedDetailMedias, context);
        this.downloadedDetailMedias = downloadedDetailMedias;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.downloaded_item_detail;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        DownloadedDetailMedia downloadedDetailMedia = downloadedDetailMedias.get(position);

        holder
                .setImage(R.id.downloaded_item_detail_imageView_cover, downloadedDetailMedia.cover, ImagePixelSize.COVER)
                .setText(R.id.downloaded_item_detail_textView_title, downloadedDetailMedia.title)
                .setText(R.id.downloaded_item_detail_textView_mediaInfo, ValueFormat.sizeFormat(downloadedDetailMedia.size, true));

        if (!downloadedDetailMedia.isComplete) {
            holder.setVisibility(R.id.downloaded_item_detail_imageView_error, View.VISIBLE);
        }
    }
}
