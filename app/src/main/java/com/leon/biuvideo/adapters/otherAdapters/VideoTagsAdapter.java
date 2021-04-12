package com.leon.biuvideo.adapters.otherAdapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoTag;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/6
 * @Desc 视频Tag适配器
 */
public class VideoTagsAdapter extends BaseAdapter<VideoTag> {
    private final List<VideoTag> videoTagList;

    public VideoTagsAdapter(List<VideoTag> beans, Context context) {
        super(beans, context);

        this.videoTagList = beans;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.video_tag_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        VideoTag videoTag = videoTagList.get(position);

        TextView videoTagView = holder.findById(R.id.video_tag_name);
        videoTagView.setText(videoTag.tagName);

        ImageView videoTagIcon = holder.findById(R.id.video_tag_icon);
        if (videoTag.color != null) {
            videoTagView.setTextColor(context.getColor(R.color.tagColor));
        } else {
            videoTagIcon.setVisibility(View.GONE);
        }
    }
}
