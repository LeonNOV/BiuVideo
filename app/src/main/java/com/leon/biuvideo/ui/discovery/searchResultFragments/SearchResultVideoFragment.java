package com.leon.biuvideo.ui.discovery.searchResultFragments;

import android.animation.ObjectAnimator;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.discoverAdapters.searchResultAdapters.SearchResultVideoAdapter;
import com.leon.biuvideo.beans.searchResultBeans.SearchResultVideo;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.ui.views.searchResultViews.SearchResultMenuAdapter;
import com.leon.biuvideo.ui.views.searchResultViews.SearchResultMenuPopupWindow;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.searchParsers.SearchResultVideoParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/29
 * @Desc 视频搜索结果
 */
public class SearchResultVideoFragment extends BaseLazySupportFragment implements View.OnClickListener {
    private final List<SearchResultVideo> searchResultVideoList = new ArrayList<>();

    private final String keyword;
    private String order;
    private String length;
    private String partition;

    private int orderSelectedPosition = 0;
    private int lengthSelectedPosition = 0;
    private int partitionSelectedPosition = 0;

    private TextView searchResultVideoMenuOrderText;
    private TextView searchResultVideoMenuLengthText;
    private TextView searchResultVideoMenuPartitionText;

    private ObjectAnimator orderImgWhirl;
    private ObjectAnimator lengthImgWhirl;
    private ObjectAnimator partitionImgWhirl;
    private SearchResultVideoParser searchResultVideoParser;
    private SmartRefreshRecyclerView<SearchResultVideo> searchResultVideoData;
    private SearchResultVideoAdapter searchResultVideoAdapter;

    public SearchResultVideoFragment (String keyword) {
        this.keyword = keyword;
        this.order = "totalrank";
        this.length = "0";
        this.partition = "0";
    }

    @Override
    protected int setLayout() {
        return R.layout.search_ressult_video_fragment;
    }

