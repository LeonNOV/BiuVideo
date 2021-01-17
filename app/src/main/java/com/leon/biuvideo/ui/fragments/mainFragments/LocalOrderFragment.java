package com.leon.biuvideo.ui.fragments.mainFragments;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.ViewPageAdapter;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.ui.fragments.localOrderFragments.LocalOrderBaseFragment;
import com.leon.biuvideo.ui.fragments.localOrderFragments.LocalOrderVideoFragment;
import com.leon.biuvideo.utils.ViewUtils;
import com.leon.biuvideo.values.LocalOrderType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 播放列表fragment
 */
public class LocalOrderFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private Map<Integer, TextView> textViewMap;

    @Override
    public int setLayout() {
        return R.layout.main_fragment_local_order;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        textViewMap = new HashMap<>();
        TextView main_fragment_local_order_textView_bangumi = findView(R.id.main_fragment_local_order_textView_bangumi);
        main_fragment_local_order_textView_bangumi.setOnClickListener(this);
        textViewMap.put(0, main_fragment_local_order_textView_bangumi);

        TextView main_fragment_local_order_textView_video = findView(R.id.main_fragment_local_order_textView_video);
        main_fragment_local_order_textView_video.setOnClickListener(this);
        textViewMap.put(1, main_fragment_local_order_textView_video);

        TextView main_fragment_local_order_textView_article = findView(R.id.main_fragment_local_order_textView_article);
        main_fragment_local_order_textView_article.setOnClickListener(this);
        textViewMap.put(2, main_fragment_local_order_textView_article);

        TextView main_fragment_local_order_textView_audio = findView(R.id.main_fragment_local_order_textView_audio);
        main_fragment_local_order_textView_audio.setOnClickListener(this);
        textViewMap.put(3, main_fragment_local_order_textView_audio);

        viewPager = findView(R.id.main_fragment_local_order_viewPage);
    }

    @Override
    public void initValues() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new LocalOrderBaseFragment(LocalOrderType.BANGUMI));
        fragmentList.add(new LocalOrderVideoFragment());
        fragmentList.add(new LocalOrderBaseFragment(LocalOrderType.ARTICLE));
        fragmentList.add(new LocalOrderBaseFragment(LocalOrderType.AUDIO));

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragmentList);
        viewPager.setAdapter(viewPageAdapter);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_fragment_local_order_textView_bangumi:
                viewPager.setCurrentItem(0);
                break;
            case R.id.main_fragment_local_order_textView_video:
                viewPager.setCurrentItem(1);
                break;
            case R.id.main_fragment_local_order_textView_article:
                viewPager.setCurrentItem(2);
                break;
            case R.id.main_fragment_local_order_textView_audio:
                viewPager.setCurrentItem(3);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {
        ViewUtils.changeText(textViewMap, position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
