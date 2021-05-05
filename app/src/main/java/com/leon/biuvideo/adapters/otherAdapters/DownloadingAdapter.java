package com.leon.biuvideo.adapters.otherAdapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadEntity;
import com.arialyy.aria.core.download.DownloadReceiver;
import com.arialyy.aria.core.download.target.HttpNormalTarget;
import com.arialyy.aria.core.task.DownloadTask;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.greendao.dao.DownloadHistory;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @Author Leon
 * @Time 2021/5/4
 * @Desc 下载中资源适配器
 */
public class DownloadingAdapter extends BaseAdapter<DownloadHistory> {
    private final List<DownloadHistory> downloadHistoryList;
    private final List<ImageView> downloadingItemSelectList;

    private boolean editState = false;
    private final ScheduledExecutorService executorService;

    public DownloadingAdapter(List<DownloadHistory> beans, Context context) {
        super(beans, context);
        this.downloadHistoryList = beans;
        downloadingItemSelectList = new ArrayList<>(beans.size());

        // 参数corePoolSize和aria配置文件中的最大的任务数相同
        executorService = new ScheduledThreadPoolExecutor(5, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "downloadingTimer");
            }
        });
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.downloading_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        DownloadHistory downloadHistory = downloadHistoryList.get(position);


        DownloadEntity downloadEntity = Aria.download(context).getDownloadEntity(downloadHistory.getTaskId());

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
        TextView downloadingItemProgress = holder.findById(R.id.downloading_item_progress);
        TextView downloadingItemNowSize = holder.findById(R.id.downloading_item_now_size);
        ProgressBar downloadingItemProgressBar = holder.findById(R.id.downloading_item_progressBar);
        TextView downloadingItemNowSpeed = holder.findById(R.id.downloading_item_now_speed);

        addTimer(downloadEntity, downloadingItemNowSpeed, downloadingItemProgress, downloadingItemNowSize, downloadingItemProgressBar);
    }

    /**
     * 添加计时器，用以更新下载进度、百分比进度、已下载大小
     *
     * @param downloadEntity    DownloadEntity
     * @param downloadingItemNowSpeed   当前下载速度
     * @param downloadingItemProgress   百分比进度
     * @param downloadingItemNowSize    当前已下载大小
     * @param downloadingItemProgressBar    下载进度条
     */
    private void addTimer(DownloadEntity downloadEntity, TextView downloadingItemNowSpeed,
                          TextView downloadingItemProgress, TextView downloadingItemNowSize,
                          ProgressBar downloadingItemProgressBar) {

        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (downloadEntity.getState() == DownloadEntity.STATE_RUNNING) {
                    String convertSpeed = downloadEntity.getConvertSpeed().replace("mb", "M");
                    downloadingItemNowSpeed.setText(convertSpeed);

                    int percent = downloadEntity.getPercent();

                    String percentStr = percent + "%";
                    downloadingItemProgress.setText(percentStr);
                    downloadingItemProgressBar.setProgress(percent);

                    String fileSize = downloadEntity.getConvertFileSize().replace("mb", "M");
                    long currentFileSize = downloadEntity.getCurrentProgress();

                    double currentFileSizeMb = ((double) (currentFileSize / 1000) / 1000);

                    String currentFileSizeMbStr = fileSize + "/" + currentFileSizeMb + "M";
                    downloadingItemNowSize.setText(currentFileSizeMbStr);
                }
            }
        }, 100, 500, TimeUnit.MILLISECONDS);
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

            downloadingItemSelectList.remove(imageView);
            remove(downloadHistory);
        }
    }
}