    @Override
    protected void initView() {
        findView(R.id.search_result_video_menu_order).setOnClickListener(this);
        findView(R.id.search_result_video_menu_length).setOnClickListener(this);
        findView(R.id.search_result_video_menu_partition).setOnClickListener(this);

        searchResultVideoMenuOrderText = findView(R.id.search_result_video_menu_order_text);
        searchResultVideoMenuLengthText = findView(R.id.search_result_video_menu_length_text);
        searchResultVideoMenuPartitionText = findView(R.id.search_result_video_menu_partition_text);

        ImageView searchResultVideoMenuOrderImg = findView(R.id.search_result_video_menu_order_img);
        orderImgWhirl = ObjectAnimator.ofFloat(searchResultVideoMenuOrderImg, "rotation", 0.0f, 180.0f);
        orderImgWhirl.setDuration(400);

        ImageView searchResultVideoMenuLengthImg = findView(R.id.search_result_video_menu_length_img);
        lengthImgWhirl = ObjectAnimator.ofFloat(searchResultVideoMenuLengthImg, "rotation", 0.0f, 180.0f);
        lengthImgWhirl.setDuration(400);

        ImageView searchResultVideoMenuPartitionImg = findView(R.id.search_result_video_menu_partition_img);
        partitionImgWhirl = ObjectAnimator.ofFloat(searchResultVideoMenuPartitionImg, "rotation", 0.0f, 180.0f);
        partitionImgWhirl.setDuration(400);

        searchResultVideoData = findView(R.id.search_result_video_data);
        searchResultVideoData.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (!searchResultVideoParser.dataStatus) {
                    searchResultVideoData.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                } else {
                    getVideo(1);
                }
            }
        });
        searchResultVideoAdapter = new SearchResultVideoAdapter(searchResultVideoList, context);
        searchResultVideoAdapter.setHasStableIds(true);
        searchResultVideoData.setRecyclerViewAdapter(searchResultVideoAdapter);
        searchResultVideoData.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<SearchResultVideo> searchResultVideos = (List<SearchResultVideo>) msg.obj;

                switch (msg.what) {
                    case 0:
                        if (searchResultVideos == null || searchResultVideos.size() == 0) {
                            searchResultVideoData.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                            searchResultVideoData.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        } else {
                            searchResultVideoAdapter.append(searchResultVideos);
                            searchResultVideoData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                        }
                        break;
                    case 1:
                        if (searchResultVideos != null && searchResultVideos.size() > 0) {
                            searchResultVideoAdapter.append(searchResultVideos);
                            searchResultVideoData.setSmartRefreshStatus(SmartRefreshRecyclerView.LOADING_FINISHING);
                        } else {
                            searchResultVideoData.setSmartRefreshStatus(SmartRefreshRecyclerView.NO_DATA);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onLazyLoad() {
        searchResultVideoData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        getVideo(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_result_video_menu_order:
                orderImgWhirl.start();
                SearchResultMenuPopupWindow searchResultOrderMenuPopupWindow = new SearchResultMenuPopupWindow(v, context, SearchResultMenuPopupWindow.SearchResultVideoMenuItem.VIDEO_ORDER_LIST, orderSelectedPosition, 4);
                searchResultOrderMenuPopupWindow.setOnSearchResultMenuItemListener(new SearchResultMenuAdapter.OnSearchResultMenuItemListener() {
                    @Override
                    public void onClickMenuItem(String[] values, int position) {
                        searchResultVideoMenuOrderText.setText(values[0]);
                        orderSelectedPosition = position;
                        searchResultOrderMenuPopupWindow.dismiss();
                        order = values[1];
                        reset();
                    }
                });
                searchResultOrderMenuPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        orderImgWhirl.reverse();
                    }
                });

                break;
            case R.id.search_result_video_menu_length:
                lengthImgWhirl.start();
                SearchResultMenuPopupWindow searchResultLengthMenuPopupWindow = new SearchResultMenuPopupWindow(v, context, SearchResultMenuPopupWindow.SearchResultVideoMenuItem.VIDEO_LENGTH_LIST, lengthSelectedPosition, 4);
                searchResultLengthMenuPopupWindow.setOnSearchResultMenuItemListener(new SearchResultMenuAdapter.OnSearchResultMenuItemListener() {
                    @Override
                    public void onClickMenuItem(String[] values, int position) {
                        searchResultVideoMenuLengthText.setText(values[0]);
                        lengthSelectedPosition = position;
                        searchResultLengthMenuPopupWindow.dismiss();
                        length = values[1];
                        reset();
                    }
                });
                searchResultLengthMenuPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        lengthImgWhirl.reverse();
                    }
                });

                break;
            case R.id.search_result_video_menu_partition:
                partitionImgWhirl.start();
                SearchResultMenuPopupWindow searchResultPartitionMenuPopupWindow = new SearchResultMenuPopupWindow(v, context, SearchResultMenuPopupWindow.SearchResultVideoMenuItem.VIDEO_PARTITIONS_LIST, partitionSelectedPosition, 4);
                searchResultPartitionMenuPopupWindow.setOnSearchResultMenuItemListener(new SearchResultMenuAdapter.OnSearchResultMenuItemListener() {
                    @Override
                    public void onClickMenuItem(String[] values, int position) {
                        searchResultVideoMenuPartitionText.setText(values[0]);
                        partitionSelectedPosition = position;
                        searchResultPartitionMenuPopupWindow.dismiss();
                        partition = values[1];
                        reset();
                    }
                });
                searchResultPartitionMenuPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        partitionImgWhirl.reverse();
                    }
                });

                break;
            default:
                break;
        }
    }

    /**
     * 获取video数据
     *
     * @param what  what
     */
    private void getVideo(int what) {
        if (searchResultVideoParser == null) {
            searchResultVideoParser = new SearchResultVideoParser(keyword, order, length, partition);
        }
        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                List<SearchResultVideo> searchResultVideoList = searchResultVideoParser.parseData();

                Message message;
                if (what == -1) {
                    message = receiveDataHandler.obtainMessage(0);
                } else {
                    message = receiveDataHandler.obtainMessage(what);
                }
                message.obj = searchResultVideoList;
                receiveDataHandler.sendMessage(message);
            }
        });
    }

    /**
     *  重置当前所有的数据
     */
    private void reset() {
        searchResultVideoData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

        // 清空列表中的数据
        searchResultVideoAdapter.removeAll();

        searchResultVideoParser = new SearchResultVideoParser(keyword, order, length, partition);
        getVideo(-1);
    }
}
