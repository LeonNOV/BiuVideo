package com.leon.biuvideo.ui.resourcesFragment.video;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.commentAdapters.CommentLevelOneAdapter;
import com.leon.biuvideo.beans.resourcesBeans.Comment;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.CommentParser;

/**
 * @Author Leon
 * @Time 2021/4/6
 * @Desc 视频评论页面
 */
public class VideoCommentFragment extends BaseLazySupportFragment {
    private final String avid;

    private ToCommentDetailFragment toCommentDetailFragment;
    private DataLoader<Comment> commentDataLoader;

    public VideoCommentFragment(String avid) {
        this.avid = avid;
    }

    public void setToCommentDetailFragment(ToCommentDetailFragment toCommentDetailFragment) {
        this.toCommentDetailFragment = toCommentDetailFragment;
    }

    public interface ToCommentDetailFragment {
        void toCommentDetail(Comment comment);
    }

    @Override
    protected void onLazyLoad() {
        commentDataLoader.insertData(true);
    }

    @Override
    protected int setLayout() {
        return R.layout.video_comment_fragment;
    }

    @Override
    protected void initView() {
        findView(R.id.video_comment_container).setBackgroundResource(R.color.white);

        CommentLevelOneAdapter commentLevelOneAdapter = new CommentLevelOneAdapter(getMainActivity(), context);
        commentLevelOneAdapter.setToCommentDetailFragment(toCommentDetailFragment);
        commentDataLoader = new DataLoader<>(context, new CommentParser(avid, CommentParser.TYPE_VIDEO, CommentParser.SORT_REPLAY),
                R.id.video_comment_commentData,
                commentLevelOneAdapter,
                this);
    }
}
