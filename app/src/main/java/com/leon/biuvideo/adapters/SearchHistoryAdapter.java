package com.leon.biuvideo.adapters;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;

import java.util.List;

public class SearchHistoryAdapter extends BaseAdapter<String> {
    private final List<String> historys;
    private final Context context;

    public SearchHistoryAdapter(List<String> historys, Context context) {
        super(historys, context);

        this.historys = historys;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.history_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder
                .setText(R.id.history_item_keyword, historys.get(position))
                .setOnClickListener(R.id.history_item_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "删除-" + historys.get(position), Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Keyword：" + historys.get(position), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
