package com.leon.biuvideo.ui.discovery;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.otherAdapters.SearchHistoryAdapter;
import com.leon.biuvideo.greendao.dao.DaoBaseUtils;
import com.leon.biuvideo.greendao.dao.SearchHistory;
import com.leon.biuvideo.greendao.dao.SearchHistoryDao;
import com.leon.biuvideo.greendao.daoutils.SearchHistoryUtils;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.LoadingRecyclerView;
import com.leon.biuvideo.ui.views.SimpleTopBar;
import com.leon.biuvideo.utils.InternetUtils;

import java.util.Comparator;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 搜索页面
 */
public class SearchFragment extends BaseSupportFragment implements View.OnClickListener {
    private EditText searchFragmentEditTextKeyword;
    private LoadingRecyclerView searchFragmentHistoryList;
    private Context context;
    private SearchResultFragment searchResultFragment;
    private DaoBaseUtils<SearchHistory> searchHistoryDaoUtils;
    private SearchHistoryAdapter searchHistoryAdapter;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getContext();
    }

    @Override
    protected int setLayout() {
        return R.layout.search_fragment;
    }

    @Override
    protected void initView() {
        ((SimpleTopBar) findView(R.id.search_fragment_topBar)).setBackListener(() -> {
            hideSoftInput();
            backPressed();
        });

        searchFragmentEditTextKeyword = findView(R.id.search_fragment_editText_keyword);
        searchFragmentEditTextKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (InternetUtils.checkNetwork(context)) {
                    //按下软键盘的搜索按钮会触发该方法
                    if (i == EditorInfo.IME_ACTION_SEARCH) {
                        //获取输入内容
                        String value = searchFragmentEditTextKeyword.getText().toString();

                        if (!"".equals(value)) {
                            // 生成唯一key
                            long hashCode = value.hashCode();

                            List<SearchHistory> searchHistories = searchHistoryDaoUtils.queryByQueryBuilder(SearchHistoryDao.Properties.HashCode.eq(hashCode));

                            for (SearchHistory searchHistory : searchHistories) {
                                if (searchHistory.getHashCode() == hashCode) {
                                    searchHistoryDaoUtils.delete(searchHistory);
                                    break;
                                }
                            }

                            // 存放当前的关键词
                            searchHistoryDaoUtils.insert(new SearchHistory(null, hashCode, value));

                            // 将之前的搜索结果界面弹出
                            popTo(SearchResultFragment.class, false);

                            // 保证不会产生其他同类型对象
                            searchResultFragment = new SearchResultFragment(value);

                            // 启动目标Fragment并弹出自身
                            startWithPop(searchResultFragment);

                            hideSoftInput();
                        }
                    }
                } else {
                    Toast.makeText(context, getString(R.string.networkWarn), Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
        showSoftInput(searchFragmentEditTextKeyword);

        ImageView searchFragmentImageViewClearKeyword = findView(R.id.search_fragment_imageView_clearKeyword);
        searchFragmentImageViewClearKeyword.setOnClickListener(this);

        TextView searchFragmentTextViewClearHistory = findView(R.id.search_fragment_textView_clearHistory);
        searchFragmentTextViewClearHistory.setOnClickListener(this);

        searchFragmentHistoryList = findView(R.id.search_fragment_historyList);
        searchFragmentHistoryList.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        SearchHistoryUtils searchHistoryUtils = new SearchHistoryUtils(context);
        searchHistoryDaoUtils = searchHistoryUtils.getSearchHistoryDaoUtils();

        List<SearchHistory> searchHistoryList = searchHistoryDaoUtils.queryAll();

        if (searchHistoryList.size() == 0) {
            searchFragmentHistoryList.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
        } else {
            searchHistoryList.sort(new Comparator<SearchHistory>() {
                @Override
                public int compare(SearchHistory o1, SearchHistory o2) {
                    return (int) (o2.getId() - o1.getId());
                }
            });

            searchHistoryAdapter = new SearchHistoryAdapter(searchHistoryList, context);
            searchHistoryAdapter.setOnSearchHistoryListener(new SearchHistoryAdapter.OnSearchHistoryListener() {
                @Override
                public void onDelete(SearchHistory searchHistory) {
                    searchHistoryDaoUtils.delete(searchHistory);
                    searchHistoryList.remove(searchHistory);
                    searchHistoryAdapter.notifyDataSetChanged();
                    if (searchHistoryAdapter.getItemCount() == 0) {
                        searchFragmentHistoryList.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                    }
                }

                @Override
                public void onClick(String keyword) {
                    searchFragmentEditTextKeyword.getText().clear();
                    searchFragmentEditTextKeyword.setText(keyword);
                    searchFragmentEditTextKeyword.setSelection(keyword.length());
                }
            });
            searchHistoryAdapter.setHasStableIds(true);
            searchFragmentHistoryList.setRecyclerViewAdapter(searchHistoryAdapter);
            searchFragmentHistoryList.setRecyclerViewLayoutManager(new LinearLayoutManager(context));

            searchFragmentHistoryList.setLoadingRecyclerViewStatus(LoadingRecyclerView.LOADING_FINISH);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_fragment_textView_clearHistory:
                searchHistoryDaoUtils.deleteAll();
                searchHistoryAdapter.removeAll();
                searchFragmentHistoryList.setLoadingRecyclerViewStatus(LoadingRecyclerView.NO_DATA);
                break;
            case R.id.search_fragment_imageView_clearKeyword:
                searchFragmentEditTextKeyword.getText().clear();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
