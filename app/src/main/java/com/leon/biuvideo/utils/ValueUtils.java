package com.leon.biuvideo.utils;

public class ValueUtils {
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
            String result = tenThousand + "." + thousand + "万";
            return result;
        }
    }
}
