package com.leon.biuvideo.ui.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.leon.biuvideo.R;

public class SearchResultActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView search_imageView_back, search_imageView_clean;
    private EditText search_editText_searchBox;
    private TextView search_textView_result_video, search_textView_result_article, search_textView_result_user;
    private ViewPager search_viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        initView();
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

                break;
            case R.id.search_textView_result_article:

                break;
            case R.id.search_textView_result_user:

                break;
            default:
                break;
        }
    }
}