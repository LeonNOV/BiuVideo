package com.leon.biuvideo.ui.home.downloadManagerFragments;

import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadEntity;
import com.arialyy.aria.core.task.DownloadTask;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.DownloadingAdapter;
import com.leon.biuvideo.greendao.dao.DaoBaseUtils;
import com.leon.biuvideo.greendao.dao.DownloadHistory;
import com.leon.biuvideo.greendao.dao.DownloadHistoryDao;
import com.leon.biuvideo.greendao.daoutils.DownloadHistoryUtils;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.utils.Fuck;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/7
 * @Desc 下载管理页面-下载中的资源
 */
public class DownloadingFragment extends BaseSupportFragment implements View.OnClickListener {
    public static final int DOWNLOAD_STAT_RUNNING = 0;
    public static final int DOWNLOAD_STAT_PAUSE = 1;
    public static final int DOWNLOAD_STAT_ERROR = 2;
    public static final int DOWNLOAD_STAT_WAITING = 3;

    private TextView downloadingAllStat;
    private TextView downloadingEdit;
    private FrameLayout downloadManagerEditContainer;
    private TextView downloadingEditSelectAll;
    private DownloadingAdapter downloadingAdapter;
    private List<DownloadHistory> downloadHistoryList;

    private DaoBaseUtils<DownloadHistory> downloadHistoryDaoUtils;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected int setLayout() {
        return R.layout.downloading_fragment;
    }

    @Override
    protected void initView() {
        // 注册Aria
        Aria.download(this).register();

        DownloadHistoryUtils downloadHistoryUtils = new DownloadHistoryUtils(context);
        downloadHistoryDaoUtils = downloadHistoryUtils.getDownloadHistoryDaoUtils();

        findView(R.id.downloading_container).setBackgroundColor(Color.WHITE);

        downloadingAllStat = findView(R.id.downloading_all_stat);
        downloadingAllStat.setOnClickListener(this);
        downloadingAllStat.setText(R.string.downloadManagerPauseAll);
        downloadingAllStat.setSelected(true);

        downloadingEdit = findView(R.id.downloading_edit);
        downloadingEdit.setOnClickListener(this);

        LoadingRecyclerView downloadingAllRunningTask = findView(R.id.downloading_all_running_task);
        downloadingAllRunningTask.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        downloadManagerEditContainer = findView(R.id.download_manager_edit_container);

        downloadingEditSelectAll = findView(R.id.downloading_edit_select_all);
        downloadingEditSelectAll.setOnClickListener(this);

        TextView downloadingEditRemove = findView(R.id.downloading_edit_remove);
        downloadingEditRemove.setOnClickListener(this);

        downloadHistoryList = downloadHistoryDaoUtils.queryByQueryBuilder(DownloadHistoryDao.Properties.IsCompleted.eq(false));
        downloadingAdapter = new DownloadingAdapter(downloadHistoryList, context);

        downloadingAllRunningTask.setRecyclerViewAdapter(downloadingAdapter);
        linearLayoutManager = new LinearLayoutManager(context);
        downloadingAllRunningTask.setRecyclerViewLayoutManager(linearLayoutManager);

        downloadingAllRunningTask.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
    }

    /**
     * 取消下载
     */
    @Download.onTaskCancel
    void onCancel (DownloadTask downloadTask) {
        DownloadHistory thisHistory = findThisHistory(downloadTask);
        if (thisHistory != null && thisHistory.getIsCompleted()) {
            downloadingAdapter.remove(thisHistory);
            downloadHistoryDaoUtils.delete(thisHistory);
        }
    }

    /**
     * 下载失败
     */
    @Download.onTaskFail
    void onFail (DownloadTask downloadTask) {
        DownloadHistory thisHistory = findThisHistory(downloadTask);

        if (thisHistory != null) {
            updateItemInfo(DOWNLOAD_STAT_ERROR, downloadTask, thisHistory);
            thisHistory.setIsFailed(true);
            downloadHistoryDaoUtils.update(thisHistory);
        }
    }

    /**
     * 下载完成
     */
    @Download.onTaskComplete
    void complete (DownloadTask downloadTask) {
        DownloadHistory thisHistory = findThisHistory(downloadTask);

        if (thisHistory != null && thisHistory.getIsCompleted()) {
            downloadingAdapter.remove(thisHistory);

            thisHistory.setIsCompleted(true);
            downloadHistoryDaoUtils.update(thisHistory);
        }
    }

    /**
     * 下载中
     */
    @Download.onTaskRunning
    void onRunning (DownloadTask downloadTask) {
        DownloadHistory thisHistory = findThisHistory(downloadTask);

        if (thisHistory != null) {
            updateItemInfo(DOWNLOAD_STAT_RUNNING, downloadTask, thisHistory);
        }
    }

