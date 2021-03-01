package com.leon.biuvideo.ui.discovery;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.SearchHistoryAdapter;
import com.leon.biuvideo.ui.views.SimpleTopBar;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

public class SearchFragment extends SupportFragment implements View.OnClickListener {
    private static final String ARG_MSG = "arg_msg";

    private SimpleTopBar search_fragment_topBar;
    private EditText search_fragment_editText_keyword;
    private ImageView search_fragment_imageView_clearKeyword;
    private TextView search_fragment_textView_clearHistory;
    private RecyclerView search_fragment_recyclerView_historyList;
    private Context context;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 接收数据
//        Parcelable parcelable = getArguments().getParcelable(ARG_MSG);
        this.context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        search_fragment_topBar = view.findViewById(R.id.search_fragment_topBar);
        search_fragment_topBar.setOnSimpleTopBarListener(new SimpleTopBar.OnSimpleTopBarListener() {
            @Override
            public void onLeft() {
                _mActivity.onBackPressed();
            }

            @Override
            public void onRight() {
                Toast.makeText(context, "wasd", Toast.LENGTH_SHORT).show();
            }
        });

        search_fragment_editText_keyword = view.findViewById(R.id.search_fragment_editText_keyword);
        search_fragment_imageView_clearKeyword = view.findViewById(R.id.search_fragment_imageView_clearKeyword);
        search_fragment_imageView_clearKeyword.setOnClickListener(this);

        search_fragment_textView_clearHistory = view.findViewById(R.id.search_fragment_textView_clearHistory);
        search_fragment_textView_clearHistory.setOnClickListener(this);

        search_fragment_recyclerView_historyList = view.findViewById(R.id.search_fragment_recyclerView_historyList);
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
        search_fragment_recyclerView_historyList.setAdapter(searchHistoryAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_fragment_textView_clearHistory:
                Toast.makeText(context, "点击了-清空搜索词", Toast.LENGTH_SHORT).show();
                break;
            case R.id.search_fragment_imageView_clearKeyword:
                Toast.makeText(context, "点击了-清空搜索历史", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        search_fragment_recyclerView_historyList = null;
    }
}
