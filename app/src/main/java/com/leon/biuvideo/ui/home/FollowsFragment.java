package com.leon.biuvideo.ui.home;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.FollowsAdapter;
import com.leon.biuvideo.beans.userBeans.Follow;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.userDataParsers.FollowsParser;

/**
 * @Author Leon
 * @Time 2021/3/5
 * @Desc 关注数据界面
 */
public class FollowsFragment extends BaseSupportFragment {
    private final String mid;
    private final boolean isBiliUser;

    private DataLoader<Follow> followDataLoader;

    public FollowsFragment(boolean isBiliUser, String mid) {
        this.isBiliUser = isBiliUser;
        this.mid = mid;
    }

    public static FollowsFragment getInstance(boolean isBiliUser, String mid) {
        return new FollowsFragment(isBiliUser, mid);
    }

    @Override
    protected int setLayout() {
        return R.layout.follows_fragment;
    }

    @Override
    protected void initView() {
        setTopBar(R.id.follows_topBar);

        followDataLoader = new DataLoader<>(context, new FollowsParser(context, mid),
                R.id.follows_smartRefreshLoadingRecyclerView,
                new FollowsAdapter(getMainActivity(), context, isBiliUser),
                this);
    }

    @Override
    public void lazyInit() {
        super.lazyInit();
        followDataLoader.insertData(true);
    }
}
