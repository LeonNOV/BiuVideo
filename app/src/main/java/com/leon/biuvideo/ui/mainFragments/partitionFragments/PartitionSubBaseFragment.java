package com.leon.biuvideo.ui.mainFragments.partitionFragments;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.leon.biuvideo.beans.Partition;
import com.leon.biuvideo.beans.TestBeans.RvTestBean;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragmentWithSrr;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/23
 * @Desc
 */
public class PartitionSubBaseFragment extends BaseSupportFragmentWithSrr<RvTestBean> {
    private final String id;
    private final List<Partition.Tag> tags;

    public PartitionSubBaseFragment(String id, List<Partition.Tag> tags) {
        this.id = id;
        this.tags = tags;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);


    }
}
