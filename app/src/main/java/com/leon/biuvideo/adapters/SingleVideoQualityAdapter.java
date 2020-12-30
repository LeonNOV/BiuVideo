package com.leon.biuvideo.adapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.videoBean.play.Media;
import com.leon.biuvideo.ui.dialogs.SingleVideoQualityDialog;
import com.leon.biuvideo.ui.dialogs.WarnDialog;

import java.util.List;
import java.util.Map;

/**
 * 缓存视频时，选择清晰度所需要的适配器
 */
public class SingleVideoQualityAdapter extends BaseAdapter<Map.Entry<Integer, Media>> {
    private final List<Map.Entry<Integer, Media>> videoEntrys;
    private final Context context;

    public SingleVideoQualityAdapter(List<Map.Entry<Integer, Media>> videoEntrys, Context context) {
        super(videoEntrys, context);

        this.videoEntrys = videoEntrys;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.single_video_quality_dialog_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Map.Entry<Integer, Media> mediaEntry = videoEntrys.get(position);
        holder
                .setVisibility(R.id.single_video_quality_item_isDownloaded, mediaEntry.getValue().isDownloaded ? View.VISIBLE : View.INVISIBLE)
                .setText(R.id.single_video_quality_item_quality, mediaEntry.getValue().quality)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 检查是否已下载过该媒体资源
                        if (mediaEntry.getValue().isDownloaded) {
                            WarnDialog warnDialog = new WarnDialog(context, "提示", "检测到本地已存在该视频，是否要覆盖本地资源文件？");
                            warnDialog.setOnConfirmListener(new WarnDialog.OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    SingleVideoQualityDialog.onQualityItemListener.onItemClickListener(mediaEntry);
                                    warnDialog.dismiss();
                                }
                            });
                            warnDialog.show();
                        } else {
                            SingleVideoQualityDialog.onQualityItemListener.onItemClickListener(mediaEntry);
                        }
                    }
                });
    }
}
