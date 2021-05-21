package com.leon.biuvideo.adapters.otherAdapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoInfo;
import com.leon.biuvideo.ui.resourcesFragment.video.OnBottomSheetWithItemListener;
import com.leon.biuvideo.utils.InternetUtils;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/20
 * @Desc 视频选集适配器
 */
public class VideoAnthologyAdapter extends BaseAdapter<VideoInfo.VideoAnthology> {
    private final List<VideoInfo.VideoAnthology> videoAnthologyList;
    private final int currentIndex;

    private OnBottomSheetWithItemListener onBottomSheetWithItemListener;

    public VideoAnthologyAdapter(int currentIndex, List<VideoInfo.VideoAnthology> beans, Context context) {
        super(beans, context);
        this.currentIndex = currentIndex;
        this.videoAnthologyList = beans;
    }

    public void setOnBottomSheetWithItemListener(OnBottomSheetWithItemListener onBottomSheetWithItemListener) {
        this.onBottomSheetWithItemListener = onBottomSheetWithItemListener;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.video_anthology_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        VideoInfo.VideoAnthology videoAnthology = videoAnthologyList.get(position);

        holder.setText(R.id.video_anthology_item_name, videoAnthology.subTitle);
        TextView videoAnthologyItemBadge = holder.findById(R.id.video_anthology_item_badge);
        if (videoAnthology.badge != null) {
            videoAnthologyItemBadge.setText(videoAnthology.badge);
        } else {
            videoAnthologyItemBadge.setVisibility(View.GONE);
        }

        View itemView = holder.getItemView();
        itemView.setSelected(currentIndex == position);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetUtils.checkNetwork(v)) {
                    if (currentIndex == position) {
                        return;
                    }

                    if (onBottomSheetWithItemListener != null) {
                        onBottomSheetWithItemListener.onItem(position);
                    }
                }
            }
        });
    }
}
