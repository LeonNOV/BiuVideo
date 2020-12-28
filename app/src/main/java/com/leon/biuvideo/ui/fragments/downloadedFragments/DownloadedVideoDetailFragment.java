package com.leon.biuvideo.ui.fragments.downloadedFragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.downloadAdapter.MediaDetailAdapter;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedDetailMedia;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;
import com.leon.biuvideo.utils.dataBaseUtils.SQLiteHelperFactory;
import com.leon.biuvideo.values.Tables;

import java.util.List;

public class DownloadedVideoDetailFragment extends BaseFragment {
    private RecyclerView fragment_downloaded_video_detail_recyclerView;

    private String mainId;
    private List<DownloadedDetailMedia> downloadedDetailMedia;

    @Override
    public int setLayout() {
        return R.layout.fragment_downloaded_video_detail;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mainId = arguments.getString("mainId");

            SQLiteHelperFactory sqLiteHelperFactory = new SQLiteHelperFactory(context, Tables.DownloadDetailsForVideo);
            DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils = (DownloadRecordsDatabaseUtils) sqLiteHelperFactory.getInstance();
            downloadedDetailMedia = downloadRecordsDatabaseUtils.queryAllSubVideo(mainId);

            fragment_downloaded_video_detail_recyclerView = findView(R.id.fragment_downloaded_video_detail_recyclerView);
            bindingUtils.setOnClickListener(R.id.fragment_downloaded_video_detail_imageView_back, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.action_downloadedVideoDetailFragment_to_downloadedVideoListFragment);
                }
            });

            fragment_downloaded_video_detail_recyclerView.setAdapter(new MediaDetailAdapter(downloadedDetailMedia, context));
            fragment_downloaded_video_detail_recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            Toast.makeText(context, "找不到此视频信息", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).popBackStack();
        }
    }

    @Override
    public void initValues() {

    }
}
