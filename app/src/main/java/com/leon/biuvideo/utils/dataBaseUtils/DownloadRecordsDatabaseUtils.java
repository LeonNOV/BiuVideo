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

/**
 * 处理downloadRecordsForVideo、downloadDetailsForMedia数据的工具类
 */
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
            int anthologyCount = getAnthologyCount(downloadedRecordsForVideo.mainId);
            if (anthologyCount == 0) {
                continue;
            } else {
                downloadedRecordsForVideo.count = anthologyCount;
            }

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
        Cursor cursor = sqLiteDatabase.query(videoDetail, null, "mainId = ? AND isVideo = ? AND downloadState = ? AND isDelete = ?", new String[]{mainId, "1", "2", "0"}, null, null, null);

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
        Cursor cursor = sqLiteDatabase.query(videoDetail, null, "isVideo = ? AND downloadState = ? AND isDelete = ?", new String[]{"0", "2", "0"}, null, null, null);

        List<DownloadedDetailMedia> downloadedDetailMedias = new ArrayList<>();
        while (cursor.moveToNext()) {
            DownloadedDetailMedia downloadedDetailMedia = new DownloadedDetailMedia();
            downloadedDetailMedia.fileName = cursor.getString(cursor.getColumnIndex("fileName"));
            downloadedDetailMedia.cover = cursor.getString(cursor.getColumnIndex("cover"));
            downloadedDetailMedia.title = cursor.getString(cursor.getColumnIndex("title"));
            downloadedDetailMedia.size = cursor.getLong(cursor.getColumnIndex("size"));
            downloadedDetailMedia.mainId = cursor.getString(cursor.getColumnIndex("mainId"));
            downloadedDetailMedia.audioUrl = cursor.getString(cursor.getColumnIndex("audioUrl"));
            downloadedDetailMedia.isVideo = false;
            downloadedDetailMedia.downloadState = cursor.getInt(cursor.getColumnIndex("downloadState"));
            downloadedDetailMedia.resourceMark = cursor.getString(cursor.getColumnIndex("resourceMark"));

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
    public List<DownloadedDetailMedia> queryAllSubVideo(String mainId) {
        Cursor cursor;
        if (mainId == null) {
            cursor = sqLiteDatabase.query(videoDetail, null, "isVideo = ? AND downloadState = ? AND isDelete = ?", new String[]{"1", "2", "0"}, null, null, null);
        } else {
            cursor = sqLiteDatabase.query(videoDetail, null, "mainId = ? AND isVideo = ? AND downloadState = ? AND isDelete = ?", new String[]{mainId, "1", "2", "0"}, null, null, null);
        }

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
            downloadedDetailMedia.qualityId = cursor.getInt(cursor.getColumnIndex("qualityId"));
            downloadedDetailMedia.resourceMark = cursor.getString(cursor.getColumnIndex("resourceMark"));
            downloadedDetailMedia.isVideo = true;
            downloadedDetailMedia.downloadState = cursor.getInt(cursor.getColumnIndex("downloadState"));

            downloadedDetailMedias.add(downloadedDetailMedia);
        }

        cursor.close();
        return downloadedDetailMedias;
    }

    /**
     * 根据fileName获取单个选集视频
     *
     * @param fileName    文件名称
     * @return  返回DownloadedDetailMedia对象
     */
    public DownloadedDetailMedia queryVideoByFileName(String fileName) {
        Cursor cursor = sqLiteDatabase.query(videoDetail, null, "fileName = ? AND isVideo = ? AND downloadState = ? AND isDelete = ?", new String[]{fileName, "1", "2", "0"}, null, null, null);

        if (cursor.getCount() != 0) {
            cursor.moveToNext();
            DownloadedDetailMedia downloadedDetailMedia = new DownloadedDetailMedia();
            downloadedDetailMedia.fileName = cursor.getString(cursor.getColumnIndex("fileName"));
            downloadedDetailMedia.cover = cursor.getString(cursor.getColumnIndex("cover"));
            downloadedDetailMedia.title = cursor.getString(cursor.getColumnIndex("title"));
            downloadedDetailMedia.size = cursor.getLong(cursor.getColumnIndex("size"));
            downloadedDetailMedia.mainId = cursor.getString(cursor.getColumnIndex("mainId"));
            downloadedDetailMedia.subId = cursor.getLong(cursor.getColumnIndex("subId"));
            downloadedDetailMedia.videoUrl = cursor.getString(cursor.getColumnIndex("videoUrl"));
            downloadedDetailMedia.audioUrl = cursor.getString(cursor.getColumnIndex("audioUrl"));
            downloadedDetailMedia.qualityId = cursor.getInt(cursor.getColumnIndex("qualityId"));
            downloadedDetailMedia.resourceMark = cursor.getString(cursor.getColumnIndex("resourceMark"));
            downloadedDetailMedia.downloadState = cursor.getInt(cursor.getColumnIndex("downloadState"));

            return downloadedDetailMedia;
        }

        cursor.close();
        return null;
    }

    /**
     * 根据fileName设置isComplete字段的值为1（正在下载）
     *
     * @param fileName    mainId
     */
    public void setDownloading(String fileName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("downloadState", 1);

        sqLiteDatabase.update(videoDetail, contentValues, "fileName = ?", new String[]{fileName});
    }

    /**
     * 根据fileName设置isComplete字段的值为2（下载完成）
     *
     * @param fileName    mainId
     */
    public void setComplete(String fileName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("downloadState", 2);

        sqLiteDatabase.update(videoDetail, contentValues, "fileName = ?", new String[]{fileName});
    }

    /**
     * 将对应文件从“正在下载”状态的设置为“下载失败”状态
     *
     * @param fileName  文件名称
     */
    public void setFailed(String fileName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("downloadState", 0);

        sqLiteDatabase.update(videoDetail, contentValues, "downloadState = ? AND fileName = ?", new String[]{"1", fileName});
    }

    /**
     * 建议在APP启动时调用该方法<br/>
     * 用于将“正在下载”状态的设置为“下载失败”状态
     */
    public void setFailed() {
        // 将所有为“下载中”状态的设置为“下载失败”状态
        ContentValues contentValues = new ContentValues();
        contentValues.put("downloadState", 0);

        sqLiteDatabase.update(videoDetail, contentValues, "downloadState = ?", new String[]{"1"});
    }

    /**
     * 添加视频（主）
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
     * 添加选集/音乐下载条目
     *
     * @param downloadedDetailMedia downloadedDetailMedia对象
     */
    public void addMediaDetail(DownloadedDetailMedia downloadedDetailMedia) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("fileName", downloadedDetailMedia.fileName);
        contentValues.put("cover", downloadedDetailMedia.cover);
        contentValues.put("title", downloadedDetailMedia.title);
        contentValues.put("size", downloadedDetailMedia.size);
        contentValues.put("mainId", downloadedDetailMedia.mainId);
        contentValues.put("subId", downloadedDetailMedia.subId);
        contentValues.put("videoUrl", downloadedDetailMedia.videoUrl);
        contentValues.put("audioUrl", downloadedDetailMedia.audioUrl);
        contentValues.put("qualityId", downloadedDetailMedia.qualityId);
        contentValues.put("resourceMark", downloadedDetailMedia.resourceMark);
        contentValues.put("isVideo", downloadedDetailMedia.isVideo ? 1 : 0);

        sqLiteDatabase.insert(videoDetail, null, contentValues);
    }

    /**
     * 查询对应视频/音频的对应清晰度是否已下载
     *
     * @param resourceMark  资源标记
     * @return  返回已下载状态
     */
    public boolean queryVideoDownloadState(String resourceMark) {
        Cursor cursor = sqLiteDatabase.query(videoDetail, null, "resourceMark = ? AND downloadState = ?", new String[]{resourceMark, "2"}, null, null, null);
        int count = cursor.getCount();

        cursor.close();
        return count > 0;
    }

    /**
     * 设置对应资源文件状态为已删除状态
     *
     * @param fileName  资源文件名称
     */
    public void setDelete(String fileName, boolean isDelete) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("isDelete", isDelete ? 1 : 0);
        sqLiteDatabase.update(videoDetail, contentValues, "fileName = ?", new String[]{fileName});
    }

    /**
     * 更新本地媒体资源链接
     *
     * @param urls  资源链接
     * @param fileName  文件名称
     */
    public void updateUrl(String[] urls, String fileName) {
        ContentValues contentValues = new ContentValues();
        if (urls.length == 2) {
            contentValues.put("videoUrl", urls[0]);
            contentValues.put("audioUrl", urls[1]);
        } else {
            contentValues.put("videoUrl", urls[0]);
        }

        sqLiteDatabase.update(videoDetail, contentValues, "fileName = ?", new String[]{fileName});
    }

    /**
     * 获取所有下载失败的媒体资源
     *
     * @return  返回所有下载失败的媒体资源
     */
    public List<DownloadedDetailMedia> queryDownloadFailMedia() {
        Cursor cursor = sqLiteDatabase.query(videoDetail, null, "downloadState = ?", new String[]{"0"}, null, null, null);

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
            downloadedDetailMedia.qualityId = cursor.getInt(cursor.getColumnIndex("qualityId"));
            downloadedDetailMedia.isVideo = cursor.getInt(cursor.getColumnIndex("isVideo")) == 1;
            downloadedDetailMedia.downloadState = cursor.getInt(cursor.getColumnIndex("downloadState"));

            downloadedDetailMedias.add(downloadedDetailMedia);
        }

        cursor.close();
        return downloadedDetailMedias;
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