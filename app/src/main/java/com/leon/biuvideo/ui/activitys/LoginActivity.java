package com.leon.biuvideo.ui.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textView_title;
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

        ImageView close = findViewById(R.id.login_close);
        close.setOnClickListener(this);

        textView_title = findViewById(R.id.login_textView_title);

        ImageView refresh = findViewById(R.id.login_refresh);
        refresh.setOnClickListener(this);

        progress = findViewById(R.id.login_progress);
        webView = findViewById(R.id.login_webView);
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
                    editor.putString("cookie", cookieStr);

                    //获取用户ID
                    String[] split = cookieStr.split("; ");
                    Map<String, String> cookieMap = new HashMap<>();
                    for (String s : split) {
                        if (s.startsWith("DedeUserID")) {
                            String[] arrayTemp = s.split("=");
                            cookieMap.put(arrayTemp[0], arrayTemp[1]);
                        }
                    }

                    editor.putLong("mid", Long.parseLong(Objects.requireNonNull(cookieMap.get("DedeUserID")))).apply();

                    setResult(1004);
                    finish();
                }

                super.onPageFinished(view, url);
            }
        });

        webView.loadUrl("https://passport.bilibili.com/login");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_close:
                this.finish();
                break;
            case R.id.login_refresh:
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