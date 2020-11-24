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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private ProgressBar search_progressBar;

    private String keyword;
    private List<Fragment> fragments;


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
        search_editText_searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //当i == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    //获取输入内容
                    String value = search_editText_searchBox.getText().toString();

                    if (value.equals("")) {
                        Toast.makeText(SearchResultActivity.this, "不输点啥就搜吗？", Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        //刷新当前界面数据,显示progressBar
                        search_progressBar.setVisibility(View.VISIBLE);

                        keyword = value;
                        fragments.clear();
                        initViewPage();

                        search_progressBar.setVisibility(View.GONE);
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

        search_progressBar = findViewById(R.id.search_progressBar);

        search_viewPager = findViewById(R.id.search_viewPager);
        search_viewPager.addOnPageChangeListener(this);
    }

    private void initValue() {
        //获取输入的关键词
        Intent intent = getIntent();
        keyword = intent.getStringExtra("keyword");

        search_editText_searchBox.setText(keyword);
    }

    private void initViewPage() {
        fragments = new ArrayList<>();

        fragments.add(new VideoResultFragment(keyword));
        fragments.add(new ArticleResultFragment(keyword));
        fragments.add(new BiliUserResultFragment(keyword));

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
        int point_bilibili_pink = R.drawable.shape_bilibili_pink;
        int point_bilibili_pink_lite = R.drawable.ripple_bilibili_pink_lite;

        switch (position) {
            case 0:
                search_textView_result_video.setBackgroundResource(point_bilibili_pink);
                search_textView_result_article.setBackgroundResource(point_bilibili_pink_lite);
                search_textView_result_user.setBackgroundResource(point_bilibili_pink_lite);

                break;
            case 1:
                search_textView_result_video.setBackgroundResource(point_bilibili_pink_lite);
                search_textView_result_article.setBackgroundResource(point_bilibili_pink);
                search_textView_result_user.setBackgroundResource(point_bilibili_pink_lite);

                break;
            case 2:
                search_textView_result_video.setBackgroundResource(point_bilibili_pink_lite);
                search_textView_result_article.setBackgroundResource(point_bilibili_pink_lite);
                search_textView_result_user.setBackgroundResource(point_bilibili_pink);

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