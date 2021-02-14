package com.leon.biuvideo.utils;

import android.graphics.Color;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ValueUtils {
    /**
     * 生成以万结尾的字符串，小于1万则直接返回
     *
     * @param number    需要格式化的数
     * @return  返回结果
     */
    public static String generateCN(long number) {
        //确定数量区间
        if (number < 10000) {
            return String.valueOf(number);
        } else {
            //获取千位
            int thousand = (int) (number / 1000) % 10;

            //获取万位前面的数
            int tenThousand = (int) number / 10000;

            //组合为万位数
            return tenThousand + "." + thousand + "万";
        }
    }

    /**
     * 秒转换为一定格式的长度
     *
     * @param length    需要格式化的长度
     * @return  返回结果
     */
    public static String lengthGenerate(int length) {
        int minute = length / 60;
        int second = length % 60;

        String minuteStr = String.valueOf(minute);
        String secondStr = String.valueOf(second);

        minuteStr = minuteStr.length() < 2 ? "0" + minuteStr : minuteStr;
        secondStr = secondStr.length() < 2 ? "0" + secondStr : secondStr;

        return minuteStr + ":" + secondStr;
    }

    /**
     * 字节大小转换
     *
     * @param size  需要转换的大小(单位：byte)
     * @return  返回转换后的数据
     */
    public static String sizeFormat(long size, boolean withSuffix) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        if (size > 1024) {
            //转换为KB
            double kb = (double) size / 1024;

            if (kb > 1024) {
                double mb = kb / 1024;

                if (mb > 1024) {
                    double gb = mb / 1024;

                    if (gb > 1024) {
                        double tb = mb / 1024;

                        if (withSuffix)
                            return decimalFormat.format(tb) + "TB";

                        return decimalFormat.format(tb);
                    }

                    if (withSuffix)
                        return decimalFormat.format(gb) + "GB";
                    return decimalFormat.format(gb);
                }

                if (withSuffix)
                    return decimalFormat.format(mb) + "MB";
                return decimalFormat.format(mb);
            }

            if (withSuffix)
                return decimalFormat.format(kb) + "KB";
            return decimalFormat.format(kb);
        } else {
            if (withSuffix)
                return size + "B";
            return String.valueOf(size);
        }
    }

    /**
     * 格式化时间
     *
     * @param time  毫秒值/秒值
     * @param isSecond  是否为秒值
     * @param isMonth   是否只取月份（年/月/日）
     * @param delimiter     分隔符号
     * @return  返回格式化后的时间
     */
    public static String generateTime(long time, boolean isSecond, boolean isMonth, String delimiter) {
        String format = "yyyy" + delimiter + "MM" + delimiter + "dd HH:mm";
        Date date = new Date(isSecond ? time * 1000 : time);

        if (isMonth) {
            format = "yyyy" + delimiter + "MM" + delimiter + "dd";
        }

        return new SimpleDateFormat(format, Locale.CHINA).format(date);

    }

    public static String changeARGB(String hexColor) {
        return "#80" + hexColor.split("#")[1];
    }
}
