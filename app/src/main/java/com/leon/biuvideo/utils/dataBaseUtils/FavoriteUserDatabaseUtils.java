package com.leon.biuvideo.utils.dataBaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.leon.biuvideo.beans.Favorite;
import com.leon.biuvideo.utils.InitValueUtils;
import com.leon.biuvideo.values.Tables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理favorite_up表的工具类
 */
public class FavoriteUserDatabaseUtils extends SQLiteHelper {
    private final SQLiteHelper sqLiteHelper;
    private final SQLiteDatabase sqLiteDatabase;
    private final String tableName = Tables.FavoriteUp.value;

    public FavoriteUserDatabaseUtils(Context context) {
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
        int state = sqLiteDatabase.delete(tableName, "mid = ?", new String[]{String.valueOf(mid)});
        return  state > 0;
    }

    /**
     * '清除用户数据'使用
     */
    public boolean removeFavorite() {
        int state = sqLiteDatabase.delete(tableName, "isUser = ?", new String[]{"1"});
        return  state > 0;
    }

    /**
     * 查询对应用户是否存在于favorite_up库中
     *
     * @param mid 用户id
     * @return true：存在；false：不存在
     */
    public boolean queryFavoriteState(long mid) {
        Cursor cursor = sqLiteDatabase.query(tableName, new String[]{"id"}, "mid = ? AND isDelete = ?", new String[]{String.valueOf(mid), "0"}, null, null, null);

        int count = cursor.getCount();

        cursor.close();
        return count > 0;
    }

    /**
     * 获取favorite_up库中isDelete为0的数据
     *
     * @return  返回favorites
     */
    public List<Favorite> queryFavorites(boolean isByVisit) {
        Cursor cursor = sqLiteDatabase.query(tableName, null, "isDelete = ?", new String [] {"0"}, null, null, isByVisit ? "visit DESC" : "id DESC");

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
        if (favorite.name.equals("账号已注销")) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put("mid", favorite.mid);
        values.put("name", favorite.name);
        values.put("faceUrl", favorite.faceUrl);
        values.put("desc", favorite.desc);

        long insert = sqLiteDatabase.insert(tableName, null, values);

        return insert > 0;
    }

    /**
     * 将用户的关注列表数据导入到本地
     */
    public Map<String, Long> addFavorite(List<Favorite> favorites) {
        long successNum = 0;
        long failNum = 0;
        for (Favorite favorite : favorites) {
            ContentValues values = new ContentValues();
            values.put("mid", favorite.mid);
            values.put("name", favorite.name);
            values.put("faceUrl", favorite.faceUrl);
            values.put("desc", favorite.desc);
            values.put("isUser", 1);

            long insert;
            try {
                insert = sqLiteDatabase.insert(tableName, null, values);
            } catch (SQLException e) {
                failNum ++;
                continue;
            }

            if (insert > 0) {
                successNum++;
            } else {
                failNum++;
            }
        }

        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("successNum", successNum);
        resultMap.put("failNum", failNum);

        return resultMap;
    }

    /**
     * 更新visit
     *
     * @param mid   用户id
     */
    public void updateVisit(long mid) {
        Cursor cursor = sqLiteDatabase.query(tableName, null, "mid = ? AND isDelete = ?", new String[]{String.valueOf(mid), "0"}, null, null, null);

        if (cursor.getCount() < 1) {
            return;
        }
        
        cursor.moveToPosition(0);

        int visit = cursor.getInt(cursor.getColumnIndex("visit"));
        visit++;
        cursor.close();

        ContentValues contentValues = new ContentValues();
        contentValues.put("visit", visit);

        sqLiteDatabase.update(tableName, contentValues, "mid = ?", new String[]{String.valueOf(mid)});
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
