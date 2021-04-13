package com.leon.biuvideo.adapters.biliUserResourcesAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.biliUserResourcesBeans.BiliUserVideo;
import com.leon.biuvideo.values.FragmentType;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/10
 * @Desc B站用户投稿视频适配器
 */
public class BiliUserVideosAdapter extends BaseAdapter<BiliUserVideo> {
    private final List<BiliUserVideo> biliUserVideoList;

    public BiliUserVideosAdapter(List<BiliUserVideo> beans, Context context) {
        super(beans, context);
        this.biliUserVideoList = beans;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.bili_user_video_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        BiliUserVideo biliUserVideo = biliUserVideoList.get(position);

        holder
                .setImage(R.id.bili_user_video_item_cover, biliUserVideo.cover, ImagePixelSize.COVER)
                .setText(R.id.bili_user_video_item_duration, biliUserVideo.duration)
                .setText(R.id.bili_user_video_item_title, biliUserVideo.title)
                .setText(R.id.bili_user_video_item_pubTime, biliUserVideo.pubTime)
                .setText(R.id.bili_user_video_item_view, biliUserVideo.play)
                .setText(R.id.bili_user_video_item_danmaku, biliUserVideo.danmaku)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startPublicFragment(FragmentType.VIDEO, biliUserVideo.bvid);
                    }
                });
    }
}
