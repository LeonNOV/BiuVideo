package com.leon.biuvideo.utils;

import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpUtils {
    /**
     * get请求获取数据
     *
     * @param path  请求路径
     * @param params    请求参数
     * @return  返回响应体
     */
    public static String GETByParam(String path, Map<String, Object> params) {
        try {
            StringBuilder builder = new StringBuilder(path + "?");
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                builder
                        .append(URLEncoder.encode(key))
                        .append("=")
                        .append(URLEncoder.encode(String.valueOf(value)))
                        .append("&");
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
    }

    /**
     * 短链解析
     *
     * @param shortUrl  短链
     * @return  返回url后面的path
     */
    public static String parseShortUrl(String shortUrl) {
        CloseableHttpClient client = HttpClientBuilder.create().disableRedirectHandling().build();

        HttpHead request = null;
        try {
            request = new HttpHead(shortUrl);
            HttpResponse httpResponse = client.execute(request);

            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != 301 && statusCode != 302) {
                return shortUrl;
            }

            //获取响应头中的location的值
            Header[] headers = httpResponse.getHeaders(HttpHeaders.LOCATION);
            String location = headers[0].getValue();

            //使用uri对象来获取path
            URI uri = URI.create(location);
            String path = uri.getPath();

            return path;
        } catch (IllegalArgumentException | IOException uriEx) {
            return null;
        } finally {
            if (request != null) {
                request.releaseConnection();
            }
        }
    }
}
