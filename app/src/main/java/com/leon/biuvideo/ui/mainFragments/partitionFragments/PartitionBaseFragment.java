package com.leon.biuvideo.ui.mainFragments.partitionFragments;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.ViewPager2Adapter;
import com.leon.biuvideo.beans.Partition;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.utils.ViewUtils;
import com.leon.biuvideo.values.Partitions;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/23
 * @Desc
 */
public class PartitionBaseFragment extends BaseSupportFragment {
    private final Partitions partitions;

    public PartitionBaseFragment(Partitions partitions) {
        this.partitions = partitions;
    }

    @Override
    protected int setLayout() {
        return R.layout.partition_base_fragment;
    }

    @Override
    protected void initView() {
        List<Partition> subPartition = Partitions.getSubPartition(partitions);

        String[] titles = new String[subPartition.size()];
        List<Fragment> subFragments = new ArrayList<>();

        for (int i = 0; i < subPartition.size(); i++) {
            titles[i] = subPartition.get(i).title;
            subFragments.add(new PartitionSubBaseFragment(subPartition.get(i).id, subPartition.get(i).tags));
        }

        SimpleTopBar partitionBaseTopBar = findView(R.id.partition_base_topBar);
        partitionBaseTopBar.setTopBarTitle(Partitions.getName(partitions));
        partitionBaseTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {

            }
        });
        TabLayout partitionBaseTabLayout = findView(R.id.partition_base_tabLayout);

//        RecyclerView partitionBaseTags = findView(R.id.partition_base_tags);

        ViewPager2 partitionBaseViewPager2 = findView(R.id.partition_base_viewPager2);
        partitionBaseViewPager2.setAdapter(new ViewPager2Adapter(this, subFragments));

        ViewUtils.initTabLayoutAndViewPager2(partitionBaseTabLayout, partitionBaseViewPager2, titles, 0);
    }
}
