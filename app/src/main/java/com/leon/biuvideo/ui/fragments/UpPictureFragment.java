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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.UpPictureAdapter;
import com.leon.biuvideo.beans.upMasterBean.UpPicture;

import java.util.List;

public class UpPictureFragment extends Fragment {
    private List<UpPicture> upPictures;
    private Context context;

    private View view;
    private RecyclerView up_picture_recyclerView;

    public UpPictureFragment(List<UpPicture> upPictures, Context context) {
        this.upPictures = upPictures;
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
        up_picture_recyclerView  = view.findViewById(R.id.up_picture_recyclerView);
    }

    private void initValue() {
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        up_picture_recyclerView.setLayoutManager(gridLayoutManager);

        UpPictureAdapter upPictureAdapter = new UpPictureAdapter(upPictures, context);
        up_picture_recyclerView.setAdapter(upPictureAdapter);
    }
}
