package com.leon.biuvideo.adapters.home;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.homeBeans.Recommend;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/10
 * @Desc 推荐视频适配器
 */
public class RecommendAdapter extends BaseAdapter<Recommend> {
    public static final int SINGLE_ROW = 1;
    public static final int DOUBLE_ROW = 2;

    private final List<Recommend> recommendList;
    private final int rows;

    public RecommendAdapter(List<Recommend> beans, int rows, Context context) {
        super(beans, context);

        this.rows = rows;
        this.recommendList = beans;
    }

    @Override
    public int getLayout(int viewType) {
        if (rows == SINGLE_ROW) {
            return R.layout.video_item_single_row;
        }

        return R.layout.video_item_double_row;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Recommend recommend = recommendList.get(position);
        holder
                .setImage(R.id.video_item_cover, recommend.cover, ImagePixelSize.COVER)
                .setText(R.id.video_item_duration, recommend.duration)
                .setText(R.id.video_item_view, ValueUtils.generateCN(recommend.view))
                .setText(R.id.video_item_danmaku, ValueUtils.generateCN(recommend.danmaku))
                .setText(R.id.video_item_desc, recommend.desc)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "点击了第" + position + "个item，BVID:" + recommend.bvid, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
