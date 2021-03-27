package com.leon.biuvideo.ui.mainFragments.partitionFragments;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.ViewPager2Adapter;
import com.leon.biuvideo.beans.Partition;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.utils.ViewUtils;
import com.leon.biuvideo.values.Partitions;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/23
 * @Desc 分区详细页面
 */
public class PartitionBaseFragment extends BaseSupportFragment {
    private final Partitions partitions;
    private MainActivity.OnTouchListener onTouchListener;

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
            Partition partition = subPartition.get(i);
            if ("-100".equals(partition.id)) {
                continue;
            }

            titles[i] = partition.title;
            subFragments.add(new PartitionSubBaseFragment(partition.id));
        }

        ViewUtils.setStatusBar(getActivity(), true);
        SimpleTopBar partitionBaseTopBar = findView(R.id.partition_base_topBar);
        partitionBaseTopBar.setTopBarTitle(Partitions.getName(partitions));
        partitionBaseTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
                ViewUtils.setStatusBar(getActivity(), false);
            }

            @Override
            public void onRight() {

            }
        });
        TabLayout partitionBaseTabLayout = findView(R.id.partition_base_tabLayout);

//        RecyclerView partitionBaseTags = findView(R.id.partition_base_tags);

        ViewPager2 partitionBaseViewPager2 = findView(R.id.partition_base_viewPager2);
        partitionBaseViewPager2.setAdapter(new ViewPager2Adapter(this, subFragments));

        onTouchListener = ViewUtils.initTabLayoutAndViewPager2(getActivity(), partitionBaseTabLayout, partitionBaseViewPager2, titles, 0);
    }

    @Override
    public void onDestroyView() {
        // 取消注册Touch事件
        ((MainActivity) getActivity()).unregisterTouchEvenListener(onTouchListener);

        super.onDestroyView();
    }
}
