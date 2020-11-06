package com.leon.biuvideo.utils;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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
    }

    /**
     * 设置头信息
     * 属于冗余，待清理
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

            return uri.getPath();
        } catch (IllegalArgumentException | IOException uriEx) {
            return null;
        } finally {
            if (request != null) {
                request.releaseConnection();
            }
        }
    }

    /**
     * 网页源代码获取
     *
     * @param path  链接
     * @return  返回HTML源码
     */
    public static String GetHtmlSrc(String path) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(path);

        //设置请求头
        for (Map.Entry<String, String> entry : getHeaders().entrySet()) {
            httpGet.setHeader(entry.getKey(), entry.getValue());
        }

        CloseableHttpResponse response;
        try {
            response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {
                if (response.getEntity() != null) {
                    String html = EntityUtils.toString(response.getEntity());

                    return html;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
