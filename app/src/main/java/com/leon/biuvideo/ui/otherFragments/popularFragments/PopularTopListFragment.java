package com.leon.biuvideo.ui.otherFragments.popularFragments;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.ViewPager2Adapter;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.utils.ViewUtils;
import com.leon.biuvideo.values.apis.BiliBiliFullSiteAPIs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 热门排行榜页面-排行榜
 */
public class PopularTopListFragment extends BaseSupportFragment {
    private static final Map<String, String> PARAMS = new LinkedHashMap<>();

    private MainActivity.OnTouchListener onTouchListener;

    @Override
    protected int setLayout() {
        return R.layout.popular_top_list;
    }

    @Override
    protected void initView() {
        SimpleTopBar popularTopList = view.findViewById(R.id.popular_top_list);
        popularTopList.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {

            }
        });

        setParams();

        String[] titles = new String[PARAMS.size()];
        List<Fragment> subFragments = new ArrayList<>(PARAMS.size());

        Set<Map.Entry<String, String>> entrySet = PARAMS.entrySet();
        for (int i = 0; i < entrySet.size(); i++) {
            subFragments.add(new PopularTopListSubFragment(entry.getKey()));
            titles[i] = entry.getKey();
        }

        TabLayout popularTopListTabLayout = findView(R.id.popular_top_list_tabLayout);
        ViewPager2 popularTopListViewPager = findView(R.id.popular_top_list_viewPager);
        popularTopListViewPager.setAdapter(new ViewPager2Adapter(this, subFragments));

        onTouchListener = ViewUtils.initTabLayoutAndViewPager2(getActivity(), popularTopListTabLayout, popularTopListViewPager, TITLES, 0);
    }

    private void setParams() {
        PARAMS.put("全站", BiliBiliFullSiteAPIs.ALL);
        PARAMS.put("番剧", BiliBiliFullSiteAPIs.BANGUMI);
        PARAMS.put("国产动画", BiliBiliFullSiteAPIs.GUOCHAN);
        PARAMS.put("国创相关", BiliBiliFullSiteAPIs.GUOCHUANG);
        PARAMS.put("纪录片", BiliBiliFullSiteAPIs.DOCUMENTARY);
        PARAMS.put("动画", BiliBiliFullSiteAPIs.DOUGA);
        PARAMS.put("音乐", BiliBiliFullSiteAPIs.MUSIC);
        PARAMS.put("舞蹈", BiliBiliFullSiteAPIs.DANCE);
        PARAMS.put("游戏", BiliBiliFullSiteAPIs.GAME);
        PARAMS.put("知识", BiliBiliFullSiteAPIs.TECHNOLOGY);
        PARAMS.put("数码", BiliBiliFullSiteAPIs.DIGITAL);
        PARAMS.put("生活", BiliBiliFullSiteAPIs.LIFE);
        PARAMS.put("美食", BiliBiliFullSiteAPIs.FOOD);
        PARAMS.put("动物圈", BiliBiliFullSiteAPIs.ANIMAL);
        PARAMS.put("鬼畜", BiliBiliFullSiteAPIs.KICHIKU);
        PARAMS.put("时尚", BiliBiliFullSiteAPIs.FASHION);
        PARAMS.put("娱乐", BiliBiliFullSiteAPIs.ENT);
        PARAMS.put("影视", BiliBiliFullSiteAPIs.CINEPHILE);
        PARAMS.put("电影", BiliBiliFullSiteAPIs.MOVIE);
        PARAMS.put("电视剧", BiliBiliFullSiteAPIs.TELEPLAY);
        PARAMS.put("原创", BiliBiliFullSiteAPIs.ORIGIN);
        PARAMS.put("新人", BiliBiliFullSiteAPIs.ROOKIE);
    }

    @Override
    public void onDestroy() {
        // 取消注册Touch事件
        ((MainActivity) getActivity()).unregisterTouchEvenListener(onTouchListener);

        super.onDestroy();
    }

    private class FragmentParams {
        public FragmentParams(int type,
                              BiliBiliFullSiteAPIs.RANKING_RID RANKINGRID,
                              BiliBiliFullSiteAPIs.RANKING_TYPE rankingV2Type,
                              BiliBiliFullSiteAPIs.SeasonType seasonType) {
            this.type = type;
            this.RANKINGRID = RANKINGRID;
            this.rankingV2Type = rankingV2Type;
            this.seasonType = seasonType;
        }

        /**
         * 0：RANKING_V2接口；1：WEB_LIST接口；2：BANGUMI接口
         */
        public int type;

        public BiliBiliFullSiteAPIs.RANKING_RID RANKINGRID;
        public BiliBiliFullSiteAPIs.RANKING_TYPE rankingV2Type;
        public BiliBiliFullSiteAPIs.SeasonType seasonType;
    }
}