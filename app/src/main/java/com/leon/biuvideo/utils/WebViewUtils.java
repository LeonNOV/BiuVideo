package com.leon.biuvideo.utils;

import android.annotation.SuppressLint;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.leon.biuvideo.values.apis.BiliBiliAPIs;

public class WebViewUtils {
    private final WebView webView;

    public WebViewUtils(WebView webView) {
        this.webView = webView;
    }

    //设置webView的链接
    public void setWebViewUrl(long aid, long cid, int pageIndex) {
        //配置webView地址
        String parameter_aid = "aid=" + aid;
        String parameter_cid = "cid=" + cid;

        //默认第一个视频为singleVideoInfoList中的第一个，page为选集列表中的第一个视频的索引（从1开始,所以要进行加1）
        String videoPath = BiliBiliAPIs.videoBaeUrl + parameter_aid + "&" + parameter_cid + "&" + "page=" + pageIndex + 1;
        configWebView(videoPath);
    }

    /**
     * 配置网页控件
     *
     * @param videoUrl 设置网页地址
     */
    @SuppressLint("SetJavaScriptEnabled")//忽略SetJavaScriptEnabled的警告
    private void configWebView(String videoUrl) {
        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAllowFileAccess(false);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);

        webView.loadUrl(videoUrl);
    }
}
