package com.leon.biuvideo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.BaseAdapter.BaseAdapter;
import com.leon.biuvideo.adapters.BaseAdapter.BaseViewHolder;
import com.leon.biuvideo.ui.dialogs.SingleVideoQualityDialog;

import java.util.List;

/**
 * 缓存视频时，选择清晰度所需要的适配器
 */
public class SingleVideoQualityAdapter extends BaseAdapter<String> {
    private final List<String> quslitys;

    public SingleVideoQualityAdapter(List<String> quslitys, Context context) {
        super(quslitys, context);

        this.quslitys = quslitys;
    }

    @Override
    public int getLayout(int viewType) {
        return R.layout.single_video_quality_dialog_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.setText(R.id.single_video_quality_item_quality, quslitys.get(position))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SingleVideoQualityDialog.onQualityItemListener.onItemClickListener(position);
                    }
                });
    }
}
