package com.leon.biuvideo.adapters.otherAdapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadEntity;
import com.arialyy.aria.core.download.target.HttpNormalTarget;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.greendao.dao.DaoBaseUtils;
import com.leon.biuvideo.greendao.dao.DownloadHistory;
import com.leon.biuvideo.greendao.daoutils.DownloadHistoryUtils;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/5/4
 * @Desc 下载中资源适配器
 */
public class DownloadingAdapter extends BaseAdapter<DownloadHistory> {
    private final List<DownloadHistory> downloadHistoryList;
    private final List<ImageView> downloadingItemSelectList;

    private boolean editState = false;

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
        downloadingItemSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadingItemSelect.setSelected(!downloadingItemSelect.isSelected());
            }
        });
        downloadingItemSelectList.add(downloadingItemSelect);

        holder
                .setImage(R.id.downloading_item_cover, downloadHistory.getCoverUrl(), ImagePixelSize.COVER)
                .setText(R.id.downloading_item_title, downloadHistory.getTitle())
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 如果处于编辑模式，则点击条目只会对选择控件做出变化
                        if (editState) {
                            downloadingItemSelect.setSelected(!downloadingItemSelect.isSelected());
                        } else {
                            // 暂停/继续 下载
                            HttpNormalTarget task = Aria.download(context).load(downloadHistory.getTaskId());
                            boolean running = task.isRunning();
                            if (running) {
                                task.stop();
                            } else {
                                task.resume();
                            }
                        }
                    }
                });

        DownloadEntity downloadEntity = Aria.download(context).getDownloadEntity(downloadHistory.getTaskId());
        String currentFileSizeAndCount = ((double) (downloadEntity.getCurrentProgress() / 1000 / 1000)) + "M/" +
                (downloadEntity.getConvertFileSize().replace("mb", "M"));
        holder.setText(R.id.downloading_item_current_size, currentFileSizeAndCount);
        ((ImageView)holder.findById(R.id.downloading_item_stat)).setImageLevel(1);
    }

    /**
     * 显示/隐藏 所有选择控件
     */
    public void showAllSelect () {
        if (downloadingItemSelectList.size() > 0) {
            int visibility = downloadingItemSelectList.get(0).getVisibility();

            this.editState = visibility != View.VISIBLE;

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
     * 选中/取消选中 所有
     */
    public void selectedAll (boolean selectedState) {
        for (ImageView imageView : downloadingItemSelectList) {
            imageView.setSelected(selectedState);
        }
    }

    /**
     * 删除所有已选中的
     */
    public void removeAllSelected () {
        DownloadHistoryUtils downloadHistoryUtils = new DownloadHistoryUtils(context);
        DaoBaseUtils<DownloadHistory> downloadHistoryDaoUtils = downloadHistoryUtils.getDownloadHistoryDaoUtils();

        List<ImageView> selectedList = new ArrayList<>();

        for (int i = 0; i < downloadingItemSelectList.size(); i++) {
            ImageView imageView = downloadingItemSelectList.get(i);

            if (imageView.isSelected()) {
                selectedList.add(imageView);
            }
        }

        for (ImageView imageView : selectedList) {
            int index = downloadingItemSelectList.indexOf(imageView);
            DownloadHistory downloadHistory = downloadHistoryList.get(index);

            // 删除任务
            Aria.download(context).load(downloadHistory.getTaskId()).removeRecord();
            downloadHistoryDaoUtils.delete(downloadHistory);

            downloadingItemSelectList.remove(imageView);
            remove(downloadHistory);
        }
    }
}
