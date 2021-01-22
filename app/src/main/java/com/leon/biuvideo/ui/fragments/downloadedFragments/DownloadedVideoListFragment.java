package com.leon.biuvideo.ui.fragments.downloadedFragments;

import android.view.View;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.downloadAdapter.DownloadedListAdapter;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedRecordsForVideo;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.dataBaseUtils.DownloadRecordsDatabaseUtils;

import java.util.List;

/**
 * 已缓存视频fragment
 */
public class DownloadedVideoListFragment extends BaseFragment implements View.OnClickListener {
    private RecyclerView fragment_downloaded_video_list_recyclerView;
    private TextView fragment_downloaded_video_list_no_data;

    @Override
    public int setLayout() {
        return R.layout.fragment_downloaded_video_list;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        fragment_downloaded_video_list_recyclerView = findView(R.id.fragment_downloaded_video_list_recyclerView);
        fragment_downloaded_video_list_no_data = findView(R.id.fragment_downloaded_video_list_no_data);
        bindingUtils
                .setOnClickListener(R.id.fragment_downloaded_video_list_textView_toAudio, this)
                .setOnClickListener(R.id.fragment_downloaded_video_list_imageView_close, this)
                .setOnClickListener(R.id.fragment_downloaded_video_list_textView_toFailList, this);

    }

    @Override
    public void initValues() {
        DownloadRecordsDatabaseUtils downloadRecordsDatabaseUtils = new DownloadRecordsDatabaseUtils(context);
        List<DownloadedRecordsForVideo> downloadedRecordsForVideos = downloadRecordsDatabaseUtils.queryAllVideo();

        if (downloadedRecordsForVideos.size() == 0) {
            fragment_downloaded_video_list_no_data.setVisibility(View.VISIBLE);
            fragment_downloaded_video_list_recyclerView.setVisibility(View.GONE);
        } else {
            fragment_downloaded_video_list_no_data.setVisibility(View.GONE);
            fragment_downloaded_video_list_recyclerView.setVisibility(View.VISIBLE);

            fragment_downloaded_video_list_recyclerView.setAdapter(new DownloadedListAdapter(downloadedRecordsForVideos, context));
            fragment_downloaded_video_list_recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_downloaded_video_list_textView_toAudio:
                Navigation.findNavController(v).navigate(R.id.action_downloadedVideoListFragment_to_downloadedMusicListFragment);
                break;
            case R.id.fragment_downloaded_video_list_imageView_close:
                getActivity().finish();
                break;
            case R.id.fragment_downloaded_video_list_textView_toFailList:
                Navigation.findNavController(v).navigate(R.id.action_downloadedVideoListFragment_to_downloadFailListFragment);
                break;
            default:
                break;
        }
    }
}
