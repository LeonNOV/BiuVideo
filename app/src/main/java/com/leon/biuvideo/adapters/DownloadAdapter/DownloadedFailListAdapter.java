package com.leon.biuvideo.adapters.DownloadAdapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedDetailMedia;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.utils.downloadUtils.MediaUtils;
import com.leon.biuvideo.utils.downloadUtils.ResourceUtils;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.values.Tables;

import java.util.List;

public class DownloadedFailListAdapter extends BaseAdapter<DownloadedDetailMedia> {
    private final Context context;
    private final List<DownloadedDetailMedia> downloadedDetailMedias;
    private DownloadedDetailMedia downloadedDetailMedia;

    public DownloadedFailListAdapter(Context context, List<DownloadedDetailMedia> downloadedDetailMedias) {
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
        int videoIcon = R.drawable.icon_rectangle_video;
        int audioIcon = R.drawable.icon_rectangle_music;

        downloadedDetailMedia = downloadedDetailMedias.get(position);

        ImageView imageView = holder.findById(R.id.downloaded_item_detail_imageView_state);
        holder
                .setImage(R.id.downloaded_item_detail_imageView_mark, downloadedDetailMedia.isVideo ? videoIcon : audioIcon)
                .setImage(R.id.downloaded_item_detail_imageView_cover, downloadedDetailMedia.cover, ImagePixelSize.COVER)
                .setText(R.id.downloaded_item_detail_textView_title, downloadedDetailMedia.title)
                .setText(R.id.downloaded_item_detail_textView_mediaInfo, ValueFormat.sizeFormat(downloadedDetailMedia.size, true))
                .setVisibility(R.id.downloaded_item_detail_imageView_state, View.VISIBLE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 重新对该资源进行下载
                        if (downloadedDetailMedia.downloadState == 0) {
                            reacquire(downloadedDetailMedia, new MediaUtils(context));
                            downloadedDetailMedia.downloadState = 1;

                            holder.setImage(R.id.downloaded_item_detail_imageView_state, R.drawable.downloading_animation);
                            AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
                            animationDrawable.start();

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    MediaUtils mediaUtils = new MediaUtils(context);

                                    if (downloadedDetailMedia.isVideo) {
                                        mediaUtils.saveVideo(downloadedDetailMedia.videoUrl, downloadedDetailMedia.audioUrl, downloadedDetailMedia.fileName);
                                    } else {
                                        mediaUtils.saveMusic(downloadedDetailMedia.audioUrl, downloadedDetailMedia.fileName);
                                    }
                                }
                            }).start();
                        } else {
                            Toast.makeText(context, "该资源正在下载中", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void refresh(String filename) {
        SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(context, Tables.DownloadDetailsForVideo);
        DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils = (DownloadRecordsDatabaseUtils) sqLiteHelperFactory.getInstance();

        DownloadedDetailMedia downloadedDetailMedia = downloadRecordsDatabaseUtils.queryAudioByFileName(filename);
        this.downloadedDetailMedias.remove(downloadedDetailMedia);

        downloadRecordsDatabaseUtils.close();
        notifyDataSetChanged();
    }

    /**
     * 检查原资源链接是否已失效
     *
     * @return  返回是否有效
     */
    private boolean checkUrlState(String videoUrl, String audioUrl) {
        if (videoUrl != null) {
            return ResourceUtils.getResourcesSize(videoUrl) + ResourceUtils.getResourcesSize(audioUrl) > 0;
        } else {
            return ResourceUtils.getResourcesSize(audioUrl) > 0;
        }
    }

    /**
     * 重新获取资源链接
     *
     * @param downloadedDetailMedia downloadedDetailMedia对象
     * @param mediaUtils    mediaUtils
     */
    private void reacquire(DownloadedDetailMedia downloadedDetailMedia, MediaUtils mediaUtils) {
        boolean urlState = checkUrlState(downloadedDetailMedia.videoUrl, downloadedDetailMedia.audioUrl);
        if (!urlState) {
            String[] newUrls = mediaUtils.reacquireMediaUrl(downloadedDetailMedia.mainId, downloadedDetailMedia.subId, downloadedDetailMedia.qualityId);
            downloadedDetailMedia.videoUrl = newUrls[0];

            if (newUrls.length == 2) {
                downloadedDetailMedia.audioUrl = newUrls[1];
            }

            //更新本地链接
            SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(context, Tables.DownloadDetailsForVideo);
            DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils = (DownloadRecordsDatabaseUtils) sqLiteHelperFactory.getInstance();
            downloadRecordsDatabaseUtils.updateUrl(newUrls, downloadedDetailMedia.fileName);
            downloadRecordsDatabaseUtils.close();
        }
    }
}
