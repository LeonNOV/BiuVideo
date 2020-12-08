package com.leon.biuvideo.ui.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.leon.biuvideo.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "BiliBiliCookie";
    private TextView textView_title;
    private ImageView close, refresh;
    private ProgressBar progress;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initWebView();
    }

    private void initView() {

        close = findViewById(R.id.close);
        close.setOnClickListener(this);

        textView_title = findViewById(R.id.textView_title);

        refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(this);

        progress = findViewById(R.id.progress);
        webView = findViewById(R.id.webView);
    }

    private void initWebView() {
        Handler handler = new Handler();

        WebSettings webViewSettings = webView.getSettings();
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setAllowFileAccess(false);
        webViewSettings.setSupportZoom(true);//是否可以缩放，默认true
        webViewSettings.setBuiltInZoomControls(true);
        webViewSettings.setAppCacheEnabled(true);//是否使用缓存
        webViewSettings.setDomStorageEnabled(true);//DOM Storage
        webViewSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        //网页自适应
        webViewSettings.setUseWideViewPort(true);//将图片调整到适合webView的大小
        webViewSettings.setLoadWithOverviewMode(true);//缩放至屏幕大小

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progress.setProgress(newProgress);
                Log.d(TAG, "onProgressChanged: " + newProgress);
                if (newProgress == 100) {
                    //700毫秒后进度条消失
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progress.setVisibility(View.GONE);
                        }
                    }, 700);
                } else {
                    progress.setVisibility(View.VISIBLE);
                }

                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                textView_title.setText(title);

                super.onReceivedTitle(view, title);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                //判断加载完成的页面是否为B站手机版主页
                if (url.equals("https://m.bilibili.com/index.html")) {
                    CookieManager cookieManager = CookieManager.getInstance();
                    String cookieStr = cookieManager.getCookie(url).trim();

                    //添加Cookie至本地
                    SharedPreferences sharedPreferences = getSharedPreferences("initValues", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("cookie", cookieStr).apply();

                    Log.d(TAG, cookieStr);

                    finish();
                }

                Log.d(TAG, "onPageFinished----NowPage: " + url);

                super.onPageFinished(view, url);
            }
        });

        webView.loadUrl("https://passport.bilibili.com/login");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                this.finish();
                break;
            case R.id.refresh:
                webView.reload();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    @Override
    protected void onDestroy() {
        //清除WebView缓存和Cookie
        webView.clearHistory();
        webView.clearFormData();
        webView.clearCache(true);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookies(null);
        cookieManager.removeSessionCookies(null);

        webView.destroy();
        super.onDestroy();
    }
}