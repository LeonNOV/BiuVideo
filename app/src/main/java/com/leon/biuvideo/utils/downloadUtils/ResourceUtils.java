package com.leon.biuvideo.utils.downloadUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.webkit.WebView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.leon.biuvideo.utils.FileUtils;
import com.leon.biuvideo.utils.HttpUtils;
import com.leon.biuvideo.utils.InternetUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.UUID;

/**
 * 保存图片等资源
 */
public class ResourceUtils {

    /**
     * 保存图片资源至系统相册
     *
     * @param context   上下文对象
     * @param picUrl    图片链接
     * @return  返回保存成功状态
     */
    public static boolean savePicture(Context context, String picUrl) {
        //判断是否有网络
        boolean isHaveNetwork = InternetUtils.checkNetwork(context);

        if (!isHaveNetwork) {
            return false;
        }

        try {
            URL url = new URL(picUrl);

            HttpURLConnection connection = (HttpURLConnection)  url.openConnection();

            //获取&设置请求头
            HashMap<String, String> headers = HttpUtils.getHeaders();
            connection.setRequestProperty("User-Agent", headers.get("User-Agent"));
            connection.setRequestProperty("Referer", headers.get("Referer"));

            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            //获取bitmap对象
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            //创建保存picFile类
            //图片文件名称使用UUID进行命名
            File picFile = new File(FileUtils.createFolder(FileUtils.ResourcesFolder.PICTURES), UUID.randomUUID().toString() + ".jpeg");

            //获取输出流
            FileOutputStream fileOutputStream = new FileOutputStream(picFile);

            boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

            fileOutputStream.close();
            inputStream.close();
            connection.disconnect();

            //保存图片后发送广播，通知刷新图库的显示
            sendBroadcast(context, new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(picFile)));

            return isSuccess;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 将webView中的内容转换为图片
     *
     * @param webView   webView对象
     * @param context   context
     * @return 返回截图保存状态
     */
    public static boolean saveArticle(WebView webView, Context context) {
        Bitmap bitmap = Bitmap.createBitmap(webView.getWidth(), webView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        webView.draw(canvas);

        try {
            File articlePic = new File(FileUtils.createFolder(FileUtils.ResourcesFolder.PICTURES), FileUtils.generateFileName("article") + ".jpeg");

            FileOutputStream fos = new FileOutputStream(articlePic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);

            fos.close();

            //发送广播
            sendBroadcast(context, new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(articlePic)));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 发送广播，通知本地资源库(视频、图片、音乐)进行更新
     *
     * @param context   context对象
     * @param intent    intent对象
     */
    public static void sendBroadcast(Context context, Intent intent) {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(context);
        broadcastManager.sendBroadcast(intent);
    }

    /**
     * 获取资源大小
     *
     * @param resourceUrl   资源链接
     * @return  返回资源大小
     */
    public static long getResourcesSize(String resourceUrl) {
        try {
            URL url = new URL(resourceUrl);

            URLConnection urlConnection = url.openConnection();

            //设置头信息
            HashMap<String, String> headers = HttpUtils.getHeaders();
            urlConnection.setRequestProperty("User-Agent", headers.get("User-Agent"));
            urlConnection.setRequestProperty("Referer", headers.get("Referer"));

            urlConnection.connect();

            return urlConnection.getContentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
