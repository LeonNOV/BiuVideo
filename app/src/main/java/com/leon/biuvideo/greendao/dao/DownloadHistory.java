package com.leon.biuvideo.greendao.dao;

import com.leon.biuvideo.utils.downloadUtils.ResourceDownloadTask;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Author Leon
 * @Time 2021/4/30
 * @Desc 下载记录
 */
@Entity
public class DownloadHistory {
    @Id(autoincrement = true)
    private Long id;

    private int resType;

    private long taskId;
    private boolean isCompleted;
    private boolean isFailed;

    private String resKey;
    private String resStreamUrl;

    /**
     * 是否为多个选集
     */
    private boolean isMultipleAnthology;

    /**
     * 一级ID（bvid、seasonId等）
     */
    private String levelOneId;

    /**
     * 视频cid
     */
    private String levelTwoId;

    private String title;
    private String coverUrl;

    private String resPath;

    /**
     * 弹幕文件路径
     */
    private String danmakuFilePath;

    @Generated(hash = 198130773)
    public DownloadHistory(Long id, int resType, long taskId, boolean isCompleted,
            boolean isFailed, String resKey, String resStreamUrl,
            boolean isMultipleAnthology, String levelOneId, String levelTwoId,
            String title, String coverUrl, String resPath, String danmakuFilePath) {
        this.id = id;
        this.resType = resType;
        this.taskId = taskId;
        this.isCompleted = isCompleted;
        this.isFailed = isFailed;
        this.resKey = resKey;
        this.resStreamUrl = resStreamUrl;
        this.isMultipleAnthology = isMultipleAnthology;
        this.levelOneId = levelOneId;
        this.levelTwoId = levelTwoId;
        this.title = title;
        this.coverUrl = coverUrl;
        this.resPath = resPath;
        this.danmakuFilePath = danmakuFilePath;
    }

    @Generated(hash = 1916314204)
    public DownloadHistory() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getResType() {
        return this.resType;
    }

    public void setResType(@ResourceDownloadTask.ResourcesType int resType) {
        this.resType = resType;
    }

    public long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public boolean getIsCompleted() {
        return this.isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public boolean getIsFailed() {
        return this.isFailed;
    }

    public void setIsFailed(boolean isFailed) {
        this.isFailed = isFailed;
    }

    public String getResKey() {
        return this.resKey;
    }

    public void setResKey(String resKey) {
        this.resKey = resKey;
    }

    public String getResStreamUrl() {
        return this.resStreamUrl;
    }

    public void setResStreamUrl(String resStreamUrl) {
        this.resStreamUrl = resStreamUrl;
    }

    public boolean getIsMultipleAnthology() {
        return this.isMultipleAnthology;
    }

    public void setIsMultipleAnthology(boolean isMultipleAnthology) {
        this.isMultipleAnthology = isMultipleAnthology;
    }

    public String getLevelOneId() {
        return this.levelOneId;
    }

    public void setLevelOneId(String levelOneId) {
        this.levelOneId = levelOneId;
    }

    public String getLevelTwoId() {
        return this.levelTwoId;
    }

    public void setLevelTwoId(String levelTwoId) {
        this.levelTwoId = levelTwoId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return this.coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getResPath() {
        return this.resPath;
    }

    public void setResPath(String resPath) {
        this.resPath = resPath;
    }

    public String getDanmakuFilePath() {
        return this.danmakuFilePath;
    }

    public void setDanmakuFilePath(String danmakuFilePath) {
        this.danmakuFilePath = danmakuFilePath;
    }
}
