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
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.SearchHistoryAdapter;
import com.leon.biuvideo.ui.baseSupportFragment.BaseSupportFragment;
import com.leon.biuvideo.ui.views.SimpleTopBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Leon
 * @Time 2021/3/1
 * @Desc 搜索页面
 */
public class SearchFragment extends BaseSupportFragment implements View.OnClickListener {
    private EditText searchFragmentEditTextKeyword;
    private RecyclerView searchFragmentRecyclerViewHistoryList;
    private Context context;

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
        SimpleTopBar searchFragmentTopBar = view.findViewById(R.id.search_fragment_topBar);
        searchFragmentTopBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                hideSoftInput();
                backPressed();
            }

            @Override
            public void onRight() {
                Toast.makeText(context, "wasd", Toast.LENGTH_SHORT).show();
            }
        });

        searchFragmentEditTextKeyword = view.findViewById(R.id.search_fragment_editText_keyword);
        searchFragmentEditTextKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //按下软键盘的搜索按钮会触发该方法
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    //获取输入内容
                    String value = searchFragmentEditTextKeyword.getText().toString();

                    if (!"".equals(value)) {
                        start(new SearchResultFragment(value));
                        hideSoftInput();
                    }
                }

                return false;
            }
        });
        showSoftInput(searchFragmentEditTextKeyword);

        ImageView searchFragmentImageViewClearKeyword = view.findViewById(R.id.search_fragment_imageView_clearKeyword);
        searchFragmentImageViewClearKeyword.setOnClickListener(this);

        TextView searchFragmentTextViewClearHistory = view.findViewById(R.id.search_fragment_textView_clearHistory);
        searchFragmentTextViewClearHistory.setOnClickListener(this);

        searchFragmentRecyclerViewHistoryList = view.findViewById(R.id.search_fragment_recyclerView_historyList);
    }

    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);

        // 入场动画结束后执行
        List<String> historys = new ArrayList<>();
        historys.add("阿斯顿撒");
        historys.add("wasd2");
        historys.add("阿斯顿撒");
        historys.add("啊实打实大苏打盛大的");
        historys.add("撒大撒大撒大撒大撒大苏打倒萨大苏打倒萨大苏打倒萨倒萨");

        SearchHistoryAdapter searchHistoryAdapter = new SearchHistoryAdapter(historys, context);
        searchHistoryAdapter.setHasStableIds(true);
        searchFragmentRecyclerViewHistoryList.setAdapter(searchHistoryAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_fragment_textView_clearHistory:
                Toast.makeText(context, "点击了-清空搜索历史", Toast.LENGTH_SHORT).show();
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

        searchFragmentRecyclerViewHistoryList = null;
    }
}
