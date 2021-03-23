package com.leon.biuvideo.ui.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.homeBeans.favoriteBeans.FavoriteArticle;
import com.leon.biuvideo.beans.orderBeans.LocalOrder;
import com.leon.biuvideo.ui.dialogs.LoadingDialog;
import com.leon.biuvideo.ui.views.RoundPopupWindow;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.dataBaseUtils.LocalOrdersDatabaseUtils;
import com.leon.biuvideo.utils.downloadUtils.ResourceUtils;
import com.leon.biuvideo.values.LocalOrderType;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;
import com.leon.biuvideo.utils.ValueUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;

/**
 * 专栏界面Activity
 */
public class ArticleActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView article_textView_title;
    private ImageView article_imageView_face, article_imageView_more;
    private WebView article_webView;
    private TextView
            article_textView_author,
            article_textView_category,
            article_textView_ctime,
            article_textView_view,
            article_textView_like,
            article_textView_replay;

    private FavoriteArticle favoriteArticle;
    private LocalOrdersDatabaseUtils localOrdersDatabaseUtils;

    private boolean isHaveLocalOrder = false;
    private final static LocalOrderType localOrderType = LocalOrderType.ARTICLE;
    private LinearLayout article_linearLayout;
    private LoadingDialog loadingDialog;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        initView();
    }

    private void initView() {
        article_linearLayout = findViewById(R.id.article_linearLayout);

        article_imageView_face = findViewById(R.id.article_imageView_face);
        article_imageView_face.setOnClickListener(this);

        article_textView_author = findViewById(R.id.article_textView_author);

        article_textView_title = findViewById(R.id.article_textView_title);

        ImageView article_imageView_back = findViewById(R.id.article_imageView_back);
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

        loadingDialog = new LoadingDialog(ArticleActivity.this);
        loadingDialog.show();

        loadData();
    }

    private void loadData() {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                favoriteArticle = (FavoriteArticle) extras.getSerializable("favoriteArticle");
                boolean isHistory = extras.getBoolean("isHistory", false);

                Message message = handler.obtainMessage();
                message.what = 0;

                Bundle bundle = new Bundle();
                bundle.putBoolean("loadState", true);
                bundle.putBoolean("isHistory", isHistory);

                message.setData(bundle);
                handler.sendMessage(message);
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                boolean loadState = msg.getData().getBoolean("loadState");
                boolean isHistory = msg.getData().getBoolean("isHistory");

                if (loadState) {
                    initValue(isHistory);
                }

                loadingDialog.dismiss();

                return true;
            }
        });
    }

    private void initValue(boolean isHistory) {
        //设置头像
        Glide.with(getApplicationContext()).load(favoriteArticle.face).into(article_imageView_face);

        //设置作者名称
        article_textView_author.setText(favoriteArticle.author);

        //设置标题
        article_textView_title.setText(favoriteArticle.title);

        if (isHistory) {
            article_textView_category.setVisibility(View.GONE);
            article_textView_ctime.setVisibility(View.GONE);
        } else {
            //设置文章分类
            article_textView_category.setText(favoriteArticle.category);

            //设置创建时间
            article_textView_ctime.setText(ValueUtils.generateTime(favoriteArticle.ctime, true, true, "-"));
        }

        //设置观看量
        String viewStr = ValueUtils.generateCN(favoriteArticle.view) + "次阅读";
        article_textView_view.setText(viewStr);

        //设置点赞数
        String likeStr = ValueUtils.generateCN(favoriteArticle.like) + "次点赞";
        article_textView_like.setText(likeStr);

        //设置评论数
        String replayStr = ValueUtils.generateCN(favoriteArticle.reply) + "次评论";
        article_textView_replay.setText(replayStr);

        String path = BiliBiliAPIs.articleWebPage + favoriteArticle.articleId;

        //获取文章页面
        String unencodedHtml = parseHTMLonPhone(path);

        /**
         * 加载文章内容
         * 注意：原HTML是未进行编码的，如果android版本为8.0以上（不包括8.0）,则需要将其解码为base64
         */
        String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(), Base64.NO_PADDING);
        article_webView.loadData(encodedHtml, "text/html", "base64");

        localOrdersDatabaseUtils = new LocalOrdersDatabaseUtils(getApplicationContext());
        isHaveLocalOrder = localOrdersDatabaseUtils.queryLocalOrder(String.valueOf(favoriteArticle.articleId), null, localOrderType);
        Fuck.blue(isHaveLocalOrder + "");
    }

    /**
     * 获取文章内容
     *
     * @param path 文章地址
     * @return 返回文章HTML源码
     */
    public String parseHTMLonPhone(String path) {
        HttpUtils httpUtils = new HttpUtils(path, Headers.of(HttpUtils.getHeaders()), null);

        String html = httpUtils.getData();
        Document document = Jsoup.parse(html);

        //获取文章主要部分
        Elements articleHolder = document.getElementsByClass("favoriteArticle-holder");

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
        return combineWebPage(body);
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

            String css = "<style type=\"text/css\">" + FileUtils.getAssetsContent(getApplicationContext(), "index.css") + "</style>";

            webPage.append(head + css + "</head>" + "<body>");

            //添加头部图片
            webPage.append("<div class=\"head-container\">\n" +
                    "        <div class=\"banner-img-holder\">\n" +
                    "           <img src=\"" + favoriteArticle.coverUrl + "\">" +
                    "        </div>\n" +
                    "    </div>");

            webPage.append(content + "</body>\n</html>");

            return webPage.toString();
        }

        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.article_imageView_face:
                if (!InternetUtils.checkNetwork(getApplicationContext())) {
                    SimpleSnackBar.make(v, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();
                    break;
                }

                Intent intent = new Intent(this, UserActivity.class);
                intent.putExtra("mid", favoriteArticle.mid);
                startActivity(intent);

                break;
            case R.id.article_imageView_back:
                this.finish();
                break;
            case R.id.article_imageView_more:
                //显示弹出菜单
                RoundPopupWindow roundPopupWindow = new RoundPopupWindow(getApplicationContext(), article_imageView_more);
                roundPopupWindow
                        .setContentView(R.layout.article_more_menu)
                        .setText(R.id.article_more_menu_favorite, isHaveLocalOrder ? "取消收藏" : "收藏该文章")
                        .setOnClickListener(R.id.article_more_menu_savePic, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //进行保存
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        boolean saveState = ResourceUtils.saveArticle(article_webView, getApplicationContext());
                                        SimpleSnackBar.make(article_linearLayout, saveState ? "保存成功" : "保存失败", SimpleSnackBar.LENGTH_SHORT).show();
                                    }
                                }).start();

                                roundPopupWindow.dismiss();
                            }
                        })
                        .setOnClickListener(R.id.article_more_menu_jumpToOrigin, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //跳转到源网站
                                Intent intentOriginUrl = new Intent();
                                intentOriginUrl.setAction("android.intent.action.VIEW");
                                Uri uri = Uri.parse(BiliBiliAPIs.articleWebPage + favoriteArticle.articleId);
                                intentOriginUrl.setData(uri);
                                startActivity(intentOriginUrl);

                                roundPopupWindow.dismiss();
                            }
                        })
                        .setOnClickListener(R.id.article_more_menu_favorite, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                boolean operatingStatus;

                                if (isHaveLocalOrder) {
                                    operatingStatus = localOrdersDatabaseUtils.deleteLocalOrder(String.valueOf(favoriteArticle.articleId), null, localOrderType);
                                    if (operatingStatus) {
                                        roundPopupWindow.setText(R.id.article_more_menu_favorite, "收藏该文章");
                                        SimpleSnackBar.make(v, R.string.remFavoriteSign, SimpleSnackBar.LENGTH_SHORT).show();
                                        isHaveLocalOrder = false;
                                    }
                                } else {
                                    LocalOrder localOrder = new LocalOrder();
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("face", favoriteArticle.face);
                                    map.put("title", favoriteArticle.title);
                                    map.put("summary", favoriteArticle.summary);
                                    map.put("author", favoriteArticle.author);
                                    map.put("cover", favoriteArticle.coverUrl);
                                    map.put("category", favoriteArticle.category);
                                    map.put("ctime", favoriteArticle.ctime);
                                    map.put("favoriteTime", favoriteArticle.favoriteTime);
                                    map.put("view", favoriteArticle.view);
                                    map.put("like", favoriteArticle.like);
                                    map.put("reply", favoriteArticle.reply);
                                    localOrder.jsonObject = new JSONObject(map);
                                    localOrder.mainId = String.valueOf(favoriteArticle.articleId);
                                    localOrder.subId = String.valueOf(favoriteArticle.mid);
                                    localOrder.orderType = localOrderType;
                                    localOrder.addTime = System.currentTimeMillis();

                                    operatingStatus = localOrdersDatabaseUtils.addLocalOrder(localOrder);

                                    if (operatingStatus) {
                                        roundPopupWindow.setText(R.id.article_more_menu_favorite, "取消收藏");
                                        SimpleSnackBar.make(v, R.string.addFavoriteSign, SimpleSnackBar.LENGTH_SHORT).show();
                                        isHaveLocalOrder = true;
                                    }
                                }
                            }
                        })
                        .setLocation(RoundPopupWindow.SHOW_AS_DROP_DOWN)
                        .create();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (localOrdersDatabaseUtils != null) {
            localOrdersDatabaseUtils.close();
        }
        super.onDestroy();
    }
}