package com.leon.biuvideo.adapters.UserFragmentAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.searchBean.bangumi.Ep;
import com.leon.biuvideo.utils.Fuck;

import java.util.List;

/**
 * 搜索结果界面-番剧选集适配器
 */
public class BangumiEpAdapter extends BaseAdapter<Ep> {
    private final List<Ep> eps;

    public BangumiEpAdapter(List<Ep> eps, Context context) {
        super(eps, context);
        this.eps = eps;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.search_result_bangumi_ep_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Ep ep = eps.get(position);

        holder
                .setText(R.id.search_result_bangumi_ep_item_textView_indexTitle, ep.title)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fuck.blue("id:" + ep.id + "----isVIP:" + ep.isVIP + "----position:" + position);
                    }
                });
    }
}
