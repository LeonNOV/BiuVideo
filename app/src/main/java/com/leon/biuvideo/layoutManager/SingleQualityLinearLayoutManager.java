package com.leon.biuvideo.layoutManager;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class SingleQualityLinearLayoutManager extends LinearLayoutManager {
    public SingleQualityLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
