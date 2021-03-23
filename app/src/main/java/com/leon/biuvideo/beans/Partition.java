package com.leon.biuvideo.beans;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/23
 * @Desc 分区数据
 */
public class Partition {
    public String title;
    public String id;

    public List<Tag> tags;
    public static class Tag {
        public String title;
        public String id;
    }
}
