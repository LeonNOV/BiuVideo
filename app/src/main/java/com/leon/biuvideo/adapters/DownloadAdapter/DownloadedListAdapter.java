package com.leon.biuvideo.adapters.DownloadAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedDetailMedia;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedRecordsForVideo;
import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.values.Tables;

import java.util.List;

/**
 * 视频下载列表适配器
 */
public class DownloadedListAdapter extends BaseAdapter<DownloadedRecordsForVideo> {
    private final List<DownloadedRecordsForVideo> downloadRecords;
    private final Context context;

    public DownloadedListAdapter(List<DownloadedRecordsForVideo> downloadRecords, Context context) {
        super(downloadRecords, context);
        this.downloadRecords = downloadRecords;
        this.context = context;
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
                .setText(R.id.downloaded_item_textView_title, downloadRecord.title)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(context, Tables.DownloadDetailsForVideo);
                        DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils = (DownloadRecordsDatabaseUtils) sqLiteHelperFactory.getInstance();

                        List<DownloadedDetailMedia> downloadedDetailMedia = downloadRecordsDatabaseUtils.queryAllSubVideo(downloadRecord.mainId);

                        if (downloadRecord.count == 1) {
                            String folderPath = FileUtils.createFolder(FileUtils.ResourcesFolder.VIDEOS);
                            String path = folderPath + "/" + downloadedDetailMedia.get(0).fileName + ".mp4";

                            Intent intentVideoPlayer = new Intent(Intent.ACTION_VIEW);
                            Uri uri = Uri.parse(path);
                            intentVideoPlayer.setDataAndType(uri , "video/mp4");
                            context.startActivity(intentVideoPlayer);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("mainId", downloadRecord.mainId);
                            Navigation.findNavController(v).navigate(R.id.action_downloadedVideoListFragment_to_downloadedVideoDetailFragment, bundle);
                        }
                    }
                });

        if (downloadRecord.count > 1) {
            holder
                    .setVisibility(R.id.downloaded_item_textView_count, View.VISIBLE)
                    .setText(R.id.downloaded_item_textView_count, downloadRecord.count + "P");
        }
    }
}
