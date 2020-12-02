package com.leon.biuvideo.utils;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Headers;

public class IDUtils {
    private static final int[] ints = {11, 10, 3, 8, 4, 6};
    private static final char[] chars = new char[58];

    static {
        String str = "fZodR9XQDSUm21yCkr6zBqiveYah8bt4xsWpHnJE7jL5VG3guMTKNPAwcF";
        for (int i = 0; i < str.length(); i++) {
            chars[i] = str.charAt(i);
        }
    }

    /**
     * 获取bvid
     *
     * @param value avid/含有bvid的链接/含有avid的链接
     * @return 返回bvid
     */
    public static String getBvid(String value) {
        //去除所有空格
        value = value.replace(" ", "");

        //判断原本是否就是bvid
        String regEx = "BV[\\w]{10}";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(value);

        if (matcher.matches()) {
            return value;
        }

        //判断是否为url
        if (verifyUrl(value)) {
            return parseUrl(value);
        } else {
            long avid;

            try {
                //判断是否为avid
                if (value.startsWith("av")) {
                    String substring = value.substring(2);
                    avid = Long.parseLong(substring);
                } else {
                    avid = Long.parseLong(value);
                }

                return avidToBvid(avid);
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * 获取mid
     *
     * @param value 需要处理的值
     * @return  返回mid
     */
    public static long getMid(String value) {
        Pattern pattern;
        Matcher matcher;

        if (value.startsWith("UID:")) {
            value = value.replaceAll("UID:", "");
        }

        //判断原本的内容是否为mid
        String regEx = "[\\d]{7,9}";
        pattern = Pattern.compile(regEx);
        matcher = pattern.matcher(value);

        if (matcher.matches()) {
            return Long.parseLong(value);
        }

        //判断是否为url,提前去除参数部分
        value = value.split("\\?")[0];
        if (verifyUrl(value)) {
            //去除URL参数部分
            String mid = parseUrl(value);

            return Long.parseLong(mid);
        }

        return 0;
    }

    /**
     * avid转bvid算法
     *
     * @param avid  需要转换的avid
     * @return  返回bvid
     */
    private static String avidToBvid(long avid) {
        long xor = 177451812;
        long add = 8728348608L;
        long avid2 = (avid ^ xor) + add;
        char[] baseArray = {'B', 'V', '1', ' ', ' ', '4', ' ', '1', ' ', '7', ' ', ' '};

        for (int i = 0; i < 6; i++) {
            double s2 = Math.floor(avid2 / Math.pow(58, i));
            double index = s2 % 58;

            baseArray[ints[i]] = chars[(int) index];
        }

        return String.valueOf(baseArray);
    }

    /**
     * 验证是否是URL
     *
     * @param url   需要验证的url
     * @return  返回一个boolean变量
     */
    private static boolean verifyUrl(String url) {
        // URL验证规则
        String regEx = "^((https|http)?://)[^\\s]+";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(url);

        return matcher.matches();
    }

    /**
     * 链接解析
     *
     * @param url 要解析的url
     * @return 返回解析的bvid
     */
    private static String parseUrl(String url) {
        String path;

        //判断是否为短链
        if (url.contains("https://b23.tv/")) {
            HttpUtils httpUtils = new HttpUtils(url, Headers.of("Host", "b23.tv"), null);
            path = httpUtils.parseShortUrl();
        } else {
            //否则按正常url来解析
            URI uri = URI.create(url);

            String host = uri.getHost();

            //判断是否为用户host
            if (host.equals("space.bilibili.com")) {
                //移除数字以外的其他字符
                String regEx = "[^0-9]";
                Pattern pattern = Pattern.compile(regEx);
                Matcher matcher = pattern.matcher(uri.getPath());
                return matcher.replaceAll("").trim();
            } else {
                //判断host是否为b站
                if (!host.equals("www.bilibili.com")) {
                    return null;
                }
            }

            path = uri.getPath();
        }

        String id = path.split("/")[2];

        //判断id是否为avid
        if (id.startsWith("av")) {
            String str = id.substring(2);
            long avid = Long.parseLong(str);

            return avidToBvid(avid);
        } else {
            String str = path.split("/")[2];

            //判断是否以BV开头
            if (str.startsWith("BV")) {
                return str;
            }

            return null;
        }
    }
}
