package com.leon.biuvideo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.leon.biuvideo.beans.upMasterBean.VideoPlayList;

import java.util.ArrayList;
import java.util.List;

public class VideoListDatabaseUtils {
    private final Context context;

    public VideoListDatabaseUtils(Context context) {
        this.context = context;
    }

    /**
     * 查询所有喜欢的视频
     *
     * @return  返回VideoPlayList集合
     */
    public List<VideoPlayList> queryFavoriteVideos() {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context, 1);
        SQLiteDatabase database = sqLiteHelper.getReadableDatabase();

        Cursor cursor = database.query(Tables.VideoPlayList, null, "isDelete=?", new String[]{"1"}, null, null, null);

        List<VideoPlayList> videoPlayLists = new ArrayList<>();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                VideoPlayList videoPlayList = new VideoPlayList();

                videoPlayList.bvid = cursor.getString(cursor.getColumnIndex("bvid"));
                videoPlayList.uname = cursor.getString(cursor.getColumnIndex("uname"));
                videoPlayList.desc = cursor.getString(cursor.getColumnIndex("desc"));
                videoPlayList.coverUrl = cursor.getString(cursor.getColumnIndex("coverUrl"));
                videoPlayList.length = cursor.getInt(cursor.getColumnIndex("length"));
                videoPlayList.play = cursor.getInt(cursor.getColumnIndex("play"));
                videoPlayList.danmaku = cursor.getInt(cursor.getColumnIndex("danmaku"));

                videoPlayLists.add(videoPlayList);
            }
        }

        cursor.close();
        database.close();
        sqLiteHelper.close();

        return videoPlayLists;
    }

    /**
     * 查询视频是否存在于videoPlayList中
     *
     * @param bvid  要查询的视频的bvid
     * @return  是否存在，小于等于0不存在，大于0则存在
     */
    public boolean queryFavoriteVideo (String bvid) {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context, 1);
        SQLiteDatabase database = sqLiteHelper.getReadableDatabase();

        Cursor cursor = database.query(Tables.VideoPlayList, null, "bvid=? and isDelete=?", new String[]{bvid, "1"}, null, null, null);
        int count = cursor.getCount();

        cursor.close();
        database.close();
        sqLiteHelper.close();

        return count > 0;
    }

    /**
     * 添加视频到播放列表
     *
     * @param videoPlayList 视频信息
     * @return 是否添加成功
     */
    public boolean addFavoriteVideo (VideoPlayList videoPlayList) {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context, 1);
        SQLiteDatabase database = sqLiteHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("bvid", videoPlayList.bvid);
        contentValues.put("uname", videoPlayList.uname);
        contentValues.put("desc", videoPlayList.desc);
        contentValues.put("coverUrl", videoPlayList.coverUrl);
        contentValues.put("length", videoPlayList.length);
        contentValues.put("play", videoPlayList.play);
        contentValues.put("danmaku", videoPlayList.danmaku);
        contentValues.put("isDelete", 1);

        long insert = database.insert(Tables.VideoPlayList, null, contentValues);

        database.close();
        sqLiteHelper.close();

        return insert > 0;
    }

    /**
     * 将视频从videoPlayList中删除
     *
     * @param bvid  移除对象
     * @return  移除状态
     */
    public boolean removeVideo(String bvid) {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(context, 1);
        SQLiteDatabase database = sqLiteHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("isDelete", 0);

        int update = database.update(Tables.VideoPlayList, contentValues, "bvid=?", new String[]{bvid});

        database.close();
        sqLiteHelper.close();

        return update > 0;
    }
}
