package com.leon.biuvideo.greendao.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @Author Leon
 * @Time 2021/4/1
 * @Desc 搜索历史数据
 */
@Entity
public class SearchHistory {
    @Id(autoincrement = true)
    private Long id;

    private Long hashCode;
    private String keyword;
    @Generated(hash = 100897936)
    public SearchHistory(Long id, Long hashCode, String keyword) {
        this.id = id;
        this.hashCode = hashCode;
        this.keyword = keyword;
    }
    @Generated(hash = 1905904755)
    public SearchHistory() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getHashCode() {
        return this.hashCode;
    }
    public void setHashCode(Long hashCode) {
        this.hashCode = hashCode;
    }
    public String getKeyword() {
        return this.keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
