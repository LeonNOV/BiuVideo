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
        View view = LayoutInflater.from(context).inflate(R.layout.listview_item, parent, false);

        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(viewPage.coverUrl + WebpSizes.cover).into(holder.item_imageView_cover);
        holder.item_textView_title.setText(viewPage.singleVideoInfoList.get(position).part);
    }

    @Override
    public int getItemCount() {
        return viewPage.singleVideoInfoList.size();
    }

    public interface OnItemClickListener {
        void onImageViewClicked(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.onItemClickListener = clickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_imageView_cover;
        TextView item_textView_title;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            item_imageView_cover = itemView.findViewById(R.id.item_imageView_cover);
            item_textView_title = itemView.findViewById(R.id.item_textView_title);

            //设置点击事件
            item_imageView_cover.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {

                            //singleVideoIndex的值和position是同一个
                            onItemClickListener.onImageViewClicked(position);
                        }
                    }
                }
            });
        }
    }
}
