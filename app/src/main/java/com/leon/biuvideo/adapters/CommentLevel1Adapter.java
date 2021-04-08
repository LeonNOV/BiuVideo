package com.leon.biuvideo.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.FeaturesName;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.os.Handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Leon
 * @Time 2021/4/7
 * @Desc 一级评论适配器
 */
public class CommentLevel1Adapter extends BaseAdapter<Comment> {
    public static final int EMOJI_WH = 86;
    private final List<Comment> commentList;

    public CommentLevel1Adapter(List<Comment> beans, Context context) {
        super(beans, context);
        this.commentList = beans;
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
        if (comment.userInfo.isVIP) {
            commentItemUserName.setTextColor(context.getColor(R.color.BiliBili_pink));
        }

        TextView commentItemMessage = holder.findById(R.id.comment_item_message);
        if (comment.content.emojiMap != null && comment.content.emojiMap.size() > 0) {
            createEmoji(comment.content, commentItemMessage);
        } else {
            commentItemMessage.setText(comment.content.message);
        }

        holder
                .setText(R.id.comment_item_pubTime, ValueUtils.generateTime(comment.sendTime, "yyyy-MM-dd HH:mm", true))
                .setText(R.id.comment_item_likeTotal, ValueUtils.generateCN(comment.like))
                .setText(R.id.comment_item_replayTotal, ValueUtils.generateCN(comment.rcount))
                .setVisibility(R.id.comment_item_upAction, comment.upLike ? View.VISIBLE : View.GONE);

        LinearLayout commentItemReplayContainer = holder.findById(R.id.comment_item_replay_container);
        if (comment.subCommentList != null && comment.subCommentList.size() > 0) {
            commentItemReplayContainer.setVisibility(View.VISIBLE);
            LoadingRecyclerView commentItemReplays = holder.findById(R.id.comment_item_replays);
            TextView commentItemCheckAll = holder.findById(R.id.comment_item_checkAll);

            commentItemReplays.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

            CommentLevel2Adapter commentLevel2Adapter = new CommentLevel2Adapter(comment.subCommentList, context);
            commentLevel2Adapter.setHasStableIds(true);
            commentItemReplays.setRecyclerViewAdapter(commentLevel2Adapter);
            commentItemReplays.setRecyclerViewLayoutManager(new LinearLayoutManager(context));

            String replayTotalStr = "共" + comment.rcount + "条回复";
            commentItemCheckAll.setText(replayTotalStr);
            commentItemCheckAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "查看所有回复：" + comment.rpid, Toast.LENGTH_SHORT).show();
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
    private void createEmoji(Comment.Content content, TextView commentItemMessage) {
        SpannableString spannableString = new SpannableString(content.message);
        for (Map.Entry<String, String> entry : content.emojiMap.entrySet()) {
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
                    Matcher matcher = compile.matcher(content.message);

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

                    commentItemMessage.setText(spannableString);

                    return false;
                }
            }).submit();
        }
    }
}
