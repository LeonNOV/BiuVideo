package com.leon.biuvideo.ui.fragments.downloadedFragments;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.downloadAdapter.MediaListAdapter;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedRecordsForVideo;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.values.Tables;

import java.util.List;

public class VideoListFragment extends BaseFragment {
    private RecyclerView fragment_recyclerView;
    private TextView fragment_no_data;

    private DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils;
    private List<DownloadedRecordsForVideo> downloadedRecordsForVideos;

    @Override
    public int setLayout() {
        return R.layout.layout_recycler_view_public;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        fragment_recyclerView = findView(R.id.fragment_recyclerView);
        fragment_no_data = findView(R.id.fragment_no_data);
    }

    @Override
    public void initValues() {
        SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(context, Tables.DownloadDetailsForVideo);
        downloadRecordsDatabaseUtils = (DownloadRecordsDatabaseUtils) sqLiteHelperFactory.getInstance();
    }

    @Override
    public void onResume() {
        downloadedRecordsForVideos = downloadRecordsDatabaseUtils.queryAllVideo();

        if (downloadedRecordsForVideos.size() == 0) {
            fragment_recyclerView.setVisibility(View.GONE);
            fragment_no_data.setVisibility(View.VISIBLE);
        } else {
            fragment_recyclerView.setVisibility(View.VISIBLE);
            fragment_no_data.setVisibility(View.GONE);

            fragment_recyclerView.setAdapter(new MediaListAdapter(downloadedRecordsForVideos, context));
            fragment_recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }

        super.onResume();
    }
}
