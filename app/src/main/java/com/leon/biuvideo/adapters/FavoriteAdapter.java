package com.leon.biuvideo.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.leon.biuvideo.R;
import com.leon.biuvideo.beans.Favorite;
import com.leon.biuvideo.ui.activitys.UpMasterActivity;
import com.leon.biuvideo.utils.SQLiteHelper;
import com.leon.biuvideo.utils.WebpSizes;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoriteAdapter extends BaseAdapter implements View.OnClickListener {
    private List<Favorite> favorites;
    private Context context;

    private int nowPosition;
    private Favorite nowFavorite;

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

        this.nowPosition = position;
        this.nowFavorite = favorites.get(position);

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
        viewHolder.favorite_circleImageView_face.setOnClickListener(this);

        viewHolder.favorite_textView_name.setText(favorite.name);
        viewHolder.favorite_textView_desc.setText(favorite.desc);

        //设置按钮监听事件
        viewHolder.favorite_imageView_cancel_favoriteIcon.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.favorite_imageView_cancel_favoriteIcon:

                //移除对应的收藏
                removeFavorite();
                break;
            case R.id.favorite_circleImageView_face:

                //跳转到UpMasterActivity中
                Intent intent = new Intent(context, UpMasterActivity.class);
                intent.putExtra("mid", getItem(nowPosition).mid);
                context.startActivity(intent);

                break;
            default:
                break;
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    /**
     * 将对应的信息从favorite_up中删除（从“我的收藏”中删除）
     */
    private void removeFavorite() {
        //将对应mid的isFavorite的值修改为0
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context, 1);
        SQLiteDatabase database = sqLiteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("isFavorite", 0);

        int favorite_up = database.update("favorite_up", values, "mid=?", new String[]{nowFavorite.mid + ""});

        if (favorite_up > 0) {
            //删除对应item
            favorites.remove(nowPosition);
            notifyDataSetChanged();

            Toast.makeText(context, nowFavorite.name + " 已从“我的收藏”中移除" + nowFavorite.name, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "出错了~~~", Toast.LENGTH_SHORT).show();
        }

        database.close();
        sqLiteHelper.close();
    }

    static class ViewHolder {
        CircleImageView favorite_circleImageView_face;
        TextView favorite_textView_desc, favorite_textView_name;
        Button favorite_imageView_cancel_favoriteIcon;
    }
}
