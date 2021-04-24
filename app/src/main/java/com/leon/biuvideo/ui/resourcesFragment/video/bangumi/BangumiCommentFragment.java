package com.leon.biuvideo.ui.resourcesFragment.video.bangumi;

import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.commentAdapters.CommentLevelOneAdapter;
import com.leon.biuvideo.adapters.otherAdapters.ViewPager2Adapter;
import com.leon.biuvideo.beans.resourcesBeans.Comment;
import com.leon.biuvideo.beans.resourcesBeans.videoBeans.VideoWithFlv;
import com.leon.biuvideo.ui.MainActivity;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.VideoCommentFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.VideoInfoAndCommentsFragment;
import com.leon.biuvideo.ui.resourcesFragment.video.VideoStatListener;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.ValueUtils;
import com.leon.biuvideo.utils.ViewUtils;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.CommentParser;
import com.leon.biuvideo.utils.parseDataUtils.resourcesParsers.VideoWithFlvParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/24
 * @Desc
 */
public class BangumiCommentFragment extends BaseSupportFragment  {
    private final List<Comment> commentList = new ArrayList<>();
    private SmartRefreshRecyclerView<Comment> videoCommentCommentData;

    private String avid;
    private CommentParser commentParser;
    private CommentLevelOneAdapter commentLevelOneAdapter;

    private VideoInfoAndCommentsFragment.ToCommentDetailFragment toCommentDetailFragment;

    public void setToCommentDetailFragment(VideoInfoAndCommentsFragment.ToCommentDetailFragment toCommentDetailFragment) {
        this.toCommentDetailFragment = toCommentDetailFragment;
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

    /**
     * 对评论内容进行重置
     *
     * @param avid  视频avid
     */
    public void setCommentDatas(String avid) {
        videoCommentCommentData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        this.avid = avid;

        commentLevelOneAdapter.removeAll();
        this.commentParser = new CommentParser(avid, CommentParser.TYPE_VIDEO, CommentParser.SORT_REPLAY);

        getComments(0);
    }
}
