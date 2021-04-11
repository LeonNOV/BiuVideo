package com.leon.biuvideo.ui.resourcesFragment.video;

import com.leon.biuvideo.beans.mediaBeans.Comment;

/**
 * @Author Leon
 * @Time 2021/4/8
 * @Desc
 */
public interface OnCommentListener {
    /**
     * 查看一级评论下的所有二级评论
     *
     * @param comment   comment
     */
    void onClick (Comment comment);

    /**
     * 进入用户主页
     *
     * @param mid   用户MID
     */
    void navUserFragment (String mid);
}
