package com.leon.biuvideo.utils;

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
    private String url;
    private Headers headers;
    private Map<String, String> params;

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
     * get请求获取数据
     *
     * @param path  请求路径
     * @param params    请求参数
     * @return  返回响应体
     */
    /*public static String GETByParam(String path, Map<String, Object> params) {
        try {
            StringBuilder builder = new StringBuilder(path + "?");

            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    builder
                            .append(URLEncoder.encode(key, "utf-8"))
                            .append("=")
                            .append(URLEncoder.encode(value.toString(), "utf-8"))
                            .append("&");
                }
            }

            builder.deleteCharAt(builder.length() - 1);

            URL url = new URL(builder.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.80 Safari/537.36 Edg/86.0.622.43");
            connection.setRequestProperty("Referer", "https://www.bilibili.com/");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                StringBuilder responseBuilder = new StringBuilder();
                String temp;
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                while ((temp = bufferedReader.readLine()) != null) {
                    responseBuilder.append(temp);
                }

                bufferedReader.close();
                connection.disconnect();

                return responseBuilder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }*/

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

            if (headers == null) {
                requestBuilder.headers(Headers.of(getHeaders()));
            } else {
                requestBuilder.headers(headers);
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
