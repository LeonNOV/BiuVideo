package com.leon.biuvideo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.view.ViewPage;
import com.leon.biuvideo.utils.WebpSizes;

import java.util.zip.Inflater;

/**
 * video_listView_singleVideoList控件的适配器
 */
public class AnthologyAdapter extends RecyclerView.Adapter<AnthologyAdapter.ViewHolder> {
    private Context context;
    private ViewPage viewPage;

    public AnthologyAdapter(Context context, ViewPage viewPage) {
        this.context = context;
        this.viewPage = viewPage;
    }

    @NonNull
    @Override
    public AnthologyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview_item, parent, false);

        return new ViewHolder(view);
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_imageView_cover;
        TextView item_textView_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_imageView_cover = itemView.findViewById(R.id.item_imageView_cover);
            item_textView_title = itemView.findViewById(R.id.item_textView_title);
        }
    }
}
