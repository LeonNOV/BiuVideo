package com.leon.biuvideo.beans.searchResultBeans;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/30
 * @Desc 番剧搜索结果数据
 */
public class SearchResultBangumi {
    public String mediaId;
    public String seasonId;

    public String cover;
    public String badge;

    public String title;
    public String pubTime;
    public String areas;
    public String styles;

    public String score;
    public String userCount;

    public List<SearchResultBangumiEp> searchResultBangumiEpList;

    public static class SearchResultBangumiEp {
        public String epId;
        public String title;
        public String longTitle;
        public String badge;
        public boolean isVip;
    }
}
