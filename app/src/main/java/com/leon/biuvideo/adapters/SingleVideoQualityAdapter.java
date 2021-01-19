package com.leon.biuvideo.adapters;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.videoBean.play.Media;

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

    private OnSingleVideoQualityClickListener onSingleVideoQualityClickListener;

    public interface OnSingleVideoQualityClickListener {
        void onClickListener(Map.Entry<Integer, Media> mediaEntry);
    }

    public void setOnSingleVideoQualityClickListener(OnSingleVideoQualityClickListener onSingleVideoQualityClickListener) {
        this.onSingleVideoQualityClickListener = onSingleVideoQualityClickListener;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.single_video_quality_dialog_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Map.Entry<Integer, Media> mediaEntry = videoEntrys.get(position);
        Media media = mediaEntry.getValue();

        holder
                .setVisibility(R.id.single_video_quality_item_isDownloaded, media.isDownloaded ? View.VISIBLE : View.INVISIBLE)
                .setText(R.id.single_video_quality_item_quality, media.quality)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 检查是否已下载过该媒体资源
                        if (media.isDownloaded) {
                            Snackbar.make(v, "该资源已缓存过了", Snackbar.LENGTH_SHORT).show();
                        } else {
                            if (onSingleVideoQualityClickListener != null) {
                                onSingleVideoQualityClickListener.onClickListener(mediaEntry);
                            }
                        }
                    }
                });
    }
}
