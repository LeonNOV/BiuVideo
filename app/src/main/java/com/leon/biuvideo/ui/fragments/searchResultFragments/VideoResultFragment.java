package com.leon.biuvideo.ui.fragments.searchResultFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.UserFragmentAdapters.UserVideoAdapter;
import com.leon.biuvideo.beans.upMasterBean.UpVideo;
import com.leon.biuvideo.utils.OrderType;
import com.leon.biuvideo.utils.searchParsers.VideoParser;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

public class VideoResultFragment extends Fragment {
    private SmartRefreshLayout search_result_smartRefresh;
    private RecyclerView search_result_recyclerView;
    private String keywordCoded;
    private int count;

    private VideoParser videoParser;
    private List<UpVideo> videos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_result_fragment, container, false);

        initView();
        initValue();

        return view;
    }

    private void initView() {
        View view = getView();
        search_result_smartRefresh = view.findViewById(R.id.search_result_smartRefresh);
        search_result_recyclerView = view.findViewById(R.id.search_result_recyclerView);

        //关闭下拉刷新
        search_result_smartRefresh.setEnableRefresh(false);
    }

    private void initValue() {
        //获取结果总数，最大为1000， 最小为0
        count = VideoParser.getSearchVideoCount(keywordCoded);

        videoParser = new VideoParser();
        videos = videoParser.videoParse(keywordCoded, 1, OrderType.DEFAULT);

        UserVideoAdapter userVideoAdapter = new UserVideoAdapter(videos, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        search_result_recyclerView.setLayoutManager(linearLayoutManager);
        search_result_recyclerView.setAdapter(userVideoAdapter);
    }
}
