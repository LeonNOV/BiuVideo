package com.leon.biuvideo.adapters.otherAdapters;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.baseAdapters.BaseAdapter;
import com.leon.biuvideo.adapters.baseAdapters.BaseViewHolder;
import com.leon.biuvideo.beans.resourcesBeans.bangumiBeans.BangumiAnthology;
import com.leon.biuvideo.ui.resourcesFragment.video.OnBottomSheetWithItemListener;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/20
 * @Desc 番剧/电视剧 选集适配器
 */
public class BangumiAnthologyAdapter extends BaseAdapter<BangumiAnthology> {
    private final int currentIndex;
    private final List<BangumiAnthology> bangumiAnthologyList;

    private OnBottomSheetWithItemListener onBottomSheetWithItemListener;

    public BangumiAnthologyAdapter(List<BangumiAnthology> beans, Context context, int currentIndex) {
        super(beans, context);
        this.currentIndex = currentIndex;
        this.bangumiAnthologyList = beans;
    }

    public void setOnBottomSheetWithItemListener(OnBottomSheetWithItemListener onBottomSheetWithItemListener) {
        this.onBottomSheetWithItemListener = onBottomSheetWithItemListener;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.video_anthology_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        BangumiAnthology bangumiAnthology = bangumiAnthologyList.get(position);

        holder.setText(R.id.video_anthology_item_name, bangumiAnthology.subTitle);
        TextView videoAnthologyItemBadge = holder.findById(R.id.video_anthology_item_badge);
        if (bangumiAnthology.badge != null) {
            videoAnthologyItemBadge.setText(bangumiAnthology.badge);
        } else {
            videoAnthologyItemBadge.setVisibility(View.GONE);
        }

        View itemView = holder.getItemView();
        itemView.setSelected(currentIndex == position);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex == position) {
                    return;
                }

                if ("会员".equals(bangumiAnthology.badge)) {
//                    SimpleSnackBar.make(view, "需要开通大会员才能进行观看哦~", SimpleSnackBar.LENGTH_LONG).show();
                    Toast toast = Toast.makeText(context, "需要开通大会员才能进行观看哦~", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 10);
                    toast.show();
                    return;
                }

                if (onBottomSheetWithItemListener != null) {
                    onBottomSheetWithItemListener.onItem(position);
                }
            }
        });
    }
}
