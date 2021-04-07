package com.leon.biuvideo.beans.mediaBeans;

import com.leon.biuvideo.beans.upMasterBean.BiliUserInfo;

import java.util.List;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/4/7
 * @Desc 评论数据
 */
public class Comment {
    /**
     * 评论ID
     */
    public String rpid;
    public String mid;

    /**
     * 根评论ID
     * 如果为一级评论则为0，大于一级则为评论ID
     */
    public String root;

    /**
     * 回复父评论ID
     * 如果为一级评论则为0，大于一级则为评论ID
     *
     */
    public String parent;

    /**
     * 回复对方评论ID
     * 若为一级则为0，若为二级则为该评论ID，大于二级评论为上一级评论ID
     *
     */
    public String dialog;

    /**
     * 二级评论数
     *
     */
    public int count;

    /**
     * 回复评论数
     *
     */
    public int rcount;

    /**
     * 发送时间
     */
    public long sendTime;

    public int like;

    /**
     * 已登录用户对该条评论是否已点赞
     *
     */
    public boolean action;

    /**
     * 用户信息
     */
    public BiliUserInfo userInfo;

    /**
     * 评论内容
     */
    public Content content;

    /**
     * 该评论回复信息
     */
    public List<SubComment> subCommentList;

    /**
     * UP点赞状态
     */
    public boolean upLike;

    /**
     * UP回复状态
     */
    public boolean upReply;

    public static class Content {
        /**
         * 评论内容
         */
        public String message;

        /**
         * 评论Emoji
         */
        public Map<String, String> emojiMap;
    }

    public static class SubComment {
        public String userMid;
        public String userName;
        public String message;
        public Map<String, String> emojiMap;

        public String replayUserName;
        public String replayUserMid;
    }
}
