package com.leon.biuvideo.adapters.userFragmentAdapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.searchBean.bangumi.Ep;
import com.leon.biuvideo.ui.views.SimpleSnackBar;
import com.leon.biuvideo.utils.InternetUtils;
import com.leon.biuvideo.utils.PreferenceUtils;

import java.util.List;

/**
 * 搜索结果界面-番剧选集适配器
 */
public class BangumiEpAdapter extends BaseAdapter<Ep> {
    private final Context context;
    private final List<Ep> eps;

    public BangumiEpAdapter(Context context, List<Ep> eps) {
        super(eps, context);
        this.eps = eps;
        this.context = context;
    }

    private OnEpClickListener onEpClickListener;

    public interface OnEpClickListener {
        void onEpClick(int position);
    }

    public void setOnEpClickListener(OnEpClickListener onEpClickListener) {
        this.onEpClickListener = onEpClickListener;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.search_result_bangumi_ep_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Ep ep = eps.get(position);

        if (ep.badge != null) {
            holder.setText(R.id.search_result_bangumi_ep_item_textView_badge, ep.badge);
        } else {
            holder.setVisibility(R.id.search_result_bangumi_ep_item_textView_badge, View.GONE);
        }

        holder
                .setText(R.id.search_result_bangumi_ep_item_textView_indexTitle, ep.title)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!InternetUtils.checkNetwork(context)) {
                            SimpleSnackBar.make(v, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();
                            return;
                        }

                        // 先检查是否为VIP资源
                        if (ep.isVIP) {
                            // 判断当前是否已登录账号
                            if (PreferenceUtils.getLoginStatus()) {

                                // 判断已登录账号是否为大会员
                                if (!PreferenceUtils.getVipStatus()) {
                                    SimpleSnackBar.make(v, "该番剧需要成为大会员才能进行观看", SimpleSnackBar.LENGTH_SHORT).show();
                                } else {
                                    toBangumi(position);
                                }
                            } else {
                                SimpleSnackBar.make(v, "该番剧需要登录账号才能观看", SimpleSnackBar.LENGTH_SHORT).show();
                            }
                        } else {
                            toBangumi(position);
                        }
                    }
                });
    }

    public void toBangumi (int position) {
        if (onEpClickListener != null) {
            onEpClickListener.onEpClick(position);
        }
    }
}
