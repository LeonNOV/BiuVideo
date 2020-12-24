package com.leon.biuvideo.ui.fragments.mainFragments;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.ViewPageAdapter;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.ui.fragments.playListFragments.MusicListFragment;
import com.leon.biuvideo.ui.fragments.playListFragments.VideoListFragment;
import com.leon.biuvideo.utils.Fuck;

import java.util.ArrayList;
import java.util.List;

public class PlayListFragment extends BaseFragment implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private TextView play_list_textView_video, play_list_textView_music;
    private ViewPager play_list_viewPage;

    @Override
    public int setLayout() {
        return R.layout.main_fragment_play_list_fragment;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        play_list_textView_video = view.findViewById(R.id.play_list_textView_video);
        play_list_textView_video.setOnClickListener(this);

        play_list_textView_music = view.findViewById(R.id.play_list_textView_music);
        play_list_textView_music.setOnClickListener(this);

        play_list_viewPage = view.findViewById(R.id.play_list_viewPage);
        play_list_viewPage.addOnPageChangeListener(this);
    }

    @Override
    public void initValues() {
        List<Fragment> fragments = new ArrayList<>();

//        fragments.add(new VideoListFragment());
        fragments.add(new MusicListFragment());

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getParentFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments);
        play_list_viewPage.setAdapter(viewPageAdapter);
    }

    @Override
    public void onPageSelected(int position) {
        Log.d(Fuck.blue, "page position:" + position);

        int pointBiliBiliPink = R.drawable.shape_bilibili_pink;
        int pointBiliBiliPinkLite = R.drawable.ripple_bilibili_pink_lite;

        switch (position) {
            case 0:
                play_list_textView_video.setBackgroundResource(pointBiliBiliPink);
                play_list_textView_music.setBackgroundResource(pointBiliBiliPinkLite);

                break;
            case 1:
                play_list_textView_video.setBackgroundResource(pointBiliBiliPinkLite);
                play_list_textView_music.setBackgroundResource(pointBiliBiliPink);

                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_list_textView_video:
                play_list_viewPage.setCurrentItem(0);
                break;
            case R.id.play_list_textView_music:
                play_list_viewPage.setCurrentItem(1);
                break;
            default:
                break;
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
