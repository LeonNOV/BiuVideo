package com.leon.biuvideo.ui.otherFragments.biliUserFragments;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.biliUserResourcesAdapters.BiliUserVideosAdapter;
import com.leon.biuvideo.beans.biliUserResourcesBeans.BiliUserVideo;
import com.leon.biuvideo.ui.baseSupportFragment.BaseLazySupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SmartRefreshRecyclerView;
import com.leon.biuvideo.utils.parseDataUtils.DataLoader;
import com.leon.biuvideo.utils.parseDataUtils.biliUserResourcesParseUtils.BiliUserVideoParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Leon
 * @Time 2021/4/9
 * @Desc B站用户视频数据
 */
public class BiliUserVideosFragment extends BaseLazySupportFragment implements View.OnClickListener {
    private final String mid;
    private final List<BiliUserVideo> biliUserVideoList = new ArrayList<>();
    private final Map<Integer, TextView> textViewMap = new HashMap<>(3);

    private SmartRefreshRecyclerView<BiliUserVideo> biliUserVideosData;

    private String order = BiliUserVideoParser.ORDER_DEFAULT;
    private BiliUserVideosAdapter biliUserVideosAdapter;
    private DataLoader<BiliUserVideo> biliUserVideoDataLoader;

    public BiliUserVideosFragment(String mid) {
        this.mid = mid;
    }

    @Override
    protected int setLayout() {
        return R.layout.bili_user_videos_fragment;
    }

    @Override
    protected void initView() {
        TextView biliUserVideosOrderDefault = findView(R.id.bili_user_videos_order_default);
        biliUserVideosOrderDefault.setOnClickListener(this);
        textViewMap.put(0, biliUserVideosOrderDefault);

        TextView biliUserVideosOrderClick = findView(R.id.bili_user_videos_order_click);
        biliUserVideosOrderClick.setOnClickListener(this);
        textViewMap.put(1, biliUserVideosOrderClick);

        TextView biliUserVideosOrderStow = findView(R.id.bili_user_videos_order_stow);
        biliUserVideosOrderStow.setOnClickListener(this);
        textViewMap.put(2, biliUserVideosOrderStow);

        biliUserVideosAdapter = new BiliUserVideosAdapter(biliUserVideoList, context);
        biliUserVideosAdapter.setHasStableIds(true);
        biliUserVideosData = findView(R.id.bili_user_videos_data);
        biliUserVideosData.setRecyclerViewAdapter(biliUserVideosAdapter);
        biliUserVideosData.setRecyclerViewLayoutManager(new LinearLayoutManager(context));
        biliUserVideosData.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                biliUserVideoDataLoader.insertData(false);
            }
        });

        biliUserVideoDataLoader = new DataLoader<>(new BiliUserVideoParser(mid, order), biliUserVideosData, biliUserVideosAdapter, this);
    }

    @Override
    protected void onLazyLoad() {
        biliUserVideosData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
        biliUserVideoDataLoader.insertData(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bili_user_videos_order_default:
                if (order.equals(BiliUserVideoParser.ORDER_DEFAULT)) {
                    return;
                }

                order = null;
                BiliUserFragment.changeText(0, textViewMap, context);
                break;
            case R.id.bili_user_videos_order_click:
                if (order.equals(BiliUserVideoParser.ORDER_CLICK)) {
                    return;
                }

                order = BiliUserVideoParser.ORDER_CLICK;
                BiliUserFragment.changeText(1, textViewMap, context);
                break;
            case R.id.bili_user_videos_order_stow:
                if (order.equals(BiliUserVideoParser.ORDER_STOW)) {
                    return;
                }

                order = BiliUserVideoParser.ORDER_STOW;
                BiliUserFragment.changeText(2, textViewMap, context);
                break;
            default:
                break;
        }

        reset();
    }

    private void reset () {
        biliUserVideosData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        biliUserVideosAdapter.removeAll();

        biliUserVideoDataLoader.setParserInterface(new BiliUserVideoParser(mid, order));
        biliUserVideoDataLoader.insertData(true);
    }
}
