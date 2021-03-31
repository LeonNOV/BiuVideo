package com.leon.biuvideo.adapters.discover.searchResultAdapters;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.searchResultBeans.SearchResultVideo;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/30
 * @Desc 视频搜索结果适配器
 */
public class SearchResultVideoAdapter extends BaseAdapter<SearchResultVideo> {
    private final List<SearchResultVideo> searchResultVideoList;

    public SearchResultVideoAdapter(List<SearchResultVideo> beans, Context context) {
        super(beans, context);

        this.searchResultVideoList = beans;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.search_result_item_video;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        SearchResultVideo searchResultVideo = searchResultVideoList.get(position);

        holder
                .setImage(R.id.search_result_item_video_cover, searchResultVideo.cover, ImagePixelSize.COVER)
                .setText(R.id.search_result_item_video_duration, searchResultVideo.duration)
                .setText(R.id.search_result_item_video_title, Html.fromHtml(searchResultVideo.title, Html.FROM_HTML_OPTION_USE_CSS_COLORS))
                .setText(R.id.search_result_item_video_userName, searchResultVideo.userName)
                .setText(R.id.search_result_item_video_view, searchResultVideo.play)
                .setText(R.id.search_result_item_video_danmaku, searchResultVideo.danmaku)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
    }
}
