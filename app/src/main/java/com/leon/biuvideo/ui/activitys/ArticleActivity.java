package com.leon.biuvideo.ui.activitys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.leon.biuvideo.R;
import com.leon.biuvideo.utils.HttpUtils;

import java.io.IOException;
import java.io.InputStream;

import static com.leon.biuvideo.R.layout.activity_article;

public class ArticleActivity extends AppCompatActivity {
    private WebView article_webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_article);

        initView();
        initValue();
    }

    private void initView() {
        article_webView = findViewById(R.id.article_webView);
    }

    private void initValue() {
        WebSettings settings = article_webView.getSettings();

        settings.setJavaScriptEnabled(true);

        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAppCacheEnabled(true);

        //设置自适应屏幕
        settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
//        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        settings.setLoadsImagesAutomatically(true); //支持自动加载图片
        settings.setDefaultTextEncodingName("utf-8");//设置编码格式

        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setLoadWithOverviewMode(true);

        settings.setDefaultFontSize(40);

        article_webView.loadUrl("https://www.bilibili.com/read/mobile/8107744", HttpUtils.getHeaders());

        //加载本地JS文件对网页进行修改
        article_webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                try {
                    return new WebResourceResponse("application/x-javascript", "utf-8", getApplicationContext().getAssets().open("removeElements.js"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        });
    }
}