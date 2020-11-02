package com.leon.biuvideo.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.lang.reflect.Field;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String favorite_up = "CREATE TABLE \"favorite_up\" (\n" +
            "\"id\"  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "\"mid\"  INTEGER NOT NULL,\n" +
            "\"name\"  TEXT,\n" +
            "\"faceUrl\"  TEXT,\n" +
            "\"desc\"  TEXT,\n" +
            "\"isFavorite\"  INTEGER DEFAULT 1\n" +
            ");";
    private static final String musicPlayList = "CREATE TABLE \"musicPlayList\" (\n" +
            "\"id\"  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "\"sid\"  INTEGER,\n" +
            "\"bvid\"  TEXT,\n" +
            "\"author\"  TEXT,\n" +
            "\"musicName\"  TEXT,\n" +
            "\"isHaveVideo\"  INTEGER,\n" +
            "\"isDelete\"  INTEGER\n" +
            ");";

    private static final String videoPlayList = "CREATE TABLE \"videoPlayList\" (\n" +
            "\"id\"  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "\"bvid\"  TEXT,\n" +
            "\"uname\"  TEXT,\n" +
            "\"desc\"  TEXT,\n" +
            "\"coverUrl\"  TEXT,\n" +
            "\"length\"  INTEGER,\n" +
            "\"play\"  INTEGER,\n" +
            "\"danmaku\"  INTEGER,\n" +
            "\"isDelete\"  INTEGER\n" +
            ");";

    public SQLiteHelper(@Nullable Context context, int version) {
        super(context, "biu_video.db", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //创建数据库表格，通过反射获取SQL语句
        Class<SQLiteHelper> sqLiteHelperClass = SQLiteHelper.class;
        Field[] fields = sqLiteHelperClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                Object sql = field.get(field);

                sqLiteDatabase.execSQL(sql.toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

class Tables {
    public static final String FavoriteUp = "favorite_up";
    public static final String MusicPlayList = "musicPlayList";
    public static final String VideoPlayList = "videoPlayList";
}
