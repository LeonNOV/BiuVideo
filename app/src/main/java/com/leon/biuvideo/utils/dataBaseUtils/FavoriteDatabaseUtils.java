package com.leon.biuvideo.utils.dataBaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.leon.biuvideo.beans.Favorite;
import com.leon.biuvideo.utils.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDatabaseUtils extends SQLiteHelper {
    private final SQLiteHelper sqLiteHelper;
    private final SQLiteDatabase sqLiteDatabase;

    public FavoriteDatabaseUtils(Context context) {
        super(context, 1);

        this.sqLiteHelper = new SQLiteHelper(context, 1);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
    }

    /**
     * 从关注列表中移除对应mid条目
     *
     * @param mid mid
     */
    public boolean removeFavorite(long mid) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("isDelete", 0);

        int state = sqLiteDatabase.update(Tables.FavoriteUp.value, contentValues, "mid=?", new String[]{String.valueOf(mid)});

        return  state > 0;
    }

    /**
     * 查询对应用户是否存在于favorite_up库中
     *
     * @param mid 用户id
     * @return true：存在；false：不存在
     */
    public boolean queryFavoriteState(long mid) {
        Cursor cursor = sqLiteDatabase.query(Tables.FavoriteUp.value, new String[]{"isDelete"}, "mid=? and isDelete=?", new String[]{String.valueOf(mid), "1"}, null, null, null);

        int count = cursor.getCount();

        cursor.close();
        return count > 0;
    }

    /**
     * 获取favorite_up库中isDelete为1的数据
     *
     * @return  返回favorites
     */
    public List<Favorite> queryFavorites() {
        Cursor cursor = sqLiteDatabase.query(Tables.FavoriteUp.value, null, "isDelete=?", new String [] {"1"}, null, null, "id DESC");

        List<Favorite> favorites = new ArrayList<>();
        while (cursor.moveToNext()) {
            Favorite favorite = new Favorite();

            favorite.mid = cursor.getLong(cursor.getColumnIndex("mid"));
            favorite.name = cursor.getString(cursor.getColumnIndex("name"));
            favorite.faceUrl = cursor.getString(cursor.getColumnIndex("faceUrl"));
            favorite.desc = cursor.getString(cursor.getColumnIndex("desc"));

            favorites.add(favorite);
        }

        cursor.close();
        return favorites;
    }

    /**
     * 将UP的数据导入favorite_up库中
     */
    public boolean addFavorite(Favorite favorite) {
        ContentValues values = new ContentValues();
        values.put("mid", favorite.mid);
        values.put("name", favorite.name);
        values.put("faceUrl", favorite.faceUrl);
        values.put("desc", favorite.desc);
        values.put("isDelete", 1);//1：正在关注；0：已取消关注

        long insert = sqLiteDatabase.insert(Tables.FavoriteUp.value, null, values);

        return insert > 0;
    }

    /**
     * 查询关注列表数据，按照访问量进行排序
     *
     * @return  返回favorites
     */
    public List<Favorite> queryFavoritesByVisit() {
        Cursor cursor = sqLiteDatabase.query(Tables.FavoriteUp.value, null, "isDelete=?", new String [] {"1"}, null, null, "visit DESC");

        List<Favorite> favorites = new ArrayList<>();
        while (cursor.moveToNext()) {
            Favorite favorite = new Favorite();

            favorite.mid = cursor.getLong(cursor.getColumnIndex("mid"));
            favorite.name = cursor.getString(cursor.getColumnIndex("name"));
            favorite.faceUrl = cursor.getString(cursor.getColumnIndex("faceUrl"));
            favorite.desc = cursor.getString(cursor.getColumnIndex("desc"));

            favorites.add(favorite);
        }

        cursor.close();
        return favorites;
    }

    /**
     * 更新visit
     *
     * @param mid   用户id
     */
    public void updateVisit(long mid) {
        Cursor cursor = sqLiteDatabase.query(Tables.FavoriteUp.value, null, "mid=? and isDelete=?", new String[]{String.valueOf(mid), "1"}, null, null, null);

        cursor.moveToPosition(0);

        int visit = cursor.getInt(cursor.getColumnIndex("visit"));
        visit++;
        cursor.close();

        ContentValues contentValues = new ContentValues();
        contentValues.put("visit", visit);

        sqLiteDatabase.update(Tables.FavoriteUp.value, contentValues, "mid=?", new String[]{String.valueOf(mid)});
    }

    /**
     * 关闭数据库连接
     */
    @Override
    public void close() {
        if (sqLiteHelper != null) {
            sqLiteDatabase.close();
            sqLiteHelper.close();
        }
    }
}
