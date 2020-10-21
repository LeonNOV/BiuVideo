package com.leon.biuvideo.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.UpVideoAdapter;
import com.leon.biuvideo.beans.upMasterBean.UpVideo;

import java.util.List;

public class UpVideoFragment extends Fragment {
    private List<UpVideo> upVideos;
    private Context context;

    private View view;
    private RecyclerView recyclerView;

    public UpVideoFragment(List<UpVideo> upVideos, Context context) {
        this.upVideos = upVideos;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_up_space, container, false);

        initView();
        initValue();

        return view;
    }

    private void initView() {
        recyclerView  = view.findViewById(R.id.up_space_recyclerView);
    }

    private void initValue() {
        UpVideoAdapter upVideoAdapter = new UpVideoAdapter(upVideos, context);
        recyclerView.setAdapter(upVideoAdapter);
    }
}
