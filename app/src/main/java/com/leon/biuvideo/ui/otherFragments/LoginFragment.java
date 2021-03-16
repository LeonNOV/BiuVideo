package com.leon.biuvideo.ui.otherFragments;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.utils.PreferenceUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/3/7
 * @Desc 登录界面
 */
public class LoginFragment extends BaseSupportFragment {
    private static final String ORIGINAL_URL = "https://passport.bilibili.com/login?gourl=https://m.bilibili.com/index.html";
    private static final int PROGRESS_MAX = 100;

    private WebView loginWebView;
    private ProgressBar loginProgress;
    private Handler handler;

    public static LoginFragment getInstance() {
        return new LoginFragment();
    }

    @Override
    protected int setLayout() {
        return R.layout.login_fragment;
    }

    @Override
    protected void initView() {
        SimpleTopBar loginTopBar = findView(R.id.login_topBar);
        loginTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                onBackPressedSupport();
            }

            @Override
            public void onRight() {
                loginWebView.reload();
            }
        });

        loginWebView = findView(R.id.login_webView);
        loginProgress = findView(R.id.login_progress);

        initWebView();
    }

    private void initWebView() {
        WebSettings webViewSettings = loginWebView.getSettings();
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setAllowFileAccess(false);

        //是否可以缩放，默认true
        webViewSettings.setSupportZoom(true);
        webViewSettings.setBuiltInZoomControls(true);

        //是否使用缓存
        webViewSettings.setAppCacheEnabled(true);

        //DOM Storage
        webViewSettings.setDomStorageEnabled(true);
        webViewSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        //网页自适应
        //将图片调整到适合webView的大小
        webViewSettings.setUseWideViewPort(true);

        //缩放至屏幕大小
        webViewSettings.setLoadWithOverviewMode(true);

        loginWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                loginProgress.setProgress(newProgress);
                if (newProgress == PROGRESS_MAX) {
                    //700毫秒后进度条消失
                    if (handler == null) {
                        handler = new Handler(Looper.getMainLooper());
                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loginProgress.setVisibility(View.GONE);
                        }
                    }, 700);
                } else {
                    loginProgress.setVisibility(View.VISIBLE);
                }

                super.onProgressChanged(view, newProgress);
            }
        });

        loginWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                //判断加载完成的页面是否为B站手机版主页
                if ("https://m.bilibili.com/index.html".equals(url)) {
                    SimpleSnackBar.make(view, "正在获取用户信息中，请不要进行任何操作", SimpleSnackBar.LENGTH_LONG).show();
                    CookieManager cookieManager = CookieManager.getInstance();
                    String cookieStr = cookieManager.getCookie(url).trim();

                    //获取用户ID
                    String[] split = cookieStr.split("; ");
                    Map<String, String> cookieMap = new HashMap<>();
                    for (String s : split) {
                        if (s.startsWith("DedeUserID")) {
                            String[] arrayTemp = s.split("=");
                            cookieMap.put(arrayTemp[0], arrayTemp[1]);
                        }
                    }

                    if (!cookieMap.containsKey("DedeUserID")) {
                        SimpleSnackBar.make(view, "获取不到登录信息，请重新进行登录", SimpleSnackBar.LENGTH_SHORT).show();
                        return;
                    }

                    PreferenceUtils.setCookie(cookieStr);
                    PreferenceUtils.setUserId(cookieMap.get("DedeUserID"));
                    PreferenceUtils.setLoginStatus(true);

                    // 登录成功，发送广播
                    sendBroadcast();

                    backPressed();
                }
            }
        });

        // 参数gourl为登陆成功后跳转到的url地址
        loginWebView.loadUrl(ORIGINAL_URL);
    }

    /**
     * 登录成功后发送本地广播
     */
    private void sendBroadcast() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);

        Intent intent = new Intent("loginStatus");
        intent.putExtra("loginStatus", true);

        localBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public boolean onBackPressedSupport() {
        if (loginWebView.canGoBack() && !(ORIGINAL_URL.equals(loginWebView.getUrl()))) {
            loginWebView.goBack();
        } else {
            onDestroy();

            // 由于onBackPressedSupport已被重写，此处调用backPressed并不会返回到上一个Fragment，此处只能使用pop将此Fragment从栈内弹出
            pop();
        }

        return true;
    }

    @Override
    public void onDestroy() {
        //清除WebView缓存和Cookie
        loginWebView.clearHistory();
        loginWebView.clearFormData();
        loginWebView.clearCache(true);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookies(null);
        cookieManager.removeSessionCookies(null);

        loginWebView.destroy();

        super.onDestroy();
    }
}
