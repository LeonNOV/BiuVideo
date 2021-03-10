package com.leon.biuvideo.utils.parseDataUtils;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/10
 * @Desc 所有的解析工具类都应实现该接口，用于解析请求到的JSON数据
 */
public interface ParseInterface<T> {
    /**
     * 解析数据
     *
     * @return  返回数据集
     */
    List<T> parseData();
}
