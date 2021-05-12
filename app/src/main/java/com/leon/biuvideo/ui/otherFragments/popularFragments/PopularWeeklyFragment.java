package com.leon.biuvideo.ui.otherFragments.popularFragments;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.homeAdapters.popularAdapters.PopularAdapter;
import com.leon.biuvideo.adapters.otherAdapters.PopularWeeklySeriesAdapter;
import com.leon.biuvideo.beans.homeBeans.popularBeans.PopularVideo;
import com.leon.biuvideo.beans.homeBeans.popularBeans.PopularWeeklySeries;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SimpleBottomSheet;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.utils.SimpleSingleThreadPool;
import com.leon.biuvideo.utils.parseDataUtils.homeParseUtils.popularParsers.PopularWeeklyDataParser;
import com.leon.biuvideo.utils.parseDataUtils.homeParseUtils.popularParsers.PopularWeeklySeriesParser;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 热门排行榜页面-每周必看
 */
public class PopularWeeklyFragment extends BaseSupportFragment {
    private List<PopularWeeklySeries> popularWeeklySeriesList;
    private PopularWeeklySeriesParser popularWeeklySeriesParser;
    private LoadingRecyclerView popularWeeklyData;
    private PopularWeeklyDataParser popularWeeklyDataParser;
    private TextView popularWeeklySelectedSubject;
    private TextView popularWeeklySelectedName;

    private int selectedSeriesPosition = 0;

    @Override
    protected int setLayout() {
        return R.layout.popular_weekly;
    }

    @Override
    protected void initView() {
        SimpleTopBar popularWeeklyTopBar = view.findViewById(R.id.popular_weekly_topBar);
        popularWeeklyTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                backPressed();
            }

            @Override
            public void onRight() {

            }
        });

        popularWeeklySelectedName = findView(R.id.popular_weekly_selected_name);
        popularWeeklySelectedSubject = findView(R.id.popular_weekly_selected_subject);

        LinearLayout popularWeeklySelectSeries = findView(R.id.popular_weekly_selectSeries);
        popularWeeklySelectSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(popularWeeklySeriesList, selectedSeriesPosition);
            }
        });

        // 载数据未加载完成前不能操作discoveryPopularWeeklySelectSeries
        popularWeeklySelectSeries.setClickable(false);

        popularWeeklyData = findView(R.id.popular_weekly_data);
        PopularAdapter popularAdapter = new PopularAdapter(context, PopularAdapter.WEEKLY, false);
        popularAdapter.setHasStableIds(true);
        popularWeeklyData.setRecyclerViewAdapter(popularAdapter);
        popularWeeklyData.setRecyclerViewLayoutManager(new LinearLayoutManager(context));

        setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad(Message msg) {
                List<PopularVideo> popularVideos = (List<PopularVideo>) msg.obj;

                switch (msg.what) {
                    case 0:
                        PopularWeeklySeries popularWeeklySeries = popularWeeklySeriesList.get(0);
                        popularWeeklySelectedName.setText(popularWeeklySeries.name);
                        popularWeeklySelectedSubject.setText(popularWeeklySeries.subject);

                        popularAdapter.append(popularVideos);
                        popularWeeklyData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);

                        popularWeeklySelectSeries.setClickable(true);
                        break;
                    case 1:
                        popularWeeklyData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);

                        popularAdapter.removeAll();
                        popularAdapter.append(popularVideos);

                        popularWeeklyData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        popularWeeklyData.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        getData(0, -1);
    }

    private void showBottomSheet(List<PopularWeeklySeries> popularWeeklySeriesList, int selectedPosition) {
        SimpleBottomSheet simpleBottomSheet = new SimpleBottomSheet(context, R.layout.popular_weekly_before_series_bottom_sheet);
        View view = simpleBottomSheet.initView();
        BottomSheetDialog bottomSheetDialog = simpleBottomSheet.bottomSheetDialog;

        LoadingRecyclerView popularWeeklyBeforeSeriesBottomSheetLoadingRecyclerView = view.findViewById(R.id.popular_weekly_before_series_bottom_sheet_loadingRecyclerView);
        popularWeeklyBeforeSeriesBottomSheetLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
        PopularWeeklySeriesAdapter popularWeeklySeriesAdapter = new PopularWeeklySeriesAdapter(popularWeeklySeriesList, selectedPosition, context);
        popularWeeklySeriesAdapter.setOnSeriesChangedListener(new PopularWeeklySeriesAdapter.OnSeriesChangedListener() {
            @Override
            public void onChanged(PopularWeeklySeries popularWeeklySeries, int position) {
                selectedSeriesPosition = position;

                getData(1, popularWeeklySeries.number);
                popularWeeklySelectedName.setText(popularWeeklySeries.name);
                popularWeeklySelectedSubject.setText(popularWeeklySeries.subject);

                bottomSheetDialog.dismiss();
            }
        });
        popularWeeklySeriesAdapter.setHasStableIds(true);
        popularWeeklyBeforeSeriesBottomSheetLoadingRecyclerView.setRecyclerViewAdapter(popularWeeklySeriesAdapter);
        popularWeeklyBeforeSeriesBottomSheetLoadingRecyclerView.setRecyclerViewLayoutManager(new LinearLayoutManager(context));

        popularWeeklyBeforeSeriesBottomSheetLoadingRecyclerView.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
        bottomSheetDialog.show();
    }

    private void getData(int what, int number) {
        if (popularWeeklySeriesParser == null) {
            popularWeeklyDataParser = new PopularWeeklyDataParser();
        }

        SimpleSingleThreadPool.executor(new Runnable() {
            @Override
            public void run() {
                if (what == 0) {
                    // 保证只获取一次每周必看series数据
                    if (popularWeeklySeriesParser == null) {
                        popularWeeklySeriesParser = new PopularWeeklySeriesParser();
                        popularWeeklySeriesList = popularWeeklySeriesParser.parseData();
                    }
                }

                List<PopularVideo> popularVideos;

                // 如果number等于-1，则获取最新一期的数据
                if (number == -1 && popularWeeklySeriesList != null) {
                    popularVideos = popularWeeklyDataParser.parseData(popularWeeklySeriesList.get(0).number);
                } else {
                    popularVideos = popularWeeklyDataParser.parseData(number);
                }

                Message message = receiveDataHandler.obtainMessage(what);
                message.obj = popularVideos;
                receiveDataHandler.sendMessage(message);
            }
        });
    }
}
