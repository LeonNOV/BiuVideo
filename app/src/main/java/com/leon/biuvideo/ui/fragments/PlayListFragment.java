package com.leon.biuvideo.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.ViewPageAdapter;
import com.leon.biuvideo.ui.fragments.PlayListFragments.MusicListFragment;
import com.leon.biuvideo.ui.fragments.PlayListFragments.VideoListFragment;
import com.leon.biuvideo.utils.Fuck;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity中的播放列表片段
 */
public class PlayListFragment extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private TextView play_list_textView_video, play_list_textView_music;
    private ViewPager play_list_viewPage;

    private View view;

    private int point_bilibili_pink = R.drawable.shape_bilibili_pink;
    private int point_bilibili_pink_lite = R.drawable.ripple_bilibili_pink_lite;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment_play_list_fragment, container, false);

        initView();
        initFragments();

        return view;
    }

    private void initView() {
        play_list_textView_video = view.findViewById(R.id.play_list_textView_video);
        play_list_textView_video.setOnClickListener(this);

        play_list_textView_music = view.findViewById(R.id.play_list_textView_music);
        play_list_textView_music.setOnClickListener(this);

        play_list_viewPage = view.findViewById(R.id.play_list_viewPage);
        play_list_viewPage.addOnPageChangeListener(this);
    }

    private void initFragments() {
        List<Fragment> fragments = new ArrayList<>();

        fragments.add(new VideoListFragment());
        fragments.add(new MusicListFragment());

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getParentFragmentManager(), fragments);
        play_list_viewPage.setAdapter(viewPageAdapter);
    }

    @Override
    public void onPageSelected(int position) {

        Log.d(Fuck.blue, "page position:" + position);

        switch (position) {
            case 0:
                play_list_textView_video.setBackgroundResource(point_bilibili_pink);
                play_list_textView_music.setBackgroundResource(point_bilibili_pink_lite);

                break;
            case 1:
                play_list_textView_video.setBackgroundResource(point_bilibili_pink_lite);
                play_list_textView_music.setBackgroundResource(point_bilibili_pink);

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
