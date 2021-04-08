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

    public String oid;

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
    public List<LevelTwoComment> levelTwoCommentList;

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

        /**
         * 如果@了其他人，则该处不为null
         */
        public Map<String, String> contentMembers;
    }

    /**
     * 二级评论
     * 示例
     *      AAA : 回复 @BBB :卢本伟牛逼！！！
     */
    public static class LevelTwoComment {
        public String levelTowMid;
        public String levelTwoName;
        public String levelTwoMessage;
        public Map<String, String> levelTwoEmojiMap;

        public Map<String, String> levelTwoReplayAtMap;
    }
}
