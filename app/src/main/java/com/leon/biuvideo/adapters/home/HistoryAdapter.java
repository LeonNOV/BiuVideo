package com.leon.biuvideo.adapters.home;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;

import java.util.List;

public class HistoryAdapter extends BaseAdapter<String[]> {
    private final List<String[]> stringArrays;

    public HistoryAdapter(List<String[]> beans, Context context) {
        super(beans, context);
        this.stringArrays = beans;
    }

    @Override
    public int getLayout(int viewType) {
        switch (viewType) {
            case 0:
                return R.layout.history_item_video;
            case 1:
                return R.layout.history_item_bangumi;
            case 2:
                return R.layout.history_item_article;
            default:
                return R.layout.history_item_series;
        }
    }

    @Override
    public int getItemViewType(int position) {
        String[] strings = stringArrays.get(position);
        switch (strings[0]) {
            case "video":
                return 0;
            case "bangumi":
                return 1;
            case "article":
                return 2;
            default:
                return 3;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        String[] strings = stringArrays.get(position);
        switch (strings[0]) {
            case "video":
                initVideoItemView(strings, holder, position);
                break;
            case "bangumi":
                initBangumiItemView(strings, holder, position);
                break;
            case "article":
                initArticleItemView(strings, holder, position);
                break;
            default:
                initSeriesItemView(strings, holder, position);
                break;
        }
    }

    /**
     * 初始化video条目
     *  @param strings   数据
     * @param holder    holder
     * @param position  position
     */
    private void initVideoItemView(String[] strings, BaseViewHolder holder, int position) {
        holder
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Position:" + position + "----Type:Video", Toast.LENGTH_SHORT).show();
                    }
                })
                .setText(R.id.history_item_video_type, strings[0])
                .setText(R.id.history_item_video_desc, strings[1]);
    }

    /**
     * 初始化bangumi条目
     *  @param strings   数据
     * @param holder    holder
     * @param position  position
     */
    private void initBangumiItemView(String[] strings, BaseViewHolder holder, int position) {
        holder
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Position:" + position + "----Type:Bangumi", Toast.LENGTH_SHORT).show();
                    }
                })
                .setText(R.id.history_item_bangumi_type, strings[0])
                .setText(R.id.history_item_bangumi_desc, strings[1]);
    }

    /**
     * 初始化article条目
     *  @param strings   数据
     * @param holder    holder
     * @param position  position
     */
    private void initArticleItemView(String[] strings, BaseViewHolder holder, int position) {
        holder
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Position:" + position + "----Type:Article", Toast.LENGTH_SHORT).show();
                    }
                })
                .setText(R.id.history_item_article_type, strings[0])
                .setText(R.id.history_item_article_desc, strings[1]);
    }

    /**
     * 初始化series条目
     *  @param strings   数据
     * @param holder    holder
     * @param position  position
     */
    private void initSeriesItemView(String[] strings, BaseViewHolder holder, int position) {
        holder
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Position:" + position + "----Type:Series", Toast.LENGTH_SHORT).show();
                    }
                })
                .setText(R.id.history_item_series_type, strings[0])
                .setText(R.id.history_item_series_desc, strings[1]);
    }
}
