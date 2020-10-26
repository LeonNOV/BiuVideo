package com.leon.biuvideo.ui.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.leon.biuvideo.R;
import com.leon.biuvideo.adapters.FavoriteAdapter;
import com.leon.biuvideo.beans.Favorite;
import com.leon.biuvideo.ui.activitys.UpMasterActivity;
import com.leon.biuvideo.utils.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView favorite_listView;
    private TextView favorite_textView_noDataStr;
    private Context context;

    private View view;

    private List<Favorite> favorites;
    private FavoriteAdapter favoriteAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();
        view = getView();

        initView();
        initValue();
    }

    private void initView() {
        favorite_listView = view.findViewById(R.id.favorite_listView);
        favorite_listView.setOnItemClickListener(this);

        favorite_textView_noDataStr = view.findViewById(R.id.favorite_textView_noDataStr);
    }

    private void initValue() {
        //获取favorite_up库中的数据
        favorites = getFavorites();

        //判断数据是否为0
        if (favorites.size() == 0) {
            favorite_textView_noDataStr.setVisibility(View.VISIBLE);
            favorite_listView.setVisibility(View.INVISIBLE);
        } else {
            favorite_textView_noDataStr.setVisibility(View.INVISIBLE);
            favorite_listView.setVisibility(View.VISIBLE);
        }

        favoriteAdapter = new FavoriteAdapter(favorites, context);
        favorite_listView.setAdapter(favoriteAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()) {
            case R.id.favorite_circleImageView_face:
                //跳转到UpMasterActivity中
                Intent intent = new Intent(context, UpMasterActivity.class);
                intent.putExtra("mid", favorites.get(position).mid);
                startActivity(intent);

                break;
            case R.id.favorite_imageView_cancel_favoriteIcon:
                removeFavorite(position);
                break;
        }
    }

    //获取favorite_up库中isFavorite为1的数据
    private List<Favorite> getFavorites() {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context, 1);
        SQLiteDatabase database = sqLiteHelper.getReadableDatabase();

        Cursor favoriteUp = database.query("favorite_up", new String[]{"mid", "name", "faceUrl", "desc", "isFavorite"}, "isFavorite=1", null, null, null, null);

        List<Favorite> favorites = new ArrayList<>();
        while (favoriteUp.moveToNext()) {
            Favorite favorite = new Favorite();

            favorite.mid = favoriteUp.getLong(favoriteUp.getColumnIndex("mid"));
            favorite.name = favoriteUp.getString(favoriteUp.getColumnIndex("name"));
            favorite.faceUrl = favoriteUp.getString(favoriteUp.getColumnIndex("faceUrl"));
            favorite.desc = favoriteUp.getString(favoriteUp.getColumnIndex("desc"));
            favorite.isFavorite = favoriteUp.getInt(favoriteUp.getColumnIndex("isFavorite"));

            favorites.add(favorite);
        }

        favoriteUp.close();
        database.close();
        sqLiteHelper.close();

        return favorites;
    }

    /**
     * 将对应的信息从favorite_up中删除（从“我的收藏”中删除）
     */
    private void removeFavorite(int position) {
        Favorite favorite = favorites.get(position);

        //将对应mid的isFavorite的值修改为0
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context, 1);
        SQLiteDatabase database = sqLiteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("isFavorite", 0);

        int favorite_up = database.update("favorite_up", values, "mid=?", new String[]{favorite.mid + ""});

        if (favorite_up > 0) {
            //删除对应item
            favorites.remove(position);
            favoriteAdapter.notifyDataSetChanged();

            Toast.makeText(context, favorite.name + " 已从“我的收藏”中移除" + favorite.name, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "出错了~~~", Toast.LENGTH_SHORT).show();
        }

        database.close();
        sqLiteHelper.close();
    }
}
