package com.leon.biuvideo.ui.fragments.downloadedFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.DownloadAdapter.DownloadedFailListAdapter;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedDetailMedia;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.values.Tables;

import java.util.List;

/**
 * 缓存失败fragment
 */
public class DownloadFailListFragment extends BaseFragment {
    private RecyclerView fragment_downloaded_media_detail_recyclerView;

    private DownloadedFailListAdapter downloadedFailListAdapter;
    private List<DownloadedDetailMedia> downloadedDetailMedias;

    @Override
    public int setLayout() {
        return R.layout.fragment_downloaded_media_detail;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        fragment_downloaded_media_detail_recyclerView = findView(R.id.fragment_downloaded_media_detail_recyclerView);
        bindingUtils.setOnClickListener(R.id.fragment_downloaded_media_detail_imageView_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });
    }

    @Override
    public void initValues() {
        SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(context, Tables.DownloadDetailsForVideo);
        DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils = (DownloadRecordsDatabaseUtils) sqLiteHelperFactory.getInstance();
        downloadedDetailMedias = downloadRecordsDatabaseUtils.queryDownloadFailMedia();

        downloadedFailListAdapter = new DownloadedFailListAdapter(context, downloadedDetailMedias);
        fragment_downloaded_media_detail_recyclerView.setAdapter(downloadedFailListAdapter);
        fragment_downloaded_media_detail_recyclerView.setLayoutManager(new LinearLayoutManager(context));

        initBroadcast();
    }

    public void initBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("DownloadVideo");
        intentFilter.addAction("DownloadAudio");

        LocalReceiver localReceiver = new LocalReceiver();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    private class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String fileName = intent.getStringExtra("fileName");
            downloadedFailListAdapter.refresh(fileName);
        }
    }
}
