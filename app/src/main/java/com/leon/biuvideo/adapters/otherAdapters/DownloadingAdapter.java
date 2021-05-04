package com.leon.biuvideo.adapters.otherAdapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.greendao.dao.DownloadHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/5/4
 * @Desc 下载中资源适配器
 */
public class DownloadingAdapter extends BaseAdapter<DownloadHistory> {
    private final List<DownloadHistory> downloadHistoryList;
    private List<ImageView> downloadingItemSelectList;

    public DownloadingAdapter(List<DownloadHistory> beans, Context context) {
        super(beans, context);
        this.downloadHistoryList = beans;
        downloadingItemSelectList = new ArrayList<>(beans.size());
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.downloading_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        DownloadHistory downloadHistory = downloadHistoryList.get(position);

        ImageView downloadingItemSelect = holder.findById(R.id.downloading_item_select);
        downloadingItemSelectList.add(downloadingItemSelect);

    }

    /**
     * 显示/隐藏 所有选择控件
     */
    public void showAllSelect () {
        if (downloadingItemSelectList.size() > 0) {
            int visibility = downloadingItemSelectList.get(0).getVisibility();

            for (ImageView imageView : downloadingItemSelectList) {
                if (visibility == View.VISIBLE) {
                    imageView.setVisibility(View.GONE);
                } else if (visibility == View.GONE) {
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * 删除所有已选择的item
     */
    public void removeAllSelected () {
        for (int i = 0; i < downloadingItemSelectList.size(); i++) {
            if (downloadingItemSelectList.get(i).isSelected()) {
                remove(downloadHistoryList.get(i));
            }
        }
    }
}
