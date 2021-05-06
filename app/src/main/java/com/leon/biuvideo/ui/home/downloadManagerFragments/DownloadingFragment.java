package com.leon.biuvideo.ui.home.downloadManagerFragments;

import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.task.DownloadTask;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.DownloadingAdapter;
import com.leon.biuvideo.greendao.dao.DaoBaseUtils;
import com.leon.biuvideo.greendao.dao.DownloadHistory;
import com.leon.biuvideo.greendao.daoutils.DownloadHistoryUtils;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/7
 * @Desc 下载管理页面-下载中的资源
 */
public class DownloadingFragment extends BaseSupportFragment implements View.OnClickListener {

    private TextView downloadingAllStat;
    private TextView downloadingEdit;
    private FrameLayout downloadManagerEditContainer;
    private TextView downloadingEditSelectAll;
    private DownloadingAdapter downloadingAdapter;

    @Override
    protected int setLayout() {
        return R.layout.downloading_fragment;
    }

    @Override
    protected void initView() {
        findView(R.id.downloading_container).setBackgroundColor(Color.WHITE);

        downloadingAllStat = findView(R.id.downloading_all_stat);
        downloadingAllStat.setOnClickListener(this);
        downloadingAllStat.setText(R.string.downloadManagerPauseAll);
        downloadingAllStat.setSelected(false);

        downloadingEdit = findView(R.id.downloading_edit);
        downloadingEdit.setOnClickListener(this);

        LoadingRecyclerView downloadingAllRunningTask = findView(R.id.downloading_all_running_task);
        downloadingAllRunningTask.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        downloadManagerEditContainer = findView(R.id.download_manager_edit_container);

        downloadingEditSelectAll = findView(R.id.downloading_edit_select_all);
        downloadingEditSelectAll.setOnClickListener(this);

        TextView downloadingEditRemove = findView(R.id.downloading_edit_remove);
        downloadingEditRemove.setOnClickListener(this);

        DownloadHistoryUtils downloadHistoryUtils = new DownloadHistoryUtils(context);
        DaoBaseUtils<DownloadHistory> downloadHistoryDaoUtils = downloadHistoryUtils.getDownloadHistoryDaoUtils();
        List<DownloadHistory> downloadHistoryList = downloadHistoryDaoUtils.queryAll();

        downloadingAdapter = new DownloadingAdapter(downloadHistoryList, context);

        downloadingAllRunningTask.setRecyclerViewAdapter(downloadingAdapter);
        downloadingAllRunningTask.setRecyclerViewLayoutManager(new LinearLayoutManager(context));

        downloadingAllRunningTask.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
    }

//    /**
//     * 取消下载
//     */
//    @Download.onTaskCancel
//    void onCancel (DownloadTask downloadTask) {
//
//    }
//
//    /**
//     * 下载失败
//     */
//    @Download.onTaskFail
//    void onFail (DownloadTask downloadTask) {
//
//    }
//
//    /**
//     * 下载完成
//     */
//    @Download.onTaskComplete
//    void complete (DownloadTask downloadTask) {
//
//    }
//
//    /**
//     * 下载中
//     */
//    @Download.onTaskRunning
//    void onRunning (DownloadTask downloadTask) {
//
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.downloading_all_stat:
                boolean downloadingAllStatSelected = downloadingAllStat.isSelected();

                downloadingAllStat.setSelected(!downloadingAllStatSelected);
                if (downloadingAllStatSelected) {
                    downloadingAllStat.setText(R.string.downloadManagerPlayAll);
                    Aria.download(context).resumeAllTask();
                } else {
                    downloadingAllStat.setText(R.string.downloadManagerPauseAll);
                    Aria.download(context).stopAllTask();
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
}
