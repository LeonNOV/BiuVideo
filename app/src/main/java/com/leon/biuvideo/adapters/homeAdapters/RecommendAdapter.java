package com.leon.biuvideo.adapters.homeAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.resourcesBeans.VideoRecommend;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.FragmentType;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/10
 * @Desc 推荐视频适配器
 */
public class RecommendAdapter extends BaseAdapter<VideoRecommend> {
    public static final int SINGLE_COLUMN = 1;
    public static final int DOUBLE_COLUMN = 2;

    private final MainActivity mainActivity;
    private final List<VideoRecommend> videoRecommendList;
    private final int columns;

    public RecommendAdapter(List<VideoRecommend> beans, int columns, MainActivity mainActivity, Context context) {
        super(beans, context);

        this.columns = columns;
        this.videoRecommendList = beans;
        this.mainActivity = mainActivity;
    }

    @Override
    public int getLayout(int viewType) {
        if (columns == SINGLE_COLUMN) {
            return R.layout.video_item_single_column;
        }

        return R.layout.video_item_double_column;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        VideoRecommend videoRecommend = videoRecommendList.get(position);

        holder
                .setImage(R.id.video_item_cover, videoRecommend.cover, ImagePixelSize.COVER)
                .setText(R.id.video_item_duration, videoRecommend.duration)
                .setText(R.id.video_item_view, ValueUtils.generateCN(videoRecommend.view))
                .setText(R.id.video_item_danmaku, ValueUtils.generateCN(videoRecommend.danmaku))
                .setText(R.id.video_item_title, videoRecommend.title)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (InternetUtils.checkNetwork(v)) {
                            startPublicFragment(mainActivity, FragmentType.VIDEO, videoRecommend.bvid);
                        }
                    }
                });
    }
}
