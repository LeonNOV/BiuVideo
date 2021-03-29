package com.leon.biuvideo.ui.discovery.searchResultFragments;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

/**
 * @Author Leon
 * @Time 2021/3/29
 * @Desc 视频搜索结果
 */
public class SearchResultVideoFragment extends BaseLazySupportFragment implements View.OnClickListener {
    private TextView searchResultVideoMenuOrderText;
    private TextView searchResultVideoMenuLengthText;
    private TextView searchResultVideoMenuPartitionText;

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
        ImageView searchResultVideoMenuLengthImg = findView(R.id.search_result_video_menu_length_img);
        ImageView searchResultVideoMenuPartitionImg = findView(R.id.search_result_video_menu_partition_img);

        SmartRefreshRecyclerView<Object> searchResultVideoData = findView(R.id.search_result_video_data);
        searchResultVideoData.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {

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

                break;
            case R.id.search_result_video_menu_length:
                break;
            case R.id.search_result_video_menu_partition:
                break;
            default:
                break;
        }
    }
}
