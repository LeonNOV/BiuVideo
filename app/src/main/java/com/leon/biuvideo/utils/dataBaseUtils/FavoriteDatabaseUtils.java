package com.leon.biuvideo.utils.dataBaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.leon.biuvideo.beans.Favorite;
import com.leon.biuvideo.values.Tables;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理favorite_up表的工具类
 */
public class FavoriteDatabaseUtils extends SQLiteHelper {
    private final SQLiteHelper sqLiteHelper;
    private final SQLiteDatabase sqLiteDatabase;
    private final String dbName  = Tables.FavoriteUp.value;

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
        contentValues.put("isDelete", 1);

        int state = sqLiteDatabase.update(dbName, contentValues, "mid = ?", new String[]{String.valueOf(mid)});

        return  state > 0;
    }

    /**
     * 查询对应用户是否存在于favorite_up库中
     *
     * @param mid 用户id
     * @return true：存在；false：不存在
     */
    public boolean queryFavoriteState(long mid) {
        Cursor cursor = sqLiteDatabase.query(dbName, new String[]{"isDelete"}, "mid=? and isDelete = ?", new String[]{String.valueOf(mid), "0"}, null, null, null);

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
        Cursor cursor = sqLiteDatabase.query(dbName, null, "isDelete = ?", new String [] {"0"}, null, null, "id DESC");

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

        long insert = sqLiteDatabase.insert(dbName, null, values);

        return insert > 0;
    }

    /**
     * 查询关注列表数据，按照访问量进行排序
     *
     * @return  返回favorites
     */
    public List<Favorite> queryFavoritesByVisit() {
        Cursor cursor = sqLiteDatabase.query(dbName, null, "isDelete = ?", new String [] {"0"}, null, null, "visit DESC");

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
        Cursor cursor = sqLiteDatabase.query(dbName, null, "mid = ? AND isDelete = ?", new String[]{String.valueOf(mid), "0"}, null, null, null);

        if (cursor.getCount() < 1) {
            return;
        }
        
        cursor.moveToPosition(0);

        int visit = cursor.getInt(cursor.getColumnIndex("visit"));
        visit++;
        cursor.close();

        ContentValues contentValues = new ContentValues();
        contentValues.put("visit", visit);

        sqLiteDatabase.update(dbName, contentValues, "mid = ?", new String[]{String.valueOf(mid)});
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
