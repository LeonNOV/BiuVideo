package com.leon.biuvideo.utils.dataBaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.leon.biuvideo.beans.musicBeans.MusicPlayList;
import com.leon.biuvideo.values.Tables;

import java.util.ArrayList;
import java.util.List;

public class MusicListDatabaseUtils extends SQLiteHelper {
    private final SQLiteHelper sqLiteHelper;
    private final SQLiteDatabase sqLiteDatabase;

    public MusicListDatabaseUtils(Context context) {
        super(context, 1);

        this.sqLiteHelper = new SQLiteHelper(context, 1);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
    }

    /**
     * 查询播放列表，且isDelete为1的条目
     *
     * @return 返回MusicPlayList集合
     */
    public List<MusicPlayList> queryPlayList() {
        Cursor cursor = sqLiteDatabase.query(Tables.MusicPlayList.value, null, "isDelete = ?", new String[]{"0"}, null, null, null);

        List<MusicPlayList> musicPlayLists = new ArrayList<>();
        while (cursor.moveToNext()) {
            MusicPlayList musicPlayList = new MusicPlayList();

            musicPlayList.sid = cursor.getInt(cursor.getColumnIndex("sid"));
            musicPlayList.bvid = cursor.getString(cursor.getColumnIndex("bvid"));
            musicPlayList.musicName = cursor.getString(cursor.getColumnIndex("musicName"));
            musicPlayList.author = cursor.getString(cursor.getColumnIndex("author"));
            musicPlayList.isHaveVideo = cursor.getInt(cursor.getColumnIndex("isHaveVideo"))  == 1;

            musicPlayLists.add(musicPlayList);
        }

        cursor.close();

        return musicPlayLists;
    }

    /**
     * 向播放列表中添加条目
     *
     * @param musicPlayList musicPlayList对象
     */
    public boolean addPlayList(MusicPlayList musicPlayList) {
        ContentValues values = new ContentValues();
        values.put("sid", musicPlayList.sid);
        values.put("bvid", musicPlayList.bvid);
        values.put("author", musicPlayList.author);
        values.put("musicName", musicPlayList.musicName);
        values.put("isHaveVideo", musicPlayList.isHaveVideo ? 1 : 0);
        values.put("isDelete", 1);

        long insert = sqLiteDatabase.insert(Tables.MusicPlayList.value, null, values);

        return insert > 0;
    }

    /**
     * 查询对应sid的条目
     *
     * @param sid sid
     * @return 返回查询结果；true：存在、false：不存在
     */
    public boolean queryMusic(long sid) {
        Cursor cursor = sqLiteDatabase.query(Tables.MusicPlayList.value, null, "sid = ? AND isDelete = ?", new String[]{String.valueOf(sid), String.valueOf(0)}, null, null, null);

        int count = cursor.getCount();

        cursor.close();
        return count > 0;
    }

    /**
     * 从播放列表中移除对应music
     *
     * @param sid sid
     */
    public boolean removeMusicItem(long sid) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("isDelete", 1);

        int state = sqLiteDatabase.update(Tables.MusicPlayList.value, contentValues, "sid = ?", new String[]{String.valueOf(sid)});

        return state > 0;
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
