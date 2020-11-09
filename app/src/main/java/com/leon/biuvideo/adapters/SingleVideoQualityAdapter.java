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

import java.util.List;

/**
 * 缓存视频时，选择清晰度所需要的适配器
 */
public class SingleVideoQualityAdapter extends RecyclerView.Adapter<SingleVideoQualityAdapter.ViewHolder> {
    private List<String> quslitys;
    private final Context context;

    public SingleVideoQualityAdapter(List<String> quslitys, Context context) {
        this.quslitys = quslitys;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_video_quality_dialog_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.single_video_quality_item_quality.setText(quslitys.get(position));
    }

    @Override
    public int getItemCount() {
        return quslitys.size();
    }

    private OnQualityItemListener onQualityItemListener;

    public interface OnQualityItemListener {
        void onItemListener(int position);
    }

    public void setOnQualityItemListener(OnQualityItemListener onQualityItemListener) {
        this.onQualityItemListener = onQualityItemListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView single_video_quality_item_quality;
        RelativeLayout single_video_quality_relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            single_video_quality_relativeLayout = itemView.findViewById(R.id.single_video_quality_relativeLayout);
            single_video_quality_relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onQualityItemListener != null) {
                        onQualityItemListener.onItemListener(getAdapterPosition());
                    }
                }
            });

            single_video_quality_item_quality = itemView.findViewById(R.id.single_video_quality_item_quality);
        }
    }
}
