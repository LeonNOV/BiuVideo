package com.leon.biuvideo.utils;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.leon.biuvideo.beans.videoBean.play.Play;
import com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils.MediaParser;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParseUtils.MusicUrlParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtils {
    private final String url;
    private final Headers headers;
    private final Map<String, String> params;

    public HttpUtils(String url, Map<String, String> params) {
        this.url = url;
        this.params = params;
        this.headers = Headers.of(getHeaders());
    }

    public HttpUtils(String url, Headers headers, Map<String, String> params) {
        this.url = url;
        this.headers = headers;
        this.params = params;
    }

    /**
     * 短链解析
     *
     * @return  返回短链解析结果
     */
    public String parseShortUrl() {
        try {
            return getInstance().request().url().url().toURI().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 向接口请求数据/获取网页源码

     * @return  返回响应数据
     */
    public String getData() {
        try {
            return getInstance().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取实例对象
     *
     * @return  返回Response对象
     */
    private Response getInstance () {
        Request request;
        OkHttpClient okHttpClient = new OkHttpClient();

        Request.Builder requestBuilder = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();

        //添加参数
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        try {

            if (headers != null) {
                requestBuilder.headers(headers);
            } else {
                requestBuilder.headers(Headers.of(getHeaders()));
            }

            request = requestBuilder.url(urlBuilder.build()).get().build();

            Call call = okHttpClient.newCall(request);
            Response response = call.execute();

            //判断是否响应成功
            if (response.code() == HttpURLConnection.HTTP_OK) {
                return response;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取接口响应体
     *
     * @param path  接口地址
     * @param params    参数
     * @return  将响应体已JSONObject对象的形式返回
     */
    public static JSONObject getResponse(String path, Map<String, String> params) {
        String response = new HttpUtils(path, params).getData();

        return JSON.parseObject(response);
    }

    /**
     * 获取接口响应体
     *
     * @param path  接口地址
     * @param params    参数
     * @return  将响应体已JSONObject对象的形式返回
     */
    public static JSONObject getResponse(String path, Headers headers, Map<String, String> params) {
        String response = new HttpUtils(path, headers, params).getData();

        return JSON.parseObject(response);
    }

    /**
     * 获取媒体资源链接
     *
     * @param mainId    主ID（bvid， sid）
     * @param subId 子ID（cid）
     * @return  返回资源链接
     * <br/>
     * 如果为视频，则索引0为视频连接，索引1为音频链接
     * <br/>
     * 如果为音频，则索引0就是音频链接
     */
    public static String[] reacquireMediaUrl(Context context, String mainId, long subId, int qualityId, boolean isBangumi) {
        String[] urls;
        if (subId != 0) {
            urls = new String[2];

            MediaParser mediaParser = new MediaParser(context);
            Play play = mediaParser.parseMedia(mainId, subId, isBangumi);

            if (play != null) {
                urls[0] = play.videos.get(qualityId).baseUrl;
                urls[1] = play.audioEntries().get(0).getValue().baseUrl;
            }

        } else {
            urls = new String[1];
            MusicUrlParser musicUrlParser = new MusicUrlParser(context);
            urls[0] = musicUrlParser.parseMusicUrl(mainId);;
        }

        return urls;
    }

    /**
     * 设置头信息
     *
     * @return  返回头信息
     */
    public static HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36 Edg/86.0.622.51");
        headers.put("Connection", "keep-alive");
        headers.put("Referer", "https://www.bilibili.com/");

        return headers;
    }
}
