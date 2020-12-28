package com.leon.biuvideo.ui.fragments.downloadedFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.downloadAdapter.MediaDetailAdapter;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedDetailMedia;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.values.Tables;

import java.util.List;

public class DownloadedMusicListFragment extends BaseFragment {
    private RecyclerView fragment_recyclerView;
    private TextView fragment_no_data;

    private DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils;
    private List<DownloadedDetailMedia> downloadedDetailMedias;

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

        initBroadcast();
    }

    @Override
    public void onResume() {
        downloadedDetailMedias = downloadRecordsDatabaseUtils.queryAllMusic();

        if (downloadedDetailMedias.size() == 0) {
            fragment_recyclerView.setVisibility(View.GONE);
            fragment_no_data.setVisibility(View.VISIBLE);
        } else {
            fragment_recyclerView.setVisibility(View.VISIBLE);
            fragment_no_data.setVisibility(View.GONE);

            fragment_recyclerView.setAdapter(new MediaDetailAdapter(downloadedDetailMedias, context));
            fragment_recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }

        super.onResume();
    }

    public void initBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("DownloadVideo");
        intentFilter.addAction("DownloadAudio");

        LocalReceiver localReceiver = new LocalReceiver();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    private class LocalReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("DownloadVideo")) {
                String fileName = intent.getStringExtra("fileName");
                Fuck.blue(fileName + "---已下载完成");
            }
        }
    }
}
