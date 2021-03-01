package com.leon.biuvideo.adapters.downloadAdapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedDetailMedia;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;
import com.leon.biuvideo.utils.downloadUtils.ResourceUtils;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.List;

/**
 * 下载失败列表适配器
 */
public class DownloadedFailListAdapter extends BaseAdapter<DownloadedDetailMedia> {
    private final Context context;
    private final List<DownloadedDetailMedia> downloadedDetailMedias;

    private OnClickFailedItemListener onClickFailedItemListener;

    public interface OnClickFailedItemListener {
        void onClick(int position);
    }

    public void setOnClickFailedItemListener(OnClickFailedItemListener onClickFailedItemListener) {
        this.onClickFailedItemListener = onClickFailedItemListener;
    }

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
                .setText(R.id.downloaded_item_detail_textView_mediaInfo, ValueUtils.sizeFormat(downloadedDetailMedia.size, true))
                .setVisibility(R.id.downloaded_item_detail_imageView_error, downloadedDetailMedia.downloadState == 1 ? View.GONE : View.VISIBLE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (downloadedDetailMedia.downloadState == 0) {
                            reacquire(downloadedDetailMedia);
                            downloadedDetailMedia.downloadState = 1;

                            holder
                                    .setVisibility(R.id.downloaded_item_detail_imageView_downloading, View.VISIBLE)
                                    .setVisibility(R.id.downloaded_item_detail_imageView_error, View.GONE);

                            // 重新对该资源进行下载
                            if (onClickFailedItemListener != null) {
                                onClickFailedItemListener.onClick(position);
                            }
                        } else {
                            SimpleSnackBar.make(v, "该资源正在下载中", SimpleSnackBar.LENGTH_SHORT).show();
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
            return ResourceUtils.getResourcesSize(videoUrl) > 0 && ResourceUtils.getResourcesSize(audioUrl) > 0;
        } else {
            return ResourceUtils.getResourcesSize(audioUrl) > 0;
        }
    }

    /**
     * 重新获取资源链接
     *
     * @param downloadedDetailMedia downloadedDetailMedia对象
     */
    private void reacquire(DownloadedDetailMedia downloadedDetailMedia) {
        boolean urlState = checkUrlState(downloadedDetailMedia.videoUrl, downloadedDetailMedia.audioUrl);
        if (!urlState) {
            String[] newUrls = HttpUtils.reacquireMediaUrl(context, downloadedDetailMedia.mainId, downloadedDetailMedia.subId, downloadedDetailMedia.qualityId, !downloadedDetailMedia.mainId.startsWith("BV"));
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
