package com.leon.biuvideo.utils.dataBaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.leon.biuvideo.beans.downloadedBeans.DownloadedDetailMedia;
import com.leon.biuvideo.beans.downloadedBeans.DownloadedRecordsForVideo;
import com.leon.biuvideo.values.Tables;

import java.util.ArrayList;
import java.util.List;

public class DownloadRecordsDatabaseUtils extends SQLiteHelper {
    private final SQLiteHelper sqLiteHelper;
    private final SQLiteDatabase sqLiteDatabase;
    private final String videoRecord = Tables.DownloadRecordsForVideo.value;
    private final String videoDetail = Tables.DownloadDetailsForVideo.value;

    public DownloadRecordsDatabaseUtils(Context context) {
        super(context, 1);

        this.sqLiteHelper = new SQLiteHelper(context, 1);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
    }

    /**
     * 查询所有视频下载记录
     *
     * @return  返回DownloadedRecordsForVideo集合
     */
    public List<DownloadedRecordsForVideo> queryAllVideo() {
        Cursor cursor = sqLiteDatabase.query(videoRecord, null, null, null, null, null, null);

        List<DownloadedRecordsForVideo> downloadedRecordsForVideos = new ArrayList<>();
        while (cursor.moveToNext()) {
            DownloadedRecordsForVideo downloadedRecordsForVideo = new DownloadedRecordsForVideo();
            downloadedRecordsForVideo.cover = cursor.getString(cursor.getColumnIndex("cover"));
            downloadedRecordsForVideo.title = cursor.getString(cursor.getColumnIndex("title"));
            downloadedRecordsForVideo.upName = cursor.getString(cursor.getColumnIndex("upName"));
            downloadedRecordsForVideo.mainId = cursor.getString(cursor.getColumnIndex("mainId"));

            // 根据mainId查询对应的选集个数
            downloadedRecordsForVideo.count = getAnthologyCount(downloadedRecordsForVideo.mainId);

            downloadedRecordsForVideos.add(downloadedRecordsForVideo);
        }

        cursor.close();
        return downloadedRecordsForVideos;
    }

    /**
     * 查询mainId对应的选集个数
     *
     * @param mainId    mainId
     * @return  返回个数
     */
    private int getAnthologyCount(String mainId) {
        Cursor cursor = sqLiteDatabase.query(videoDetail, null, "mainId = ? AND isVideo = ?", new String[]{mainId, "1"}, null, null, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    /**
     * 查询所有已缓存音乐<br/>
     * 该query只获取mainId，不需要获取subId
     *
     * @return  返回DownloadedDetailMedia集合
     */
    public List<DownloadedDetailMedia> queryAllMusic() {
        Cursor cursor = sqLiteDatabase.query(videoDetail, null, "isVideo = ?", new String[]{"0"}, null, null, null);

        List<DownloadedDetailMedia> downloadedDetailMedias = new ArrayList<>();
        while (cursor.moveToNext()) {
            DownloadedDetailMedia downloadedDetailMedia = new DownloadedDetailMedia();
            downloadedDetailMedia.fileName = cursor.getString(cursor.getColumnIndex("fileName"));
            downloadedDetailMedia.cover = cursor.getString(cursor.getColumnIndex("cover"));
            downloadedDetailMedia.title = cursor.getString(cursor.getColumnIndex("title"));
            downloadedDetailMedia.size = cursor.getLong(cursor.getColumnIndex("size"));
            downloadedDetailMedia.mainId = cursor.getString(cursor.getColumnIndex("mainId"));
            downloadedDetailMedia.audioUrl = cursor.getString(cursor.getColumnIndex("audioUrl"));
            downloadedDetailMedia.isComplete = cursor.getInt(cursor.getColumnIndex("isComplete")) == 1;

            downloadedDetailMedias.add(downloadedDetailMedia);
        }

        cursor.close();
        return downloadedDetailMedias;
    }

    /**
     * 根据mainId获取所有选集视频<br/>
     *
     * @param mainId    mainId
     * @return  返回DownloadedDetailMedia集合
     */
    public List<DownloadedDetailMedia> queryAllSubVideo(long mainId) {
        Cursor cursor = sqLiteDatabase.query(videoDetail, null, "mainId = ? AND isVideo = ?", new String[]{String.valueOf(mainId), "1"}, null, null, null);

        List<DownloadedDetailMedia> downloadedDetailMedias = new ArrayList<>();
        while (cursor.moveToNext()) {
            DownloadedDetailMedia downloadedDetailMedia = new DownloadedDetailMedia();
            downloadedDetailMedia.fileName = cursor.getString(cursor.getColumnIndex("fileName"));
            downloadedDetailMedia.cover = cursor.getString(cursor.getColumnIndex("cover"));
            downloadedDetailMedia.title = cursor.getString(cursor.getColumnIndex("title"));
            downloadedDetailMedia.size = cursor.getLong(cursor.getColumnIndex("size"));
            downloadedDetailMedia.mainId = cursor.getString(cursor.getColumnIndex("mainId"));
            downloadedDetailMedia.subId = cursor.getLong(cursor.getColumnIndex("subId"));
            downloadedDetailMedia.videoUrl = cursor.getString(cursor.getColumnIndex("videoUrl"));
            downloadedDetailMedia.audioUrl = cursor.getString(cursor.getColumnIndex("audioUrl"));
            downloadedDetailMedia.isComplete = cursor.getInt(cursor.getColumnIndex("isComplete")) == 1;

            downloadedDetailMedias.add(downloadedDetailMedia);
        }

        cursor.close();
        return downloadedDetailMedias;
    }

    /**
     * 根据fileName设置isComplete字段的值为1
     *
     * @param fileName    mainId
     */
    public void setCompleteState(String fileName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("isComplete", 1);

        sqLiteDatabase.update(videoDetail, contentValues, "fileName = ?", new String[]{fileName});
    }

    /**
     * 添加视频
     *
     * @param downloadedRecordsForVideo downloadedRecordsForVideo对象
     */
    public void addVideo(DownloadedRecordsForVideo downloadedRecordsForVideo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("cover", downloadedRecordsForVideo.cover);
        contentValues.put("title", downloadedRecordsForVideo.title);
        contentValues.put("upName", downloadedRecordsForVideo.upName);
        contentValues.put("mainId", downloadedRecordsForVideo.mainId);

        sqLiteDatabase.insert(videoRecord, null, contentValues);
    }

    /**
     * 添加选集/音乐条目
     *
     * @param downloadedDetailMedia downloadedDetailMedia对象
     */
    public void addSubVideo(DownloadedDetailMedia downloadedDetailMedia) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("fileName", downloadedDetailMedia.fileName);
        contentValues.put("cover", downloadedDetailMedia.cover);
        contentValues.put("title", downloadedDetailMedia.title);
        contentValues.put("size", downloadedDetailMedia.size);
        contentValues.put("mainId", downloadedDetailMedia.mainId);
        contentValues.put("subId", downloadedDetailMedia.subId);
        contentValues.put("videoUrl", downloadedDetailMedia.videoUrl);
        contentValues.put("audioUrl", downloadedDetailMedia.audioUrl);
        contentValues.put("isVideo", downloadedDetailMedia.isVideo ? 1 : 0);

        sqLiteDatabase.insert(videoDetail, null, contentValues);
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