package com.leon.biuvideo.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.leon.biuvideo.beans.mediaBeans.Comment;
import com.leon.biuvideo.utils.Fuck;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Leon
 * @Time 2021/4/7
 * @Desc 二级评论适配器
 */
public class CommentLevel2Adapter extends BaseAdapter<Comment.SubComment> {
    private final List<Comment.SubComment> subCommentList;

    public CommentLevel2Adapter(List<Comment.SubComment> beans, Context context) {
        super(beans, context);
        this.subCommentList = beans;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.comment_level2_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Comment.SubComment subComment = subCommentList.get(position);

        TextView commentLevel2ItemMessage = holder.findById(R.id.comment_level2_item_message);
        String msg;
        if (subComment.replayUserName != null) {
            msg = subComment.userName + " " + subComment.message;
        } else {
            msg = subComment.userName + " : " + subComment.message;
        }

        createClickAndEmoji(msg, subComment, commentLevel2ItemMessage);
    }

    /**
     * 对评论者/回复者添加点击事件
     *
     * @param msg   消息
     * @param subComment    SubComment
     */
    private void createClickAndEmoji (String msg, Comment.SubComment subComment, TextView commentLevel2ItemMessage) {
        // 对评论者添加点击事件
        SpannableString spannableString = new SpannableString(msg);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(context.getColor(R.color.blue));
            }

            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(context, "回复者：" + subComment.userName, Toast.LENGTH_SHORT).show();
            }
        };

        int commentStartIndex = msg.indexOf(subComment.userName);
        spannableString.setSpan(clickableSpan, commentStartIndex, commentStartIndex + subComment.userName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        // 对回复接收者添加点击事件
        if (subComment.replayUserName != null) {
            String replay = "@" + subComment.replayUserName;
            Fuck.blue("replay：" + replay);
            int replayStartIndex = msg.indexOf(replay);
            Fuck.blue("replayStartIndex：" + replayStartIndex + "replayEndIndex：" + replayStartIndex + replay.length());

            if (replayStartIndex < 0) {
                commentLevel2ItemMessage.setText(spannableString);
                return;
            }
            spannableString.setSpan(clickableSpan, replayStartIndex, replayStartIndex + replay.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        if (subComment.emojiMap != null && subComment.emojiMap.size() > 0) {
            for (Map.Entry<String, String> entry : subComment.emojiMap.entrySet()) {
                String emojiName = entry.getKey();

                Glide.with(context).load(entry.getValue()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        resource.setBounds(0, 0, CommentLevel1Adapter.EMOJI_WH, CommentLevel1Adapter.EMOJI_WH);
                        ImageSpan imageSpan = new ImageSpan(resource, ImageSpan.ALIGN_BOTTOM);

                        // 获取该Emoji出现的所有位置，Emoji名称需要进行转义
                        Pattern compile = Pattern.compile(emojiName.replaceAll("\\[", "\\\\[").replaceAll("]", "\\\\]"));
                        Matcher matcher = compile.matcher(msg);

                        List<int[]> indexList = new ArrayList<>();
                        while (matcher.find()) {
                            int[] startAndEnd = new int[2];

                            startAndEnd[0] = matcher.start();
                            startAndEnd[1] = matcher.start() + emojiName.length();

                            indexList.add(startAndEnd);
                        }

                        for (int[] startAndEnd : indexList) {
                            spannableString.setSpan(imageSpan, startAndEnd[0], startAndEnd[1], Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        }

                        commentLevel2ItemMessage.setText(spannableString);

                        return false;
                    }
                }).submit();
            }
        }
    }
}
