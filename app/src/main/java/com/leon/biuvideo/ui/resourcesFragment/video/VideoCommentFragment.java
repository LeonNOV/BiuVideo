package com.leon.biuvideo.ui.resourcesFragment.video;

import android.os.Message;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.commentAdapters.CommentLevelOneAdapter;
import com.leon.biuvideo.beans.mediaBeans.Comment;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.CommentParser;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/6
 * @Desc 视频评论页面
 */
public class VideoCommentFragment extends BaseLazySupportFragment {
    private SmartRefreshRecyclerView<Comment> videoCommentCommentData;

    private final String avid;
    private CommentParser commentParser;
    private CommentLevelOneAdapter commentLevelOneAdapter;

    private OnCommentListener onCommentListener;

    public VideoCommentFragment(String avid) {
        this.avid = avid;
    }

    public void setOnCommentListener(OnCommentListener onCommentListener) {
        this.onCommentListener = onCommentListener;
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

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<Comment> commentList = (List<Comment>) msg.obj;

                switch (msg.what) {
                    case 0:
                        if (commentList != null && commentList.size() > 0) {
                            commentLevelOneAdapter = new CommentLevelOneAdapter(commentList, context);
                            commentLevelOneAdapter.setOnCommentListener(onCommentListener);
                            commentLevelOneAdapter.setHasStableIds(true);
                            videoCommentCommentData.setRecyclerViewAdapter(commentLevelOneAdapter);
                            videoCommentCommentData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                        } else {
                            videoCommentCommentData.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                            videoCommentCommentData.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        }

                        videoCommentCommentData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
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
