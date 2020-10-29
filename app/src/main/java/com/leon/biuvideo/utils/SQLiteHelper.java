package com.leon.biuvideo.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.leon.biuvideo.beans.musicBeans.MusicPlayList;

import java.lang.reflect.Field;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String favorite_up = "CREATE TABLE \"favorite_up\" (\n" +
            "\"id\"  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "\"mid\"  INTEGER NOT NULL,\n" +
            "\"name\"  TEXT,\n" +
            "\"faceUrl\"  TEXT,\n" +
            "\"desc\"  TEXT,\n" +
            "\"isFavorite\"  INTEGER DEFAULT 1\n" +
            ");";
    private static final String musicPlayList = "CREATE TABLE musicPlayList (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, sid INTEGER, author TEXT, musicName TEXT, isDelete INTEGER);";

    public SQLiteHelper(@Nullable Context context, int version) {
        super(context, "biu_video.db", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //创建数据库表格
        Class<SQLiteHelper> sqLiteHelperClass = SQLiteHelper.class;
        Field[] fields = sqLiteHelperClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                Object sql = field.get(field);

                sqLiteDatabase.execSQL(String.valueOf(sql));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
