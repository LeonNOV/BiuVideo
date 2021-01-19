package com.leon.biuvideo.ui.fragments.localOrderFragments;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.localOrderAdapters.LocalOrderBaseAdapter;
import com.leon.biuvideo.beans.orderBeans.LocalOrder;
import com.leon.biuvideo.ui.fragments.baseFragment.BaseFragment;
import com.leon.biuvideo.ui.fragments.baseFragment.BindingUtils;
import com.leon.biuvideo.utils.dataBaseUtils.LocalOrdersDatabaseUtils;
import com.leon.biuvideo.values.LocalOrderType;

import java.util.List;

/**
 * 本地订阅Fragment,根据LocalOrderType创建Adapter
 */
public class LocalOrderBaseFragment extends BaseFragment {
    private TextView textView_noDataStr;
    private RecyclerView recyclerView;
    private LocalOrderType localOrderType;
    private LocalOrdersDatabaseUtils localOrdersDatabaseUtils;

    public LocalOrderBaseFragment() {
    }

    public LocalOrderBaseFragment(LocalOrderType localOrderType) {
        this.localOrderType = localOrderType;
    }

    @Override
    public int setLayout() {
        return R.layout.main_fragment_recycler_view;
    }

    @Override
    public void initView(BindingUtils bindingUtils) {
        textView_noDataStr = findView(R.id.textView_noDataStr);
        recyclerView = findView(R.id.recyclerView);
    }

    @Override
    public void initValues() {
        if (localOrdersDatabaseUtils == null) {
            localOrdersDatabaseUtils = new LocalOrdersDatabaseUtils(context);
        }
        List<LocalOrder> localOrderList = localOrdersDatabaseUtils.queryLocalOrder(localOrderType);

        if (localOrderList == null || localOrderList.size() <= 0) {
            textView_noDataStr.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textView_noDataStr.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            LocalOrderBaseAdapter localOrderBaseAdapter = new LocalOrderBaseAdapter(context, localOrderList, localOrderType);
            recyclerView.setAdapter(localOrderBaseAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }
    }

    @Override
    public void onResume() {
        initValues();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (localOrdersDatabaseUtils != null) {
            localOrdersDatabaseUtils.close();
        }
        super.onDestroy();
    }
}
