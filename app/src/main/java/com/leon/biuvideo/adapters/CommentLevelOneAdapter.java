package com.leon.biuvideo.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.mediaBeans.Comment;
import com.leon.biuvideo.ui.resourcesFragment.OnCommentListener;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.FeaturesName;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.List;
import java.util.Map;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Leon
 * @Time 2021/4/7
 * @Desc 一级评论适配器
 */
public class CommentLevelOneAdapter extends BaseAdapter<Comment> {
    public static final int EMOJI_WH = 66;
    private final List<Comment> commentList;
    private ImageSpan imageSpan;

    private OnCommentListener onCommentListener;

    public CommentLevelOneAdapter(List<Comment> beans, Context context) {
        super(beans, context);
        this.commentList = beans;
    }

    public void setOnCommentListener(OnCommentListener onCommentListener) {
        this.onCommentListener = onCommentListener;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.comment_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        ImageView commentItemUserFace = holder.findById(R.id.comment_item_userFace);
        ImageView commentItemVerifyMark = holder.findById(R.id.comment_item_verifyMark);
        switch (comment.userInfo.role) {
            case PERSON:
                commentItemVerifyMark.setImageResource(R.drawable.ic_person_verify);
                break;
            case OFFICIAL:
                commentItemVerifyMark.setImageResource(R.drawable.ic_official_verify);
                break;
            default:
                commentItemVerifyMark.setVisibility(View.GONE);
                break;
        }
        Glide
                .with(context)
                .load(comment.userInfo.userFace +=
                        PreferenceUtils.getFeaturesStatus(FeaturesName.IMG_ORIGINAL_MODEL) ? ImagePixelSize.FACE.value : "")
                .into(commentItemUserFace);

        TextView commentItemUserName = holder.findById(R.id.comment_item_userName);
        commentItemUserName.setText(comment.userInfo.userName);
        if (comment.userInfo.isVip) {
            commentItemUserName.setTextColor(context.getColor(R.color.BiliBili_pink));
        }

        TextView commentItemMessage = holder.findById(R.id.comment_item_message);
        createEmojiAndClickable(comment.content, commentItemMessage);

        holder
                .setText(R.id.comment_item_pubTime, ValueUtils.generateTime(comment.sendTime, "yyyy-MM-dd HH:mm", true))
                .setText(R.id.comment_item_likeTotal, ValueUtils.generateCN(comment.like))
                .setText(R.id.comment_item_replayTotal, ValueUtils.generateCN(comment.rcount))
                .setVisibility(R.id.comment_item_upAction, comment.upLike ? View.VISIBLE : View.GONE);

        LinearLayout commentItemReplayContainer = holder.findById(R.id.comment_item_replay_container);
        if (comment.levelTwoCommentList != null && comment.levelTwoCommentList.size() > 0) {
            commentItemReplayContainer.setVisibility(View.VISIBLE);
            LoadingRecyclerView commentItemReplays = holder.findById(R.id.comment_item_replays);
            TextView commentItemCheckAll = holder.findById(R.id.comment_item_checkAll);

            commentItemReplays.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

            CommentLevelTwoAdapter commentLevelTwoAdapter = new CommentLevelTwoAdapter(comment.levelTwoCommentList, context);
            commentLevelTwoAdapter.setOnCommentListener(new OnCommentListener() {
                @Override
                public void onClick(Comment comment) {
                }

                @Override
                public void navUserFragment(String mid) {
                    if (onCommentListener != null) {
                        onCommentListener.navUserFragment(mid);
                    }
                }
            });
            commentLevelTwoAdapter.setHasStableIds(true);
            commentItemReplays.setRecyclerViewAdapter(commentLevelTwoAdapter);
            commentItemReplays.setRecyclerViewLayoutManager(new LinearLayoutManager(context));

            String replayTotalStr = "共" + comment.rcount + "条回复";
            commentItemCheckAll.setText(replayTotalStr);
            commentItemCheckAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCommentListener != null) {
                        onCommentListener.onClick(comment);
                    }
                }
            });
            commentItemReplays.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
        } else {
            commentItemReplayContainer.setVisibility(View.GONE);
        }
    }

    /**
     * 对评论内容添加Emoji
     *
     * @param content Comment.Content
     */
    private void createEmojiAndClickable(Comment.Content content, TextView commentItemMessage) {
        SpannableString spannableString = new SpannableString(content.message);

        // 添加点击事件
        if (content.contentMembers != null && content.contentMembers.size() > 0) {
            for (Map.Entry<String, String> stringEntry : content.contentMembers.entrySet()) {
                ClickableSpan atBiliUserClickable = new ClickableSpan() {
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

                String atMsg = "@" + stringEntry.getValue();
                int atStartIndex = content.message.indexOf(atMsg);
                spannableString.setSpan(atBiliUserClickable, atStartIndex, (atStartIndex + atMsg.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        // 添加emoji
        if (content.emojiMap != null && content.emojiMap.size() > 0) {
            for (Map.Entry<String, String> entry : content.emojiMap.entrySet()) {
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
                        Matcher matcher = compile.matcher(content.message);

                        while (matcher.find()) {
                            int startIndex = matcher.start();

                            if (startIndex != -1) {
                                imageSpan = new ImageSpan(resource, ImageSpan.ALIGN_BASELINE);
                                spannableString.setSpan(imageSpan, startIndex, (matcher.start() + emojiName.length()), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                            }
                        }

                        commentItemMessage.setText(spannableString);
                        commentItemMessage.setMovementMethod(LinkMovementMethod.getInstance());

                        return false;
                    }
                }).submit();
            }
        } else {
            commentItemMessage.setText(spannableString);
            commentItemMessage.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
