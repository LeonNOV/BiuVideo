package com.leon.biuvideo.ui.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.FragmentViewPagerAdapter;
import com.leon.biuvideo.ui.fragments.searchResultFragments.ArticleResultFragment;
import com.leon.biuvideo.ui.fragments.searchResultFragments.BangumiResultFragment;
import com.leon.biuvideo.ui.fragments.searchResultFragments.BiliUserResultFragment;
import com.leon.biuvideo.ui.fragments.searchResultFragments.VideoResultFragment;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索结果Activity
 */
public class SearchResultActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private EditText search_editText_searchBox;
    private TextView search_textView_result_video, search_textView_result_article, search_textView_result_user, search_textView_result_bangumi;
    private ViewPager search_viewPager;

    private String keyword;
    private List<Fragment> fragments;
    private Map<Integer, TextView> textViewMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        initView();
        initValue();
        initViewPage();
    }

    private void initView() {
        ImageView searchImageViewBack = findViewById(R.id.search_imageView_back);
        searchImageViewBack.setOnClickListener(this);

        ImageView searchImageViewClean = findViewById(R.id.search_imageView_clean);
        searchImageViewClean.setOnClickListener(this);

        search_editText_searchBox = findViewById(R.id.search_editText_searchBox);
        search_editText_searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //判断是否有网络
                boolean isHaveNetwork = InternetUtils.checkNetwork(getApplicationContext());

                if (!isHaveNetwork) {
                    SimpleSnackBar.make(textView, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();
                    return false;
                }

                //按下软键盘的搜索按钮会触发该方法
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    //获取输入内容
                    String value = search_editText_searchBox.getText().toString();

                    if (value.equals("")) {
                        SimpleSnackBar.make(textView, "不输点儿啥吗？", SimpleSnackBar.LENGTH_SHORT).show();
                        return false;
                    } else {
                        //刷新当前界面数据
                        keyword = value;
                        refreshFragments();
                    }
                }

                return false;
            }
        });

        search_textView_result_video = findViewById(R.id.search_textView_result_video);
        search_textView_result_video.setOnClickListener(this);

        search_textView_result_article = findViewById(R.id.search_textView_result_article);
        search_textView_result_article.setOnClickListener(this);

        search_textView_result_user = findViewById(R.id.search_textView_result_user);
        search_textView_result_user.setOnClickListener(this);

        search_textView_result_bangumi = findViewById(R.id.search_textView_result_bangumi);
        search_textView_result_bangumi.setOnClickListener(this);

        search_viewPager = findViewById(R.id.search_viewPager);
        search_viewPager.addOnPageChangeListener(this);
    }

    private void initValue() {
        //获取输入的关键词
        Intent intent = getIntent();
        keyword = intent.getStringExtra("keyword");

        search_editText_searchBox.setText(keyword);

        textViewMap = new HashMap<>();
        textViewMap.put(0, search_textView_result_video);
        textViewMap.put(1, search_textView_result_bangumi);
        textViewMap.put(2, search_textView_result_article);
        textViewMap.put(3, search_textView_result_user);
    }

    private void initViewPage() {
        if (fragments == null) {
            fragments = new ArrayList<>();
        }

        fragments.add(new VideoResultFragment(keyword));
        fragments.add(new BangumiResultFragment(keyword));
        fragments.add(new ArticleResultFragment(keyword));
        fragments.add(new BiliUserResultFragment(keyword));

        FragmentViewPagerAdapter fragmentViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments);
        search_viewPager.setAdapter(fragmentViewPagerAdapter);
        search_viewPager.setOffscreenPageLimit(fragments.size());
    }

    private void refreshFragments() {
        for (Fragment fragment : fragments) {
            if (!fragment.isHidden()) {
                if (fragment instanceof VideoResultFragment) {
                    VideoResultFragment videoResultFragment = (VideoResultFragment) fragment;
                    videoResultFragment.updateData(keyword);
                } else if (fragment instanceof ArticleResultFragment) {
                    ArticleResultFragment articleResultFragment = (ArticleResultFragment) fragment;
                    articleResultFragment.updateData(keyword);
                } else if (fragment instanceof BiliUserResultFragment) {
                    BiliUserResultFragment biliUserResultFragment = (BiliUserResultFragment) fragment;
                    biliUserResultFragment.updateData(keyword);
                } else {
                    BangumiResultFragment bangumiResultFragment = (BangumiResultFragment) fragment;
                    bangumiResultFragment.updateData(keyword);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_imageView_back:
                this.finish();

                break;
            case R.id.search_imageView_clean:
                search_editText_searchBox.getText().clear();

                break;
            case R.id.search_textView_result_video:
                search_viewPager.setCurrentItem(0);

                break;
            case R.id.search_textView_result_bangumi:
                search_viewPager.setCurrentItem(1);

                break;
            case R.id.search_textView_result_article:
                search_viewPager.setCurrentItem(2);

                break;

            case R.id.search_textView_result_user:
                search_viewPager.setCurrentItem(3);
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