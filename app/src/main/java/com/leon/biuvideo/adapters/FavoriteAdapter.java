package com.leon.biuvideo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.Favorite;
import com.leon.biuvideo.utils.WebpSizes;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private List<Favorite> favorites;
    private Context context;

    public FavoriteAdapter(List<Favorite> favorites, Context context) {
        this.favorites = favorites;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_favorite_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Favorite favorite = favorites.get(position);

        //设置头像
        Glide.with(context).load(favorite.faceUrl + WebpSizes.face).into(holder.favorite_circleImageView_face);

        //设置昵称
        holder.favorite_textView_name.setText(favorite.name);

        //设置简介
        holder.favorite_textView_desc.setText(favorite.desc);
    }

    public interface OnItemClickListener {
        void onItemClickListener(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView favorite_circleImageView_face;
        TextView favorite_textView_desc, favorite_textView_name;
        Button favorite_imageView_cancel_favoriteIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            favorite_circleImageView_face = itemView.findViewById(R.id.favorite_circleImageView_face);
            favorite_circleImageView_face.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(v, getAdapterPosition());
                }
            });

            favorite_textView_desc = itemView.findViewById(R.id.favorite_textView_desc);
            favorite_textView_name = itemView.findViewById(R.id.favorite_textView_name);
            favorite_imageView_cancel_favoriteIcon = itemView.findViewById(R.id.favorite_imageView_cancel_favoriteIcon);
        }
    }
}