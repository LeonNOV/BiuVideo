package com.leon.biuvideo.ui.otherFragments.popularFragments;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.ViewPager2Adapter;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 热门排行榜页面-排行榜
 */
public class PopularTopListFragment extends BaseSupportFragment {
    private MainActivity.OnTouchListener onTouchListener;

    @Override
    protected int setLayout() {
        return R.layout.popular_top_list;
    }

    @Override
    protected void initView() {
        setTopBar(R.id.popular_top_list);

        List<String[]> topList = getTopList();

        String[] titles = new String[topList.size()];
        List<Fragment> subFragments = new ArrayList<>(topList.size());

        for (int i = 0; i < topList.size(); i++) {
            String[] strings = topList.get(i);

            titles[i] = strings[0];
            subFragments.add(new PopularTopListSubFragment(strings));
        }

        TabLayout popularTopListTabLayout = findView(R.id.popular_top_list_tabLayout);
        ViewPager2 popularTopListViewPager = findView(R.id.popular_top_list_viewPager);
        popularTopListViewPager.setAdapter(new ViewPager2Adapter(this, subFragments));

        onTouchListener = ViewUtils.initTabLayoutAndViewPager2(getActivity(), popularTopListTabLayout, popularTopListViewPager, titles, 0);
    }

    private List<String[]> getTopList () {
        JSONObject jsonObject = JSONObject.parseObject(FileUtils.getAssetsContent(context, "topList.json"));
        JSONArray jsonArray = jsonObject.getJSONArray("apis");

        List<String[]> list = new ArrayList<>(jsonArray.size());
        for (Object o : jsonArray) {
            JSONObject object = (JSONObject) o;
            String[] strings = new String[3];

            strings[0] = object.getString("name");
            strings[1] = object.getString("type");
            strings[2] = object.getString("url");

            list.add(strings);
        }

        return list;
    }

    @Override
    public void onDestroy() {
        // 取消注册Touch事件
        ((MainActivity) getActivity()).unregisterTouchEvenListener(onTouchListener);

        super.onDestroy();
    }
}