package com.leon.biuvideo.ui.user;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.FollowerAdapter;
import com.leon.biuvideo.beans.userBeans.Follower;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.userDataParsers.FollowersParser;

/**
 * @Author Leon
 * @Time 2021/3/18
 * @Desc 粉丝页面
 */
public class FollowersFragment extends BaseSupportFragment {
    private final boolean isBiliUser;
    private final String mid;

    public FollowersFragment(boolean isBiliUser, String mid) {
        this.isBiliUser = isBiliUser;
        this.mid = mid;
    }

    public static FollowersFragment getInstance(boolean isBiliUser, String mid) {
        return new FollowersFragment(isBiliUser, mid);
    }

    @Override
    protected int setLayout() {
        return R.layout.followers_fragment;
    }

    @Override
    protected void initView() {
        setTopBar(R.id.followers_topBar);

        DataLoader<Follower> followerDataLoader = new DataLoader<>(context, new FollowersParser(context, mid), R.id.followers_smartRefreshLoadingRecyclerView,
                new FollowerAdapter(getMainActivity(), context, isBiliUser), this);
        followerDataLoader.insertData(true);
    }
}
