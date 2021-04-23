package com.leon.biuvideo.adapters.otherAdapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoInfo;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/20
 * @Desc 视频选集适配器
 */
public class VideoAnthologyAdapter extends BaseAdapter<VideoInfo.AnthologyInfo> {
    private final List<VideoInfo.AnthologyInfo> anthologyInfoList;
    private final int currentIndex;

    private OnVideoAnthologyListener onVideoAnthologyListener;

    public VideoAnthologyAdapter(int currentIndex, List<VideoInfo.AnthologyInfo> beans, Context context) {
        super(beans, context);
        this.currentIndex = currentIndex;
        this.anthologyInfoList = beans;
    }

    public interface OnVideoAnthologyListener {
        /**
         * 视频选集点击事件
         *
         * @param position position
         */
        void onVideoAnthology (int position);
    }

    public void setOnVideoAnthologyListener(OnVideoAnthologyListener onVideoAnthologyListener) {
        this.onVideoAnthologyListener = onVideoAnthologyListener;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.video_anthology_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        VideoInfo.AnthologyInfo anthologyInfo = anthologyInfoList.get(position);

        holder.setText(R.id.video_anthology_item_name, anthologyInfo.part);
        TextView videoAnthologyItemBadge = holder.findById(R.id.video_anthology_item_badge);
        if (anthologyInfo.badge != null) {
            videoAnthologyItemBadge.setText(anthologyInfo.badge);
        } else {
            videoAnthologyItemBadge.setVisibility(View.GONE);
        }

        View itemView = holder.getItemView();
        itemView.setSelected(currentIndex == position);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex == position) {
                    return;
                }

                if (onVideoAnthologyListener != null) {
                    onVideoAnthologyListener.onVideoAnthology(position);
                }
            }
        });
    }
}
