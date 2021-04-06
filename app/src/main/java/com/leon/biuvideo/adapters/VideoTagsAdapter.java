package com.leon.biuvideo.adapters;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.mediaBeans.videoBeans.VideoTag;

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

        TextView videoTagView = holder.findById(R.id.video_tag);
        videoTagView.setText(videoTag.tagName);

        if (videoTag.color != null) {
            videoTagView.setCompoundDrawables(context.getDrawable(R.drawable.tag_default), null, null, null);
//            videoTagView.setCompoundDrawablePadding(5);
            videoTagView.setTextColor(context.getColor(R.color.tagColor));
        }
    }
}
