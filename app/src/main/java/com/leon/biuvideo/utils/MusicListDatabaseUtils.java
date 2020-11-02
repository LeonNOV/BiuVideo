package com.leon.biuvideo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.leon.biuvideo.beans.musicBeans.MusicPlayList;

import java.util.ArrayList;
import java.util.List;

public class MusicListDatabaseUtils {
    private final Context context;

    public MusicListDatabaseUtils(Context context) {
        this.context = context;
    }

    /**
     * 查询播放列表，且isDelete为1的条目
     *
     * @return 返回MusicPlayList集合
     */
    public List<MusicPlayList> queryPlayList() {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context, 1);
        SQLiteDatabase database = sqLiteHelper.getReadableDatabase();

        Cursor cursor = database.query("musicPlayList", null, "isDelete=?", new String[]{"1"}, null, null, null);

        List<MusicPlayList> musicPlayLists = new ArrayList<>();
        while (cursor.moveToNext()) {
            MusicPlayList musicPlayList = new MusicPlayList();

            musicPlayList.sid = cursor.getInt(cursor.getColumnIndex("sid"));

            musicPlayList.musicName = cursor.getString(cursor.getColumnIndex("musicName"));

            musicPlayList.author = cursor.getString(cursor.getColumnIndex("author"));

            musicPlayLists.add(musicPlayList);
        }

        cursor.close();
        database.close();
        sqLiteHelper.close();

        return musicPlayLists;
    }

    /**
     * 向播放列表中添加条目
     *
     * @param musicPlayList musicPlayList对象
     */
    public void addPlayList(MusicPlayList musicPlayList) {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context, 1);
        SQLiteDatabase database = sqLiteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("sid", musicPlayList.sid);
        values.put("bvid", musicPlayList.bvid);
        values.put("author", musicPlayList.author);
        values.put("musicName", musicPlayList.musicName);
        values.put("isHaveVideo", musicPlayList.isHaveVideo ? 1 : 0);
        values.put("isDelete", 1);

        long insert = database.insert("musicPlayList", null, values);

        Toast.makeText(context, insert > 0 ? "已加入到播放列表" : "添加失败~~~", Toast.LENGTH_SHORT).show();

        database.close();
    }

    /**
     * 查询对应sid的条目
     *
     * @param sid sid
     * @return 返回查询结果；true：存在、false：不存在
     */
    //43199
    public boolean queryMusic(long sid) {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context, 1);
        SQLiteDatabase database = sqLiteHelper.getReadableDatabase();

        Cursor cursor = database.query("musicPlayList", null, "sid=? and isDelete=?", new String[]{String.valueOf(sid), String.valueOf(1)}, null, null, null);

        int count = cursor.getCount();

        Log.d(LogTip.blue, "queryMusic: " + count);

        cursor.close();
        database.close();
        sqLiteHelper.close();

        return count > 0;
    }

    /**
     * 从播放列表中移除对应music
     *
     * @param sid sid
     */
    public void removeMusicItem(long sid) {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context, 1);
        SQLiteDatabase database = sqLiteHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("isDelete", 0);

        int state = database.update("musicPlayList", contentValues, "sid=?", new String[]{String.valueOf(sid)});

        Toast.makeText(context, state > 0 ? "已从播放列表中移除" : "移除失败~~~", Toast.LENGTH_SHORT).show();

        database.close();
        sqLiteHelper.close();
    }
}
