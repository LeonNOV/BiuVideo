package com.leon.biuvideo.greendao.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * @Author Leon
 * @Time 2021/5/4
 * @Desc
 */
@Entity
public class DownloadLevelOne {
    @Id(autoincrement = true)
    private Long id;

    private String title;

    /**
     * 一级ID（bvid、seasonId等）
     */
    @Unique
    private String levelOneId;

    private String coverUrl;

    @Generated(hash = 1491009278)
    public DownloadLevelOne(Long id, String title, String levelOneId,
            String coverUrl) {
        this.id = id;
        this.title = title;
        this.levelOneId = levelOneId;
        this.coverUrl = coverUrl;
    }

    @Generated(hash = 2041732370)
    public DownloadLevelOne() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevelOneId() {
        return this.levelOneId;
    }

    public void setLevelOneId(String levelOneId) {
        this.levelOneId = levelOneId;
    }

    public String getCoverUrl() {
        return this.coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}
