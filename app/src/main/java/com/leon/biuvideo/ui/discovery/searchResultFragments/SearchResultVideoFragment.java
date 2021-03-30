package com.leon.biuvideo.ui.discovery.searchResultFragments;

import android.animation.ObjectAnimator;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.ui.views.searchResultViews.SearchResultMenuAdapter;
import com.leon.biuvideo.ui.views.searchResultViews.SearchResultMenuPopupWindow;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

/**
 * @Author Leon
 * @Time 2021/3/29
 * @Desc 视频搜索结果
 */
public class SearchResultVideoFragment extends BaseLazySupportFragment implements View.OnClickListener {
    private int orderSelectedPosition = 0;
    private int lengthSelectedPosition = 0;
    private int partitionSelectedPosition = 0;

    private TextView searchResultVideoMenuOrderText;
    private TextView searchResultVideoMenuLengthText;
    private TextView searchResultVideoMenuPartitionText;

    private ObjectAnimator orderImgWhirl;
    private ObjectAnimator lengthImgWhirl;
    private ObjectAnimator partitionImgWhirl;

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

        SmartRefreshRecyclerView searchResultVideoData = findView(R.id.search_result_video_data);
        searchResultVideoData.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {

            }
        });

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {

            }
        });
    }

    @Override
    protected void onLazyLoad() {

    }

    private void getVideo(int what) {

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
}
