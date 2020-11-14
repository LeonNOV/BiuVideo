package com.leon.biuvideo.utils;

public class ValueFormat {
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

        String minuteStr = minute < 10 ? "0" + minute : String.valueOf(minute);
        String secondStr = second < 10 ? "0" + second : String.valueOf(second);

        return minuteStr + ":" + secondStr;
    }
}
