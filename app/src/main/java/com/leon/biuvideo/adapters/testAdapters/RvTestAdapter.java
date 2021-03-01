package com.leon.biuvideo.adapters.testAdapters;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.TestBeans.RvTestBean;

import java.util.List;

public class RvTestAdapter extends BaseAdapter<RvTestBean> {
    private final List<RvTestBean> rvTestBeans;
    private final Context context;

    public RvTestAdapter(List<RvTestBean> rvTestBeans, Context context) {
        super(rvTestBeans, context);

        this.rvTestBeans = rvTestBeans;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.rv_test_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        RvTestBean rvTestBean = rvTestBeans.get(position);

        holder
                .setImage(R.id.imageView_cover, R.drawable.ic_launcher_background)
                .setText(R.id.textView_title, rvTestBean.title)
                .setText(R.id.textView_play, rvTestBean.view + "")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "点击了第" + position + "个item.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
