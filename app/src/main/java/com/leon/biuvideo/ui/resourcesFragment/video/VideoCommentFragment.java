package com.leon.biuvideo.ui.resourcesFragment.video;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.commentAdapters.CommentLevelOneAdapter;
import com.leon.biuvideo.beans.resourcesBeans.Comment;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.contribution.VideoInfoAndCommentsFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.CommentParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/6
 * @Desc 视频评论页面
 */
public class VideoCommentFragment extends BaseLazySupportFragment {
    private final List<Comment> commentList = new ArrayList<>();
    private SmartRefreshRecyclerView<Comment> videoCommentCommentData;

    private final String avid;

    private VideoInfoAndCommentsFragment.ToCommentDetailFragment toCommentDetailFragment;
    private DataLoader<Comment> commentDataLoader;

    public VideoCommentFragment(String avid) {
        this.avid = avid;
    }

    public void setToCommentDetailFragment(VideoInfoAndCommentsFragment.ToCommentDetailFragment toCommentDetailFragment) {
        this.toCommentDetailFragment = toCommentDetailFragment;
    }

    @Override
    protected void onLazyLoad() {
        videoCommentCommentData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        commentDataLoader.insertData(true);
    }

    @Override
    protected int setLayout() {
        return R.layout.video_comment_fragment;
    }

    @Override
    protected void initView() {
        findView(R.id.video_comment_container).setBackgroundResource(R.color.white);

        videoCommentCommentData = findView(R.id.video_comment_commentData);
        videoCommentCommentData.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                commentDataLoader.insertData(false);
            }
        });
        CommentLevelOneAdapter commentLevelOneAdapter = new CommentLevelOneAdapter(commentList, context);
        commentLevelOneAdapter.setToCommentDetailFragment(toCommentDetailFragment);
        commentLevelOneAdapter.setHasStableIds(true);
        videoCommentCommentData.setRecyclerViewAdapter(commentLevelOneAdapter);
        videoCommentCommentData.setRecyclerViewLayoutManager(new LinearLayoutManager(context));

        commentDataLoader = new DataLoader<>(new CommentParser(avid, CommentParser.TYPE_VIDEO, CommentParser.SORT_REPLAY), videoCommentCommentData, commentLevelOneAdapter, this
        );
    }
}
