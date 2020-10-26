package com.leon.biuvideo.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.Favorite;
import com.leon.biuvideo.utils.WebpSizes;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoriteAdapter extends BaseAdapter {
    private List<Favorite> favorites;
    private Context context;

    public FavoriteAdapter(List<Favorite> favorites, Context context) {
        this.favorites = favorites;
        this.context = context;
    }

    @Override
    public int getCount() {
        return favorites.size();
    }

    @Override
    public Favorite getItem(int position) {
        return favorites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.fragment_favorite_item, null);

            viewHolder = new ViewHolder();

            viewHolder.favorite_circleImageView_face = convertView.findViewById(R.id.favorite_circleImageView_face);
            viewHolder.favorite_textView_desc = convertView.findViewById(R.id.favorite_textView_desc);
            viewHolder.favorite_textView_name = convertView.findViewById(R.id.favorite_textView_name);
            viewHolder.favorite_imageView_cancel_favoriteIcon = convertView.findViewById(R.id.favorite_imageView_cancel_favoriteIcon);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Favorite favorite = favorites.get(position);
        Glide.with(context).load(favorite.faceUrl + WebpSizes.face).into(viewHolder.favorite_circleImageView_face);
        viewHolder.favorite_textView_name.setText(favorite.name);
        viewHolder.favorite_textView_desc.setText(favorite.desc);

        //设置按钮监听事件
//        viewHolder.favorite_imageView_cancel_favoriteIcon.setOnClickListener(this);

        return convertView;
    }

//    @Override
//    public void onClick(View v) {
    //移除对应的收藏
//        removeFavorite();
//    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    static class ViewHolder {
        CircleImageView favorite_circleImageView_face;
        TextView favorite_textView_desc, favorite_textView_name;
        Button favorite_imageView_cancel_favoriteIcon;
    }
}