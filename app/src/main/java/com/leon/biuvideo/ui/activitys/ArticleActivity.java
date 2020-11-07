package com.leon.biuvideo.ui.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.articleBeans.Article;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import static com.leon.biuvideo.R.layout.activity_article;

public class ArticleActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView article_imageView_back;
    private WebView article_webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(activity_article);

        initView();
        initValue();
    }

    private void initView() {
        article_imageView_back = findViewById(R.id.article_imageView_back);
        article_imageView_back.setOnClickListener(this);

        article_webView = findViewById(R.id.article_webView);

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
//        settings.setLoadWithOverviewMode(true);

        settings.setDefaultFontSize(20);
    }

    private void initValue() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Article article = (Article) extras.getSerializable("article");

        String path = Paths.articleWebPage + article.articleID;

        //获取文章页面
        String html = parseHTMLonPhone(path);

        //加载文章内容
        article_webView.loadData(html, "text/html", "utf-8");
    }

    /**
     * 获取文章内容
     *
     * @param path  文章地址
     * @return  返回文章HTML源码
     */
    public String parseHTMLonPhone(String path) {
        String html = HttpUtils.GetHtmlSrc(path);
        Document document = Jsoup.parse(html);

        //获取头部份
        Elements headContainer = document.getElementsByClass("head-container");
        Element first = headContainer.first();

        //删除文章基本参数
        first.childNode(5).childNode(3).remove();

        //删除部分数据
        first.childNode(1).remove();
        first.childNode(2).remove();
        first.childNode(5).remove();

        String head = headContainer.first().toString();

        //====================================================================

        //获取文章主要部分
        Elements articleHolder = document.getElementsByClass("article-holder");

        //获取所有类名为img-box的内容(即所有figure标签)
        Elements figures = articleHolder.first().getElementsByTag("figure");
        String str = "http:";

        for (Element figure : figures) {
            //获取figure中的img标签
            Element img = figure.child(0);

            //获取img中的所有属性
            Attributes attributes = img.attributes();

            //修改图片的链接
            attributes.put("src", str + attributes.get("data-src"));
            attributes.remove("data-src");
        }

        String body = articleHolder.first().toString();

        //合并html
        return combineWebPage(head + body);
    }

    /**
     * 组合文章HTML源码
     *
     * @param content  文章主题源码
     * @return  返回组合后的源码
     */
    private String combineWebPage(String content) {
        if (content != null) {

            StringBuilder webPage = new StringBuilder();

            String head = "<!DOCTYPE html>\n" +
                    "<html lang=\"zh\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0 ,user-scalable=no\">\n";

            String css = "<style>" + readCSS() + "</style>";

            webPage.append(head + css + "</head>" + "<body>" + content + "</body>\n</html>");

            webPage.append("<div class=\"head-container\">\n" +
                    "        <div class=\"banner-img-holder\">\n" +
                    "            <img src=\"https://i0.hdslb.com/bfs/article/07fe15783a2ef177ccf8ea6b0ddc0514ec7d1d10.jpg@860w_482h.webp\">\n" +
                    "        </div>\n" +
                    "    </div>");

            return webPage.toString();
        }

        return null;
    }

    /**
     * 读取assets下的css文件
     *
     * @return  返回css源代码
     */
    private String readCSS() {
        try {
            InputStream inputStream = getApplicationContext().getAssets().open("index.css");

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

    @Override
    public void onClick(View v) {
        this.finish();
    }
}