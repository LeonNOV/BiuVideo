package com.leon.biuvideo.ui.activitys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.leon.biuvideo.R;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.LogTip;
import com.leon.biuvideo.utils.Paths;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

        article_webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                article_webView.loadUrl("javascript:" +
                        "<script>\n" +
                        "function removeTags(tagname){\n" +
                        "    var remove = document.getElementsByTagName(tagname);\n" +
                        "    for( var i = 0 ; i < remove.length ; i++ ){\n" +
                        "        if( remove[i].className ==\"h5-download-bar\"||\"clearfix\"||\"attention-animation-holder\" ||\"new-iteration-list\"||\"unselectable\"||\"new-border\"||\"info-bar\"||\"tag-container\"||\"article-action clearfix\"){\n" +
                        "            remove[i].parentNode.removeChild( remove[i] );\n" +
                        "        }\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "function removeID(IDname){\n" +
                        "    var remove = document.getElementById(IDname);\n" +
                        "        if( remove.id ==\"app\"){\n" +
                        "            remove.parentNode.removeChild(remove);\n" +
                        "    }\n" +
                        "} \n" +
                        "\n" +
                        "function removehref(Name){\n" +
                        "    var remove=document.getElementsByTagName(Name)\n" +
                        "    for(var i=0;i<remove.length;i++){\n" +
                        "        if(remove[i].classList==\"author-face\" ||\"author-name\"){\n" +
                        "            remove[i].removeAttribute(\"href\")\n" +
                        "        }\n" +
                        "    }\n" +
                        "}" +
                        "</script>");

//                article_webView.loadUrl("javascript:deleteElements()");
//                article_webView.evaluateJavascript("javascript:deleteElements()", new ValueCallback<String>() {
//                    @Override
//                    public void onReceiveValue(String value) {
//                        Log.d(LogTip.blue, "onReceiveValue: " + value);
//                    }
//                });
            }
        });
    }

    private String parseHTML() {

        String html = HttpUtils.GetHtmlSrc(Paths.article + "8201179");


        return null;
    }

    private void HtmlContent() {
        article_webView.loadUrl("javascript:" +
                "function deleteElements() {\n" +
                "    // 删除class名的div 功能\n" +
                "    var remove = document.getElementsByTagName(\"div\");\n" +
                "    for (var i = 0; i < remove.length; i++) {\n" +
                "        if (remove[i].className == \"h5-download-bar\" || \"clearfix\" || \"attention-animation-holder\" || \"new-iteration-list\" || \"unselectable\" || \"new-border\" || \"info-bar\" || \"tag-container\" || \"article-action clearfix\") {\n" +
                "            remove[i].parentNode.removeChild(remove[i]);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    // 删除ID名的div 功能\n" +
                "    var remove1 = document.getElementById(\"id\");\n" +
                "    if (remove1 != null) {\n" +
                "        remove1.parentNode.removeChild(remove);\n" +
                "    }\n" +
                "\n" +
                "    // 删除class名 的href属性\n" +
                "    var remove2 = document.getElementsByTagName(\"div\")\n" +
                "    for (var i = 0; i < remove2.length; i++) {\n" +
                "        if (remove2[i].classList == \"author-face\" || \"author-name\") {\n" +
                "            remove2[i].removeAttribute(\"href\")\n" +
                "        }\n" +
                "    }\n" +
                "}\n");

        article_webView.loadUrl("javascript:deleteElements()");
    }

    private String readJS() {
        try {
            InputStream inputStream = getApplicationContext().getAssets().open("removeElements.js");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String temp;
            StringBuilder js = new StringBuilder();
            while ((temp = bufferedReader.readLine()) != null) {
                js.append(temp);
            }

            bufferedReader.close();
            inputStream.close();
            return js.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}