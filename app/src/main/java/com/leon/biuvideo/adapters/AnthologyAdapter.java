package com.leon.biuvideo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.videoBean.view.ViewPage;
import com.leon.biuvideo.utils.WebpSizes;

/**
 * videoActivity播放列表控件的适配器
 */
public class AnthologyAdapter extends RecyclerView.Adapter<AnthologyAdapter.ViewHolder> {
    private final Context context;
    private final ViewPage viewPage;

    private OnItemClickListener onItemClickListener;

    public AnthologyAdapter(Context context, ViewPage viewPage) {
        this.context = context;
        this.viewPage = viewPage;
    }

    @NonNull
    @Override
    public AnthologyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_video_listview_item, parent, false);

        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(viewPage.coverUrl + WebpSizes.cover).into(holder.single_video_item_imageView_cover);

        String singleVideoIndexStr = "P" + (position + 1);
        holder.single_video_item_textView_index.setText(singleVideoIndexStr);
        holder.single_video_item_textView_title.setText(viewPage.singleVideoInfoList.get(position).part);
    }

    @Override
    public int getItemCount() {
        return viewPage.singleVideoInfoList.size();
    }

    public interface OnItemClickListener {
        void onSingleVideoClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.onItemClickListener = clickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView single_video_item_imageView_cover;
        TextView single_video_item_textView_index, single_video_item_textView_title;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            single_video_item_imageView_cover = itemView.findViewById(R.id.single_video_item_imageView_cover);
            single_video_item_textView_index = itemView.findViewById(R.id.single_video_item_textView_index);
            single_video_item_textView_title = itemView.findViewById(R.id.single_video_item_textView_title);

            //设置点击事件
            single_video_item_imageView_cover.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {

                            //singleVideoIndex的值和position是同一个
                            onItemClickListener.onSingleVideoClick(position);
                        }
                    }
                }
            });
        }
    }
}
