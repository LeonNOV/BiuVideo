package com.leon.biuvideo.adapters.downloadAdapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedDetailMedia;
import com.leon.biuvideo.utils.SimpleDownloadThread;
import com.leon.biuvideo.utils.SimpleThreadPool;
import com.leon.biuvideo.utils.ValueFormat;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;
import com.leon.biuvideo.utils.downloadUtils.MediaUtils;
import com.leon.biuvideo.utils.downloadUtils.ResourceUtils;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * 下载失败列表适配器
 */
public class DownloadedFailListAdapter extends BaseAdapter<DownloadedDetailMedia> {
    private final Context context;
    private final List<DownloadedDetailMedia> downloadedDetailMedias;

    private SimpleThreadPool simpleThreadPool;

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

        DownloadedDetailMedia downloadedDetailMedia = downloadedDetailMedias.get(position);

        holder
                .setImage(R.id.downloaded_item_detail_imageView_mark, downloadedDetailMedia.isVideo ? videoIcon : audioIcon)
                .setImage(R.id.downloaded_item_detail_imageView_cover, downloadedDetailMedia.cover, ImagePixelSize.COVER)
                .setText(R.id.downloaded_item_detail_textView_title, downloadedDetailMedia.title)
                .setText(R.id.downloaded_item_detail_textView_mediaInfo, ValueFormat.sizeFormat(downloadedDetailMedia.size, true))
                .setVisibility(R.id.downloaded_item_detail_imageView_error, View.VISIBLE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 重新对该资源进行下载
                        if (downloadedDetailMedia.downloadState == 0) {
                            reacquire(downloadedDetailMedia, new MediaUtils(context));
                            downloadedDetailMedia.downloadState = 1;

                            holder
                                    .setVisibility(R.id.downloaded_item_detail_imageView_downloading, View.VISIBLE)
                                    .setVisibility(R.id.downloaded_item_detail_imageView_error, View.GONE);

                            if (simpleThreadPool == null) {
                                simpleThreadPool = new SimpleThreadPool(SimpleThreadPool.DownloadTaskNum, SimpleThreadPool.DownloadTask);
                            }

                            if (downloadedDetailMedia.isVideo) {
                                simpleThreadPool.submit(new FutureTask<>(new SimpleDownloadThread(context, downloadedDetailMedia.videoUrl, downloadedDetailMedia.audioUrl, downloadedDetailMedia.fileName)));
                            } else {
                                simpleThreadPool.submit(new FutureTask<>(new SimpleDownloadThread(context, downloadedDetailMedia.audioUrl, downloadedDetailMedia.fileName)));
                            }
                        } else {
                            Snackbar.make(v, "该资源正在下载中", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void refresh(String filename) {
        int index = -1;
        for (int i = 0; i < this.downloadedDetailMedias.size(); i++) {
            if (this.downloadedDetailMedias.get(i).fileName.equals(filename)) {
                index = i;
            }
        }

        if (index != -1) {
            this.downloadedDetailMedias.remove(index);
        }

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
            DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils = new DownloadRecordsDatabaseUtils(context);
            downloadRecordsDatabaseUtils.updateUrl(newUrls, downloadedDetailMedia.fileName);
            downloadRecordsDatabaseUtils.close();
        }
    }
}
