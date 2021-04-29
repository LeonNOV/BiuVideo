package com.leon.biuvideo.adapters.homeAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.homeBeans.PartitionVideo;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/23
 * @Desc 子分区数据适配器
 */
public class PartitionAdapter extends BaseAdapter<PartitionVideo> {
    private final List<PartitionVideo> partitionVideoList;

    public PartitionAdapter(List<PartitionVideo> beans, Context context) {
        super(beans, context);

        this.partitionVideoList = beans;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.video_item_single_column;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        PartitionVideo partitionVideo = partitionVideoList.get(position);

        holder
                .setImage(R.id.video_item_cover, partitionVideo.pic, ImagePixelSize.COVER)
                .setText(R.id.video_item_duration, partitionVideo.duration)
                .setText(R.id.video_item_title, partitionVideo.title)
                .setText(R.id.video_item_view, ValueUtils.generateCN(partitionVideo.play))
                .setText(R.id.video_item_danmaku, ValueUtils.generateCN(partitionVideo.danmaku))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SimpleSnackBar.make(v, "点击了第" + position + "个item", SimpleSnackBar.LENGTH_SHORT).show();
                    }
                });
    }
}
