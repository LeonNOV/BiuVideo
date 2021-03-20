package com.leon.biuvideo.adapters.homeAdapters;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.orderBeans.Tag;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/15
 * @Desc 标签数据解析类
 */
public class TagAdapter extends BaseAdapter<Tag> {
    private final Context context;
    private final List<Tag> tags;

    public TagAdapter(List<Tag> tags, Context context) {
        super(tags, context);
        this.context = context;
        this.tags = tags;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.tag_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Tag tag = tags.get(position);

        if (tag.tagCover != null) {
            holder.setImage(R.id.tag_item_cover, tag.tagCover, ImagePixelSize.COVER);
        }

        holder.setText(R.id.tag_item_name, tag.tagName)
                .setOnClickListener(R.id.tag_item_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, tag.tagName, Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnClickListener(R.id.tag_item_cancel_order, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "取消订阅：" + tag.tagName, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
