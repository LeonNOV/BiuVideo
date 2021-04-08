package com.leon.biuvideo.ui.resourcesFragment;

import android.graphics.drawable.Drawable;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
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
import com.leon.biuvideo.adapters.CommentDetailAdapter;
import com.leon.biuvideo.adapters.CommentLevelOneAdapter;
import com.leon.biuvideo.beans.mediaBeans.Comment;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.ui.views.CardTitle;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.PreferenceUtils;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils.CommentDetailParser;
import com.leon.biuvideo.utils.parseDataUtils.mediaParseUtils.CommentParser;
import com.leon.biuvideo.values.FeaturesName;
import com.leon.biuvideo.values.ImagePixelSize;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @Author Leon
 * @Time 2021/4/8
 * @Desc 查看评论详细页面
 */
public class VideoCommentDetailFragment extends BaseSupportFragment {
    private final Comment comment;
    private CommentDetailParser commentDetailParser;
    private CommentDetailAdapter commentDetailAdapter;

    private ImageSpan imageSpan;

    public VideoCommentDetailFragment(Comment comment) {
        this.comment = comment;
    }

    @Override
    protected int setLayout() {
        return R.layout.video_comment_detail_fragment;
    }

    @Override
    protected void initView() {
        findView(R.id.video_comment_detail_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((VideoFragment) getParentFragment()).onBackPressedSupport();
            }
        });

        ((CardTitle) findView(R.id.video_comment_detail_cardTitle)).setTitle("共" + comment.rcount + "条回复");

        CircleImageView videoCommentDetailItemUserFace = findView(R.id.video_comment_detail_item_userFace);
        Glide
                .with(context)
                .load(comment.userInfo.userFace +=
                        PreferenceUtils.getFeaturesStatus(FeaturesName.IMG_ORIGINAL_MODEL) ? ImagePixelSize.FACE.value : "")
                .into(videoCommentDetailItemUserFace);

        ImageView videoCommentDetailItemVerifyMark = findView(R.id.video_comment_detail_item_verifyMark);
        switch (comment.userInfo.role) {
            case PERSON:
                videoCommentDetailItemVerifyMark.setImageResource(R.drawable.ic_person_verify);
                break;
            case OFFICIAL:
                videoCommentDetailItemVerifyMark.setImageResource(R.drawable.ic_official_verify);
                break;
            default:
                videoCommentDetailItemVerifyMark.setVisibility(View.GONE);
                break;
        }

        createEmojiAndClickable(comment.content, (TextView) findView(R.id.video_comment_detail_item_message));

        new BindingUtils(view, context)
                .setText(R.id.video_comment_detail_item_userName, comment.userInfo.userName)
                .setText(R.id.video_comment_detail_item_pubTime, ValueUtils.generateTime(comment.sendTime, "yyyy-MM-dd HH:mm", true))
                .setText(R.id.video_comment_detail_item_likeTotal, String.valueOf(comment.like))
                .setText(R.id.video_comment_detail_item_replayTotal, String.valueOf(comment.rcount))
                .setVisibility(R.id.video_comment_detail_item_upAction, comment.upLike ? View.VISIBLE : View.GONE);

        SmartRefreshRecyclerView<Comment> videoCommentDetailList = findView(R.id.video_comment_detail_list);

        videoCommentDetailList.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                getCommentDetail(1);
            }
        });
        videoCommentDetailList.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<Comment> commentList = (List<Comment>) msg.obj;

                switch (msg.what) {
                    case 0:
                        if (commentList != null && commentList.size() > 0) {
                            commentDetailAdapter = new CommentDetailAdapter(commentList, context);
                            commentDetailAdapter.setOnCommentListener(new OnCommentListener() {
                                @Override
                                public void onClick(Comment comment) {

                                }

                                @Override
                                public void navUserFragment(String mid) {

                                }
                            });
                            commentDetailAdapter.setHasStableIds(true);
                            videoCommentDetailList.setRecyclerViewAdapter(commentDetailAdapter);
                            videoCommentDetailList.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
                        } else {
                            videoCommentDetailList.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                            videoCommentDetailList.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        }

                        videoCommentDetailList.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                        break;
                    case 1:
                        if (commentList != null && commentList.size() > 0) {
                            commentDetailAdapter.append(commentList);
                        } else {
                            videoCommentDetailList.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        }

                        if (!commentDetailParser.dataStatus) {
                            videoCommentDetailList.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        }

                        videoCommentDetailList.setSmartRefreshStatus(SmartRefreshRecyclerView.LOADING_FINISHING);
                        break;
                    default:
                        break;
                }
            }
        });

        getCommentDetail(0);
    }

    /**
     * 获取评论数据
     *
     * @param what  what
     */
    private void getCommentDetail(int what) {
        if (commentDetailParser == null) {
            commentDetailParser = new CommentDetailParser(CommentParser.TYPE_VIDEO, comment.oid, comment.rpid);
        }

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                List<Comment> commentList = commentDetailParser.parseData();

                Message message = receiveDataHandler.obtainMessage(what);
                message.obj = commentList;
                receiveDataHandler.sendMessage(message);
            }
        });
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
                        Toast.makeText(context, "BiliUserMid：" + stringEntry.getKey(), Toast.LENGTH_SHORT).show();
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
