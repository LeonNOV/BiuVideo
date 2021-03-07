package com.leon.biuvideo.ui.otherFragments.popularFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.leon.biuvideo.R;

import me.yokeyword.fragmentation.SupportFragment;

public class PopularHistoryFragment extends SupportFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discover_popular_history, container, false);
        return view;
    }
}
