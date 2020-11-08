package com.leon.biuvideo.ui.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.articleBeans.Article;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.MediaUtils;
import com.leon.biuvideo.utils.Paths;
import com.leon.biuvideo.utils.ValueFormat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ArticleActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView article_textView_title;
    private ImageView article_imageView_back, article_imageView_more;
    private WebView article_webView;
    private TextView
            article_textView_category,
            article_textView_ctime,
            article_textView_view,
            article_textView_like,
            article_textView_replay;

    private Article article;
    private String encodedHtml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        initView();
        initValue();
    }

    private void initView() {
        article_textView_title = findViewById(R.id.article_textView_title);

        article_imageView_back = findViewById(R.id.article_imageView_back);
        article_imageView_back.setOnClickListener(this);

        article_imageView_more = findViewById(R.id.article_imageView_more);
        article_imageView_more.setOnClickListener(this);

        article_webView = findViewById(R.id.article_webView);
        WebSettings settings = article_webView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setUseWideViewPort(true); //将图片调整到适合webView的大小
        settings.setLoadsImagesAutomatically(true); //支持自动加载图片
        settings.setDefaultTextEncodingName("utf-8");//设置编码格式
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDefaultFontSize(20);

        article_textView_category = findViewById(R.id.article_textView_category);
        article_textView_ctime = findViewById(R.id.article_textView_ctime);
        article_textView_view = findViewById(R.id.article_textView_view);
        article_textView_like = findViewById(R.id.article_textView_like);
        article_textView_replay = findViewById(R.id.article_textView_replay);
    }

    private void initValue() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        article = (Article) extras.getSerializable("article");

        //设置标题
        article_textView_title.setText(article.title);

        //设置文章分类
        article_textView_category.setText(article.category);

        //设置创建时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        article_textView_ctime.setText(sdf.format(new Date(article.ctime * 1000)));

        //设置观看量
        String viewStr = ValueFormat.generateCN(article.view) + "次阅读";
        article_textView_view.setText(viewStr);

        //设置点赞数
        String likeStr = ValueFormat.generateCN(article.like) + "次点赞";
        article_textView_like.setText(likeStr);

        //设置评论数
        String replayStr = ValueFormat.generateCN(article.replay) + "次评论";
        article_textView_replay.setText(replayStr);

        String path = Paths.articleWebPage + article.articleID;

        //获取文章页面
        String unencodedHtml = parseHTMLonPhone(path);

        /**
         * 加载文章内容
         * 注意：原HTML是未进行编码的，如果android版本为8.0以上（不包括8.0）,则需要将其解码为base64
         */
        encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(), Base64.NO_PADDING);
        article_webView.loadData(encodedHtml, "text/html", "base64");
    }

    /**
     * 获取文章内容
     *
     * @param path 文章地址
     * @return 返回文章HTML源码
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
        String str = "https:";

        for (Element figure : figures) {
            //获取figure中的img标签
            Element img = figure.child(0);


            //获取img中的所有属性
            Attributes attributes = img.attributes();

            //修改图片的链接
            attributes.put("src", str + attributes.get("data-src"));
            attributes.remove("data-src");

            //删除img标签中的width和height属性
            attributes.remove("width");
            attributes.remove("height");
        }

        String body = articleHolder.first().toString();

        //合并html
        return combineWebPage(head + body);
    }

    /**
     * 组合文章HTML源码
     *
     * @param content 文章主题源码
     * @return 返回组合后的源码
     */
    private String combineWebPage(String content) {
        if (content != null) {

            StringBuilder webPage = new StringBuilder();

            String head = "<!DOCTYPE html>\n" +
                    "<html lang=\"zh\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0 ,user-scalable=no\">\n";

            String css = "<style type=\"text/css\">" + readCSS() + "</style>";

            webPage.append(head + css + "</head>" + "<body>");

            webPage.append("<div class=\"head-container\">\n" +
                    "        <div class=\"banner-img-holder\">\n" +
                    "           <img src=\"" + article.coverUrl + "\">" +
                    "        </div>\n" +
                    "    </div>");

            webPage.append(content + "</body>\n</html>");

            return webPage.toString();
        }

        return null;
    }

    /**
     * 读取assets下的css文件
     *
     * @return 返回css源代码
     */
    private String readCSS() {
        try {
            InputStream inputStream = getApplicationContext().getAssets().open("index.css");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String temp;
            StringBuilder css = new StringBuilder();
            while ((temp = bufferedReader.readLine()) != null) {
                css.append(temp);
            }

            bufferedReader.close();
            inputStream.close();

            return css.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.article_imageView_back:
                this.finish();
                break;
            case R.id.article_imageView_more:

                //显示弹出菜单
                createPopupMenu(v);
                break;
        }
    }

    private void createPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(ArticleActivity.this, view);

        //获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.article_menu, popupMenu.getMenu());
        popupMenu.show();

        //item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.article_menu_screenshot:
                        //进行保存
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                boolean saveState = MediaUtils.saveArticle(article_webView, getApplicationContext());

                                Looper.prepare();
                                Toast.makeText(ArticleActivity.this, saveState ? "保存成功" : "保存失败", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }).start();

                        break;
                    case R.id.article_menu_jumpToOrigin:
                        //跳转到源网站收听
                        Intent intentOriginUrl = new Intent();
                        intentOriginUrl.setAction("android.intent.action.VIEW");
                        Uri uri = Uri.parse(Paths.articleWebPage + article.articleID);
                        intentOriginUrl.setData(uri);
                        startActivity(intentOriginUrl);
                        break;
                }

                return true;
            }
        });
    }
}