package com.leon.biuvideo.adapters.otherAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.greendao.dao.DownloadHistory;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.downloadUtils.ResourceDownloadTask;
import com.leon.biuvideo.values.ImagePixelSize;

import org.jetbrains.annotations.NotNull;

/**
 * @Author Leon
 * @Time 2021/5/14
 * @Desc 已下载多选集详细适配器
 */
public class DownloadedDetailAdapter extends BaseAdapter<DownloadHistory> {
    public DownloadedDetailAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.downloaded_item;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BaseViewHolder holder, int position) {
        DownloadHistory downloadHistory = getAllData().get(position);

        holder.setImage(R.id.downloaded_item_cover, downloadHistory.getCoverUrl(), ImagePixelSize.COVER)
                .setText(R.id.downloaded_item_title, downloadHistory.getSubTitle())
                .setText(R.id.downloaded_size, ValueUtils.sizeFormat(downloadHistory.getFileSize(), true));

        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int resType = downloadHistory.getResType();
                if (resType == ResourceDownloadTask.RES_TYPE_VIDEO) {
                    Intent intentVideoPlayer = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse(downloadHistory.getSavePath());
                    intentVideoPlayer.setDataAndType(uri, "video/x-flv");
                    context.startActivity(intentVideoPlayer);
                } else if (resType == ResourceDownloadTask.RES_TYPE_AUDIO) {
                    Fuck.red("savePath----" + downloadHistory.getSavePath());
                }
            }
        });
    }
}
