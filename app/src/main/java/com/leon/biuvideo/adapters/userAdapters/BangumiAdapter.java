package com.leon.biuvideo.adapters.userAdapters;

import android.content.Context;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.searchResultBeans.bangumi.Bangumi;

import java.util.List;

/**
 * 搜索结果界面-番剧列表适配器
 */
public class BangumiAdapter extends BaseAdapter<Bangumi> {
    private final List<Bangumi> bangumiList;
    private final Context context;

    public BangumiAdapter(List<Bangumi> bangumiList, Context context) {
        super(bangumiList, context);

        this.bangumiList = bangumiList;
        this.context = context;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.search_result_item_bangumi;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        /*Bangumi bangumi = bangumiList.get(position);
        holder
                .setImage(R.id.search_result_bangumi_imageView_cover, bangumi.cover, ImagePixelSize.COVER)
                .setText(R.id.search_result_bangumi_textView_title, bangumi.title)
                .setText(R.id.search_result_bangumi_textView_pubTime, ValueUtils.generateTime(bangumi.playTime, true, true, "-"))
                .setText(R.id.search_result_bangumi_textView_area, bangumi.area)
                .setText(R.id.search_result_bangumi_textView_style, bangumi.styles)
                .setText(R.id.search_result_bangumi_textView_score, String.valueOf(bangumi.score))
                .setText(R.id.search_result_bangumi_textView_reviewNum, ValueUtils.generateCN(bangumi.reviewNum) + "人点评")
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!InternetUtils.checkNetwork(context)) {
                            SimpleSnackBar.make(v, R.string.networkWarn, SimpleSnackBar.LENGTH_SHORT).show();
                            return;
                        }

                        Intent intent = new Intent(context, BangumiActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("bangumi", bangumi);
                        intent.putExtras(bundle);

                        context.startActivity(intent);
                    }
                });

        // 如果角标题为空的话就不显示该控件
        if (bangumi.angleTitle.equals("")) {
            holder.setVisibility(R.id.search_result_bangumi_textView_angleTitle, View.GONE);
        } else {
            holder.setText(R.id.search_result_bangumi_textView_angleTitle, bangumi.angleTitle);
        }

        RecyclerView recyclerView = holder.findById(R.id.search_result_bangumi_recyclerView_eps);

        BangumiEpAdapter bangumiEpAdapter = new BangumiEpAdapter(context, bangumi.eps);
        bangumiEpAdapter.setOnEpClickListener(new BangumiEpAdapter.OnEpClickListener() {
            @Override
            public void onEpClick(int position) {
                Intent intent = new Intent(context, BangumiActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("bangumi", bangumi);
                bundle.putInt("selectAnthologyIndex", position);
                intent.putExtras(bundle);

                context.startActivity(intent);
            }
        });
        recyclerView.setAdapter(bangumiEpAdapter);*/
    }
}
