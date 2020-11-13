package com.leon.biuvideo.utils.dataUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.leon.biuvideo.beans.Favorite;
import com.leon.biuvideo.utils.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDatabaseUtils {
    private Context context;

    public FavoriteDatabaseUtils(Context context) {
        this.context = context;
    }

    /**
     * 从关注列表中移除对应mid条目
     *
     * @param mid mid
     */
    public void removeFavorite(long mid) {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context, 1);
        SQLiteDatabase database = sqLiteHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("isDelete", 0);

        int state = database.update("favorite_up", contentValues, "mid=?", new String[]{String.valueOf(mid)});

        Toast.makeText(context, state > 0 ? "已从关注列表中移除" : "移除失败~~~", Toast.LENGTH_SHORT).show();

        database.close();
        sqLiteHelper.close();
    }

    /**
     * 查询对应用户是否存在于favorite_up库中
     *
     * @param mid 用户id
     * @return true：存在；false：不存在
     */
    public boolean queryFavoriteState(long mid) {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context, 1);
        SQLiteDatabase database = sqLiteHelper.getReadableDatabase();

        boolean state;

        Cursor favoriteUp = database.query("favorite_up", new String[]{"isDelete"}, "mid=? and isDelete=?", new String[]{String.valueOf(mid), "1"}, null, null, null);

        state = favoriteUp.getCount() > 0;

        favoriteUp.close();
        database.close();
        sqLiteHelper.close();

        return state;
    }

    //获取favorite_up库中isDelete为1的数据
    public List<Favorite> queryFavorites() {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context, 1);
        SQLiteDatabase database = sqLiteHelper.getReadableDatabase();

        Cursor favoriteUp = database.query("favorite_up", null, "isDelete=?", new String [] {"1"}, null, null, null);

        List<Favorite> favorites = new ArrayList<>();
        while (favoriteUp.moveToNext()) {
            Favorite favorite = new Favorite();

            favorite.mid = favoriteUp.getLong(favoriteUp.getColumnIndex("mid"));
            favorite.name = favoriteUp.getString(favoriteUp.getColumnIndex("name"));
            favorite.faceUrl = favoriteUp.getString(favoriteUp.getColumnIndex("faceUrl"));
            favorite.desc = favoriteUp.getString(favoriteUp.getColumnIndex("desc"));

            favorites.add(favorite);
        }

        favoriteUp.close();
        database.close();
        sqLiteHelper.close();

        return favorites;
    }

    /**
     * 将UP的数据导入favorite_up库中
     */
    public void addFavorite(Favorite favorite) {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context, 1);
        SQLiteDatabase database = sqLiteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("mid", favorite.mid);
        values.put("name", favorite.name);
        values.put("faceUrl", favorite.faceUrl);
        values.put("desc", favorite.desc);
        values.put("isDelete", 1);//1：正在关注；0：已取消关注

        database.insert("favorite_up", null, values);

        Toast.makeText(context, favorite.name + " 已加入到“我的收藏”中", Toast.LENGTH_SHORT).show();

        sqLiteHelper.close();
        database.close();
    }
}
