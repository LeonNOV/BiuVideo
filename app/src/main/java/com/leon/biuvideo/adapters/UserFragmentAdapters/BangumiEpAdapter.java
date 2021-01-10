package com.leon.biuvideo.adapters.UserFragmentAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.searchBean.bangumi.Bangumi;
import com.leon.biuvideo.beans.searchBean.bangumi.Ep;
import com.leon.biuvideo.ui.activitys.BangumiActivity;
import com.leon.biuvideo.utils.Fuck;
import com.leon.biuvideo.utils.VerifyUtils;

import java.util.List;

/**
 * 搜索结果界面-番剧选集适配器
 */
public class BangumiEpAdapter extends BaseAdapter<Ep> {
    private final List<Ep> eps;
    private final Context context;

    public BangumiEpAdapter(List<Ep> eps, Context context) {
        super(eps, context);

        this.eps = eps;
        this.context = context;
    }

    private OnEpClickListener onEpClickListener;

    public interface OnEpClickListener {
        Bangumi onEpClick(int position);
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

        holder
                .setText(R.id.search_result_bangumi_ep_item_textView_indexTitle, ep.title)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 先检查是否为VIP资源
                        if (ep.isVIP) {
                            // 判断当前是否已登录账号
                            if (VerifyUtils.isLogin(context)) {
                                // 判断已登录账号是否为大会员
                                if (VerifyUtils.isVIP(context)) {
                                    Toast.makeText(context, "该番剧需要成为大会员才能进行观看", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    toBangumi(position);
                                }
                            } else {
                                Toast.makeText(context, "该番剧需要登录账号才能观看", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else {
                            toBangumi(position);
                        }

                        Fuck.blue("id:" + ep.id + "----isVIP:" + ep.isVIP + "----position:" + position);
                    }
                });
    }

    public void toBangumi (int position) {
        Bangumi bangumi = null;

        if (onEpClickListener != null) {
            bangumi = onEpClickListener.onEpClick(position);
        }

        if (bangumi != null) {
            Intent intent = new Intent(context, BangumiActivity.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("bangumi", bangumi);
            bundle.putInt("selectAnthologyIndex", position);
            intent.putExtras(bundle);

            context.startActivity(intent);
        } else {
            Toast.makeText(context, "貌似出了点问题~~~", Toast.LENGTH_SHORT).show();
        }
    }
}
