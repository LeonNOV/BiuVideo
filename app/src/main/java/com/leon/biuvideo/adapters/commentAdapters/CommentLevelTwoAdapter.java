package com.leon.biuvideo.adapters.commentAdapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.resourcesBeans.Comment;
import com.leon.biuvideo.ui.resourcesFragment.video.OnCommentListener;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Leon
 * @Time 2021/4/7
 * @Desc 二级评论适配器
 */
public class CommentLevelTwoAdapter extends BaseAdapter<Comment.LevelTwoComment> {
    private final List<Comment.LevelTwoComment> levelTwoCommentList;
    private ImageSpan imageSpan;

    private OnCommentListener onCommentListener;

    public CommentLevelTwoAdapter(List<Comment.LevelTwoComment> beans, Context context) {
        super(beans, context);
        this.levelTwoCommentList = beans;
    }

    public void setOnCommentListener(OnCommentListener onCommentListener) {
        this.onCommentListener = onCommentListener;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.comment_level2_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Comment.LevelTwoComment levelTwoComment = levelTwoCommentList.get(position);

        TextView commentLevel2ItemMessage = holder.findById(R.id.comment_level2_item_message);
        String msg;
        if (levelTwoComment.levelTwoReplayAtMap != null && levelTwoComment.levelTwoReplayAtMap.size() > 0) {
            if (levelTwoComment.levelTwoMessage.startsWith("@")) {
                msg = levelTwoComment.levelTwoName + " : " + levelTwoComment.levelTwoMessage;
            } else {
                msg = levelTwoComment.levelTwoName + " " + levelTwoComment.levelTwoMessage;
            }
        } else {
            msg = levelTwoComment.levelTwoName + " : " + levelTwoComment.levelTwoMessage;
        }

        createClickAndEmoji(msg, levelTwoComment, commentLevel2ItemMessage);
    }

    /**
     * 对评论者/回复者添加点击事件
     *
     * @param msg   消息
     * @param levelTwoComment    LevelTwoComment
     */
    private void createClickAndEmoji (String msg, Comment.LevelTwoComment levelTwoComment, TextView commentLevel2ItemMessage) {
        // 对评论者添加点击事件
        SpannableString spannableString = new SpannableString(msg);

        ClickableSpan commentClickable = new ClickableSpan() {
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(context.getColor(R.color.blue));
            }

            @Override
            public void onClick(@NonNull View widget) {
                if (onCommentListener != null) {
                    onCommentListener.navUserFragment(levelTwoComment.levelTowMid);
                }
            }
        };

        int commentStartIndex = msg.indexOf(levelTwoComment.levelTwoName);
        spannableString.setSpan(commentClickable, commentStartIndex, (commentStartIndex + levelTwoComment.levelTwoName.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 对回复接收者添加点击事件
        if (levelTwoComment.levelTwoReplayAtMap != null && levelTwoComment.levelTwoReplayAtMap.size() > 0) {
            for (Map.Entry<String, String> stringEntry : levelTwoComment.levelTwoReplayAtMap.entrySet()) {
                ClickableSpan commentReplayClickable = new ClickableSpan() {
                    @Override
                    public void updateDrawState(@NonNull TextPaint ds) {
                        ds.setColor(context.getColor(R.color.blue));
                    }

                    @Override
                    public void onClick(@NonNull View widget) {
                        if (onCommentListener != null) {
                            onCommentListener.navUserFragment(stringEntry.getKey());
                        }
                    }
                };

                String replay = "@" + stringEntry.getValue();
                int replayStartIndex = msg.indexOf(replay);

                if (replayStartIndex != -1) {
                    spannableString.setSpan(commentReplayClickable, replayStartIndex, (replayStartIndex + replay.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

        if (levelTwoComment.levelTwoEmojiMap != null && levelTwoComment.levelTwoEmojiMap.size() > 0) {
            for (Map.Entry<String, String> entry : levelTwoComment.levelTwoEmojiMap.entrySet()) {
                String emojiName = entry.getKey();

                Glide.with(context).load(entry.getValue()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        resource.setBounds(0, 0, CommentLevelOneAdapter.EMOJI_WH, CommentLevelOneAdapter.EMOJI_WH);

                        // 获取该Emoji出现的所有位置，Emoji名称需要进行转义
                        Pattern compile = Pattern.compile(emojiName.replaceAll("\\[", "\\\\[").replaceAll("]", "\\\\]"));
                        Matcher matcher = compile.matcher(msg);

                        while (matcher.find()) {
                            int startIndex = matcher.start();

                            if (startIndex != -1) {
                                imageSpan = new ImageSpan(resource, ImageSpan.ALIGN_BASELINE);
                                spannableString.setSpan(imageSpan, startIndex, (matcher.start() + emojiName.length()), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                            }
                        }

                        commentLevel2ItemMessage.setText(spannableString);
                        commentLevel2ItemMessage.setMovementMethod(LinkMovementMethod.getInstance());

                        return true;
                    }
                }).submit();
            }
        } else {
            commentLevel2ItemMessage.setText(spannableString);
            commentLevel2ItemMessage.setMovementMethod(LinkMovementMethod.getInstance());
        }

        commentLevel2ItemMessage.setText(spannableString);
        commentLevel2ItemMessage.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
