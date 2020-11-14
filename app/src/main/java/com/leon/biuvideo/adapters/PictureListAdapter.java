package com.leon.biuvideo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;

import java.util.List;

public class PictureListAdapter extends RecyclerView.Adapter<PictureListAdapter.ViewHolder> {
    private Context context;
    private List<String> pictures;
    private final String picturePixelSize;

    public PictureListAdapter(Context context, List<String> pictures, String picturePixelSize) {
        this.context = context;
        this.pictures = pictures;
        this.picturePixelSize = picturePixelSize;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.picture_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(pictures.get(position) + picturePixelSize).into(holder.picture_imageView_item);
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    private OnPictureItemClickListener onPictureItemClickListener;

    public interface OnPictureItemClickListener {
        void onItemClick(int position);
    }

    public void setOnPictureItemClickListener(OnPictureItemClickListener onPictureItemClickListener) {
        this.onPictureItemClickListener = onPictureItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picture_imageView_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            picture_imageView_item = itemView.findViewById(R.id.picture_imageView_item);

            picture_imageView_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPictureItemClickListener != null) {
                        onPictureItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
