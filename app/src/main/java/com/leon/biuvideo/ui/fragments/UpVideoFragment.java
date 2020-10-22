package com.leon.biuvideo.ui.fragments;

import android.content.Context;
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
import com.leon.biuvideo.adapters.UpVideoAdapter;
import com.leon.biuvideo.beans.upMasterBean.UpVideo;

import java.util.List;

public class UpVideoFragment extends Fragment {
    private List<UpVideo> upVideos;
    private Context context;

    private View view;
    private RecyclerView up_video_recyclerView;

    public UpVideoFragment(List<UpVideo> upVideos, Context context) {
        this.upVideos = upVideos;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_up_picture, container, false);

        initView();
        initValue();

        return view;
    }

    private void initView() {
        up_video_recyclerView  = view.findViewById(R.id.up_video_recyclerView);
    }

    private void initValue() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        up_video_recyclerView.setLayoutManager(layoutManager);

        UpVideoAdapter upVideoAdapter = new UpVideoAdapter(upVideos, context);
        up_video_recyclerView.setAdapter(upVideoAdapter);
    }
}
