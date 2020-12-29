package com.leon.biuvideo.adapters.DownloadAdapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedDetailMedia;
import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.values.Tables;

import java.util.List;

public class DownloadedDetailAdapter extends BaseAdapter<DownloadedDetailMedia> {
    private final Context context;
    private final List<DownloadedDetailMedia> downloadedDetailMedias;

    public DownloadedDetailAdapter(List<DownloadedDetailMedia> downloadedDetailMedias, Context context) {
        super(downloadedDetailMedias, context);
        this.downloadedDetailMedias = downloadedDetailMedias;
        this.context = context;
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
                .setText(R.id.downloaded_item_detail_textView_mediaInfo, ValueFormat.sizeFormat(downloadedDetailMedia.size, true))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String folderPath;
                        folderPath = downloadedDetailMedia.isVideo ? FileUtils.createFolder(FileUtils.ResourcesFolder.VIDEOS) : FileUtils.createFolder(FileUtils.ResourcesFolder.MUSIC);
                        String path = folderPath + "/" + downloadedDetailMedia.fileName + (downloadedDetailMedia.isVideo ? ".mp4" : ".mp3");

                        Intent intentMediaPlayer;
                        Fuck.blue("isVideo:" + downloadedDetailMedia.isVideo + "");
                        if (downloadedDetailMedia.isVideo) {
                            intentMediaPlayer = new Intent(Intent.ACTION_VIEW);
                            Uri uri = Uri.parse(path);
                            intentMediaPlayer.setDataAndType(uri , "video/mp4");
                        } else {
//                            intentMediaPlayer.setDataAndType(uri , "audio/*");
                            intentMediaPlayer = new Intent("android.intent.action.MUSIC_PLAYER");
                        }

                        try {
                            context.startActivity(intentMediaPlayer);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(context, "找不到支持打开该文件格式的应用", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        if (downloadedDetailMedia.downloadState == 1) {
            holder
                    .setVisibility(R.id.downloaded_item_detail_imageView_state, View.VISIBLE)
                    .setImage(R.id.downloaded_item_detail_imageView_state, R.drawable.icon_error2);
        }
    }

    @Override
    public void refresh(String filename) {
        SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(context, Tables.DownloadDetailsForVideo);
        DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils = (DownloadRecordsDatabaseUtils) sqLiteHelperFactory.getInstance();

        DownloadedDetailMedia downloadedDetailMedia = downloadRecordsDatabaseUtils.queryVideoByFileName(filename);
        this.downloadedDetailMedias.add(downloadedDetailMedia);

        downloadRecordsDatabaseUtils.close();
        notifyDataSetChanged();
    }
}
