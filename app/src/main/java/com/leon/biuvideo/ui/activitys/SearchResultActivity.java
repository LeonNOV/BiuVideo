package com.leon.biuvideo.ui.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.ViewPageAdapter;
import com.leon.biuvideo.ui.fragments.searchResultFragments.ArticleResultFragment;
import com.leon.biuvideo.ui.fragments.searchResultFragments.BiliUserResultFragment;
import com.leon.biuvideo.ui.fragments.searchResultFragments.VideoResultFragment;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ImageView search_imageView_back, search_imageView_clean;
    private EditText search_editText_searchBox;
    private TextView search_textView_result_video, search_textView_result_article, search_textView_result_user;
    private ViewPager search_viewPager;

    private String keywordUnCoded;

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
        search_imageView_back = findViewById(R.id.search_imageView_back);
        search_imageView_back.setOnClickListener(this);

        search_imageView_clean = findViewById(R.id.search_imageView_clean);
        search_imageView_clean.setOnClickListener(this);

        search_editText_searchBox = findViewById(R.id.search_editText_searchBox);
        search_textView_result_video  = findViewById(R.id.search_textView_result_video);
        search_textView_result_video.setOnClickListener(this);

        search_textView_result_article = findViewById(R.id.search_textView_result_article);
        search_textView_result_article.setOnClickListener(this);

        search_textView_result_user = findViewById(R.id.search_textView_result_user);
        search_textView_result_user.setOnClickListener(this);

        search_viewPager = findViewById(R.id.search_viewPager);
        search_viewPager.addOnPageChangeListener(this);
    }

    private void initValue() {
        //获取输入的关键词
        Intent intent = getIntent();
        keywordUnCoded = intent.getStringExtra("keyword");

        search_editText_searchBox.setText(keywordUnCoded);
    }

    private void initViewPage() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new VideoResultFragment(keywordUnCoded));
        fragments.add(new ArticleResultFragment(keywordUnCoded));
        fragments.add(new BiliUserResultFragment(keywordUnCoded));

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), fragments);
        search_viewPager.setAdapter(viewPageAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_imageView_back:
                this.finish();

                break;
            case R.id.search_imageView_clean:
                search_editText_searchBox.setText("");

                break;
            case R.id.search_textView_result_video:
                search_viewPager.setCurrentItem(0);

                break;
            case R.id.search_textView_result_article:
                search_viewPager.setCurrentItem(1);

                break;
            case R.id.search_textView_result_user:
                search_viewPager.setCurrentItem(2);

                break;
            default:
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {
        int point_bilibili_pink_lite = R.drawable.ripple_bilibili_pink_lite;
        int point_bilibili_pink = R.drawable.shape_bilibili_pink;

        switch (position) {
            case 0:
                search_textView_result_video.setBackgroundResource(point_bilibili_pink_lite);
                search_textView_result_article.setBackgroundResource(point_bilibili_pink);
                search_textView_result_user.setBackgroundResource(point_bilibili_pink);

                break;
            case 1:
                search_textView_result_video.setBackgroundResource(point_bilibili_pink);
                search_textView_result_article.setBackgroundResource(point_bilibili_pink_lite);
                search_textView_result_user.setBackgroundResource(point_bilibili_pink);

                break;
            case 2:
                search_textView_result_video.setBackgroundResource(point_bilibili_pink);
                search_textView_result_article.setBackgroundResource(point_bilibili_pink);
                search_textView_result_user.setBackgroundResource(point_bilibili_pink_lite);

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