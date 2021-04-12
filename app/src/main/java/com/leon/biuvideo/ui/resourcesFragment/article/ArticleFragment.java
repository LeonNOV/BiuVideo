package com.leon.biuvideo.ui.resourcesFragment.article;

import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.mediaBeans.ArticleInfo;
import com.leon.biuvideo.beans.mediaBeans.BiliUserInfo;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.otherFragments.biliUserFragments.BiliUserFragment;
import com.leon.biuvideo.utils.BindingUtils;
import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.ArticleInfoParser;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.BiliUserParser;
import com.leon.biuvideo.values.FeaturesName;
import com.leon.biuvideo.values.ImagePixelSize;
import com.leon.biuvideo.values.apis.BiliBiliAPIs;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import okhttp3.Headers;

/**
 * @Author Leon
 * @Time 2021/4/11
 * @Desc 专栏页面
 */
public class ArticleFragment extends BaseSupportFragment implements View.OnClickListener {
    private final String articleId;
    private BiliUserInfo biliUserInfo;

    public ArticleFragment(String articleId) {
        this.articleId = articleId;
    }

    @Override
    protected int setLayout() {
        return R.layout.article_fragment;
    }

    @Override
    protected void initView() {
        findView(R.id.article_back).setOnClickListener(this);

        ImageView articleFace = findView(R.id.article_face);
        articleFace.setOnClickListener(this);

        TextView articleUserFollow = findView(R.id.article_user_follow);
        articleUserFollow.setOnClickListener(this);

        WebView articleWebView = findView(R.id.article_webView);
        WebSettings settings = articleWebView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);

        // 将图片调整到适合webView的大小
        settings.setUseWideViewPort(true);

        // 支持自动加载图片
        settings.setLoadsImagesAutomatically(true);

        // 设置编码格式
        settings.setDefaultTextEncodingName("utf-8");
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDefaultFontSize(20);

        TextView articleWriteComment = findView(R.id.article_write_comment);
        articleWriteComment.setOnClickListener(this);

        findView(R.id.article_like_container).setOnClickListener(this);
        findView(R.id.article_comment_container).setOnClickListener(this);
        findView(R.id.article_favorite_container).setOnClickListener(this);

        ImageView articleLikeStatus = findView(R.id.article_like_status);
        ImageView articleFavoriteStatus = findView(R.id.article_favorite_status);

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                switch (msg.what) {
                    case 0:
                        ArticleInfo articleInfo = (ArticleInfo) msg.obj;
                        if (articleInfo != null) {
                            articleUserFollow.setSelected(articleInfo.attentionStatus);

                            BindingUtils bindingUtils = new BindingUtils(view, context);
                            bindingUtils
                                    .setImage(R.id.article_banner, articleInfo.banner, ImagePixelSize.COVER)
                                    .setText(R.id.article_title, articleInfo.title)
                                    .setText(R.id.article_like, articleInfo.like)
                                    .setText(R.id.article_comment, articleInfo.comment)
                                    .setText(R.id.article_favorite, articleInfo.favorite);
                            articleFavoriteStatus.setSelected(articleInfo.favoriteStatus);
                            articleLikeStatus.setSelected(articleInfo.likeStatus);

                            getUserInfo(articleInfo.mid);
                        }

                        break;
                    case 1:
                        biliUserInfo = (BiliUserInfo) msg.obj;

                        if (biliUserInfo != null) {
                            Glide
                                    .with(context)
                                    .load(biliUserInfo.userFace += PreferenceUtils.getFeaturesStatus(FeaturesName.IMG_ORIGINAL_MODEL) ?
                                            ImagePixelSize.FACE.value : "")
                                    .into(articleFace);

                            bindingUtils.setText(R.id.article_user_name, biliUserInfo.userName);
                        }

                        getArticleContent(BiliBiliAPIs.ARTICLE_PAGE_PATH + articleId);
                        break;
                    case 2:
                        String unEncodedHtml = (String) msg.obj;

                        // 原HTML是未进行编码的，如果android版本为8.0以上（不包括8.0）,则需要将其解码为base64
                        String encodedHtml = Base64.encodeToString(unEncodedHtml.getBytes(), Base64.NO_PADDING);
                        articleWebView.loadData(encodedHtml, "text/html", "base64");
                        break;
                    default:
                        break;
                }
            }
        });

        getArticleInfo();
    }

    /**
     * 获取专栏基本数据
     */
    private void getArticleInfo() {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                ArticleInfo articleInfo = ArticleInfoParser.parseData(articleId);

                Message message = receiveDataHandler.obtainMessage(0);
                message.obj = articleInfo;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    /**
     * 获取用户数据
     *
     * @param mid MID
     */
    private void getUserInfo(String mid) {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                BiliUserInfo userInfo = BiliUserParser.getUserInfo(mid);

                Message message = receiveDataHandler.obtainMessage(1);
                message.obj = userInfo;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    /**
     * 获取文章内容
     *
     * @param path 专栏链接
     */
    public void getArticleContent(String path) {
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                String articleContent = parseHTMLonPhone(path);

                Message message = receiveDataHandler.obtainMessage(2);
                message.obj = articleContent;
                receiveDataHandler.sendMessage(message);
            }
        });
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

            String css = "<style type=\"text/css\">" + FileUtils.getAssetsContent(context, "index.css") + "</style>";

            webPage.append(head + css + "</head>" + "<body>");

            //添加头部图片
            /*webPage.append("<div class=\"head-container\">\n" +
                    "        <div class=\"banner-img-holder\">\n" +
                    "           <img src=\"" + favoriteArticle.coverUrl + "\">" +
                    "        </div>\n" +
                    "    </div>");*/

            webPage.append(content + "</body>\n</html>");

            return webPage.toString();
        }

        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.article_back:
                backPressed();
                break;
            case R.id.article_face:
                start(new BiliUserFragment(biliUserInfo.userMid));
                break;
            case R.id.article_user_follow:
                break;
            case R.id.article_like_container:
                break;
            case R.id.article_comment_container:
                break;
            case R.id.article_favorite_container:
                break;
            default:
                break;
        }
    }
}
