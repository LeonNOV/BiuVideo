package com.leon.biuvideo.ui.resourcesFragment.video;

import android.os.Message;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.commentAdapters.CommentLevelOneAdapter;
import com.leon.biuvideo.beans.resourcesBeans.Comment;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
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
    private CommentParser commentParser;
    private CommentLevelOneAdapter commentLevelOneAdapter;

    private VideoInfoAndCommentsFragment.ToCommentDetailFragment toCommentDetailFragment;

    public VideoCommentFragment(String avid) {
        this.avid = avid;
    }

    public void setToCommentDetailFragment(VideoInfoAndCommentsFragment.ToCommentDetailFragment toCommentDetailFragment) {
        this.toCommentDetailFragment = toCommentDetailFragment;
    }

    @Override
    protected void onLazyLoad() {
        videoCommentCommentData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        getComments(0);
    }

    @Override
    protected int setLayout() {
        return R.layout.video_comment_fragment;
    }

    @Override
    protected void initView() {
        videoCommentCommentData = findView(R.id.video_comment_commentData);
        videoCommentCommentData.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                getComments(1);
            }
        });
        commentLevelOneAdapter = new CommentLevelOneAdapter(commentList, context);
        commentLevelOneAdapter.setToCommentDetailFragment(toCommentDetailFragment);
        commentLevelOneAdapter.setHasStableIds(true);
        videoCommentCommentData.setRecyclerViewAdapter(commentLevelOneAdapter);
        videoCommentCommentData.setRecyclerViewLayoutManager(new LinearLayoutManager(context));

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<Comment> commentList = (List<Comment>) msg.obj;

                switch (msg.what) {
                    case 0:
                        if (commentList != null && commentList.size() > 0) {
                            videoCommentCommentData.append(commentList);
                            videoCommentCommentData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                            if (!commentParser.dataStatus) {
                                videoCommentCommentData.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                            }
                        } else {
                            videoCommentCommentData.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                            videoCommentCommentData.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        }

                        videoCommentCommentData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                        break;
                    case 1:
                        if (commentList != null && commentList.size() > 0) {
                            commentLevelOneAdapter.append(commentList);
                            videoCommentCommentData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                            if (!commentParser.dataStatus) {
                                videoCommentCommentData.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                            }
                        } else {
                            videoCommentCommentData.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 获取评论数据
     *
     * @param what  what
     */
    private void getComments (int what) {
        if (commentParser == null) {
            commentParser = new CommentParser(avid, CommentParser.TYPE_VIDEO, CommentParser.SORT_REPLAY);
        }

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                List<Comment> commentList = commentParser.parseData();

                Message message = receiveDataHandler.obtainMessage(what);
                message.obj = commentList;
                receiveDataHandler.sendMessage(message);
            }
        });
    }
}
