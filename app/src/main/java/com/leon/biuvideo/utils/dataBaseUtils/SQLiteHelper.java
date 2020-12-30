package com.leon.biuvideo.utils.dataBaseUtils;

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
            "\"visit\"  INTEGER DEFAULT 0,\n" +
            "\"isDelete\"  INTEGER DEFAULT 0\n" +
            ");";

    private static final String musicPlayList = "CREATE TABLE \"musicPlayList\" (\n" +
            "\"id\"  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "\"sid\"  INTEGER,\n" +
            "\"bvid\"  TEXT,\n" +
            "\"author\"  TEXT,\n" +
            "\"musicName\"  TEXT,\n" +
            "\"isHaveVideo\"  INTEGER,\n" +
            "\"isDelete\"  INTEGER DEFAULT 0\n" +
            ");";

    private static final String videoPlayList = "CREATE TABLE \"videoPlayList\" (\n" +
            "\"id\"  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "\"bvid\"  TEXT,\n" +
            "\"uname\"  TEXT,\n" +
            "\"title\"  TEXT,\n" +
            "\"coverUrl\"  TEXT,\n" +
            "\"length\"  INTEGER,\n" +
            "\"play\"  INTEGER,\n" +
            "\"danmaku\"  INTEGER,\n" +
            "\"isDelete\"  INTEGER DEFAULT 0\n" +
            ");";

    private static final String article = "CREATE TABLE \"article\" (\n" +
            "\"id\"  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "\"mid\"  INTEGER,\n" +
            "\"face\"  TEXT,\n" +
            "\"title\"  TEXT,\n" +
            "\"summary\"  TEXT,\n" +
            "\"coverUrl\"  TEXT,\n" +
            "\"articleId\"  INTEGER UNIQUE,\n" +
            "\"author\"  TEXT,\n" +
            "\"category\"  TEXT,\n" +
            "\"ctime\"  INTEGER,\n" +
            "\"view\"  INTEGER,\n" +
            "\"like\"  INTEGER,\n" +
            "\"reply\"  INTEGER,\n" +
            "\"isDelete\"  INTEGER DEFAULT 0\n" +
            ");";

    // 视频下载记录
    private static final String downloadRecordsForVideo = "CREATE TABLE \"downloadRecordsForVideo\" (\n" +
            "\"id\"  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "\"cover\"  TEXT,\n" +
            "\"title\"  TEXT,\n" +
            "\"upName\"  TEXT,\n" +
            "\"mainId\"  TEXT UNIQUE\n" +
            ");";

    // 视频/音乐下载记录，isVideo为视频和音频的标识，音频只需满足mainId, 视频则需要满足mainId和subId
    private static final String downloadDetailsForMedia = "CREATE TABLE \"downloadDetailsForVideo\" (\n" +
            "\"id\"  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
            "\"fileName\"  TEXT,\n" +
            "\"cover\"  TEXT,\n" +
            "\"title\"  TEXT,\n" +
            "\"size\"  LONG,\n" +
            "\"mainId\"  TEXT,\n" +
            "\"subId\"  LONG,\n" +
            "\"videoUrl\"  TEXT,\n" +
            "\"audioUrl\"  TEXT,\n" +
            "\"qualityId\"  INTEGER,\n" +
            "\"isVideo\"  INTEGER,\n" +
            "\"downloadState\"  INTEGER DEFAULT 0,\n" +
            "\"isDelete\"  INTEGER DEFAULT 0\n" +
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