    /**
     * 暂停下载
     */
    @Download.onTaskStop
    void onStop (DownloadTask downloadTask) {
        DownloadHistory thisHistory = findThisHistory(downloadTask);

        if (thisHistory != null) {
            updateItemInfo(DOWNLOAD_STAT_PAUSE, downloadTask, thisHistory);
        }
    }

    /**
     * 等待中
     */
    @Download.onWait
    void onWaiting (DownloadTask downloadTask) {
        DownloadHistory thisHistory = findThisHistory(downloadTask);

        if (thisHistory != null) {
            updateItemInfo(DOWNLOAD_STAT_WAITING, downloadTask, thisHistory);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.downloading_all_stat:
                boolean downloadingAllStatSelected = downloadingAllStat.isSelected();

                downloadingAllStat.setSelected(!downloadingAllStatSelected);
                if (downloadingAllStatSelected) {
                    downloadingAllStat.setText(R.string.downloadManagerPlayAll);
                    Aria.download(this).resumeAllTask();
                } else {
                    downloadingAllStat.setText(R.string.downloadManagerPauseAll);
                    Aria.download(this).stopAllTask();
                }
                break;
            case R.id.downloading_edit:
                int visibility = downloadManagerEditContainer.getVisibility();
                if (visibility == View.GONE) {
                    downloadManagerEditContainer.setVisibility(View.VISIBLE);
                    downloadingEdit.setText(R.string.cancel);
                } else if (visibility == View.VISIBLE) {
                    downloadManagerEditContainer.setVisibility(View.GONE);
                    downloadingEdit.setText(R.string.downloadManagerEdit);
                }

                downloadingAdapter.showAllSelect();
                break;
            case R.id.downloading_edit_select_all:
                boolean downloadingEditSelectAllSelected = downloadingEditSelectAll.isSelected();

                downloadingEditSelectAll.setSelected(!downloadingEditSelectAllSelected);
                if (downloadingEditSelectAllSelected) {
                    downloadingEditSelectAll.setText(R.string.downloadManagerSelectedAll);
                } else {
                    downloadingEditSelectAll.setText(R.string.downloadManagerCancelSelectedAll);
                }

                downloadingAdapter.selectedAll(!downloadingEditSelectAllSelected);
                break;
            case R.id.downloading_edit_remove:
                downloadingAdapter.removeAllSelected();
                break;
            default:
                break;
        }
    }

    /**
     * 更新下载条目数据
     *
     * @param stat  下载状态
     * @param downloadTask  DownloadTask
     * @param thisHistory   DownloadHistory
     */
    private void updateItemInfo(int stat, DownloadTask downloadTask, DownloadHistory thisHistory) {
        int index = downloadHistoryList.indexOf(thisHistory);

        View itemView = linearLayoutManager.findViewByPosition(index);
        if (itemView != null) {
            TextView downloadingItemCurrentSize = itemView.findViewById(R.id.downloading_item_current_size);
            ProgressBar downloadingItemProgressBar = itemView.findViewById(R.id.downloading_item_progressBar);
            TextView downloadingItemNowSpeed = itemView.findViewById(R.id.downloading_item_now_speed);
            ImageView downloadingItemStat = itemView.findViewById(R.id.downloading_item_stat);

            DownloadEntity entity = downloadTask.getEntity();
            String currentFileSizeAndCount = ((double) (entity.getCurrentProgress() / 1000 / 1000)) + "M/" +
                    (entity.getConvertFileSize().replace("mb", "M"));
            downloadingItemCurrentSize.setText(currentFileSizeAndCount);
            downloadingItemProgressBar.setProgress(entity.getPercent());

            String speedStr;
            if (stat == DOWNLOAD_STAT_RUNNING) {
                speedStr = entity.getConvertSpeed().replace("mb", "M");
            } else if (stat == DOWNLOAD_STAT_ERROR) {
                speedStr = getString(R.string.downloadingError);
            } else if (stat == DOWNLOAD_STAT_WAITING) {
                speedStr = getString(R.string.downloadingWaiting);
            } else {
                speedStr = getString(R.string.downloadingPaused);
            }
            downloadingItemNowSpeed.setText(speedStr);
            Fuck.red(speedStr);

            downloadingItemStat.setImageLevel(stat);
        }
    }

    /**
     * 在'downloadHistoryList'中找到指定的下载记录
     * 
     * @param downloadTask  DownloadTask
     * @return  DownloadHistory
     */
    private DownloadHistory findThisHistory (DownloadTask downloadTask) {
        if (downloadHistoryList != null) {
            for (DownloadHistory downloadHistory : downloadHistoryList) {
                if (downloadHistory.getTaskId() == downloadTask.getEntity().getId()) {
                    return downloadHistory;
                }
            }
        }

        return null;
    }

    @Override
    public void onDestroy() {
        // 取消注册Aria
        Aria.download(this).unRegister();
        super.onDestroy();
    }
}