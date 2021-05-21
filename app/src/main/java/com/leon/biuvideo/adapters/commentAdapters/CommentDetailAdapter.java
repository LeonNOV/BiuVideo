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
import android.widget.ImageView;
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
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.values.FeaturesName;
import com.leon.biuvideo.values.FragmentType;
import com.leon.biuvideo.values.ImagePixelSize;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Leon
 * @Time 2021/4/8
 * @Desc 一级评论下所有评论的适配器
 */
public class CommentDetailAdapter extends BaseAdapter<Comment> {
    private final MainActivity mainActivity;
    private ImageSpan imageSpan;

    public CommentDetailAdapter(MainActivity mainActivity, Context context) {
        super(context);
        this.mainActivity = mainActivity;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.comment_detail_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Comment comment = getAllData().get(position);

        ImageView commentItemUserFace = holder.findById(R.id.comment_detail_item_userFace);
        commentItemUserFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPublicFragment(mainActivity, FragmentType.BILI_USER, comment.biliUserInfo.userMid);
            }
        });

        ImageView commentItemVerifyMark = holder.findById(R.id.comment_detail_item_verifyMark);
        switch (comment.biliUserInfo.role) {
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
                .load(comment.biliUserInfo.userFace +=
                        PreferenceUtils.getFeaturesStatus(FeaturesName.IMG_ORIGINAL_MODEL) ? ImagePixelSize.FACE.value : "")
                .into(commentItemUserFace);

        TextView commentItemUserName = holder.findById(R.id.comment_detail_item_userName);
        commentItemUserName.setText(comment.biliUserInfo.userName);
        if (comment.biliUserInfo.isVip) {
            commentItemUserName.setTextColor(context.getColor(R.color.BiliBili_pink));
        }

        TextView commentItemMessage = holder.findById(R.id.comment_detail_item_message);
        createEmojiAndClickable(comment.content, commentItemMessage);

        holder
                .setText(R.id.comment_detail_item_pubTime, ValueUtils.generateTime(comment.sendTime, "yyyy-MM-dd HH:mm", true))
                .setText(R.id.comment_detail_item_likeTotal, ValueUtils.generateCN(comment.like))
                .setText(R.id.comment_detail_item_replayTotal, ValueUtils.generateCN(comment.rcount))
                .setVisibility(R.id.comment_detail_item_upAction, comment.upLike ? View.VISIBLE : View.GONE);
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
                        startPublicFragment(mainActivity, FragmentType.BILI_USER, stringEntry.getKey());
                    }
                };

                String atMsg = "@" + stringEntry.getValue();
                int atStartIndex = content.message.indexOf(atMsg);
                if (atStartIndex != -1) {
                    spannableString.setSpan(atBiliUserClickable, atStartIndex, (atStartIndex + atMsg.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
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
