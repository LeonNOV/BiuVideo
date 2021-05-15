package com.leon.biuvideo.greendao.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DOWNLOAD_HISTORY".
*/
public class DownloadHistoryDao extends AbstractDao<DownloadHistory, Long> {

    public static final String TABLENAME = "DOWNLOAD_HISTORY";

    /**
     * Properties of entity DownloadHistory.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ResType = new Property(1, int.class, "resType", false, "RES_TYPE");
        public final static Property TaskId = new Property(2, long.class, "taskId", false, "TASK_ID");
        public final static Property IsCompleted = new Property(3, boolean.class, "isCompleted", false, "IS_COMPLETED");
        public final static Property IsFailed = new Property(4, boolean.class, "isFailed", false, "IS_FAILED");
        public final static Property ResStreamUrl = new Property(5, String.class, "resStreamUrl", false, "RES_STREAM_URL");
        public final static Property LevelOneId = new Property(6, String.class, "levelOneId", false, "LEVEL_ONE_ID");
        public final static Property LevelTwoId = new Property(7, String.class, "levelTwoId", false, "LEVEL_TWO_ID");
        public final static Property MainTitle = new Property(8, String.class, "mainTitle", false, "MAIN_TITLE");
        public final static Property SubTitle = new Property(9, String.class, "subTitle", false, "SUB_TITLE");
        public final static Property CoverUrl = new Property(10, String.class, "coverUrl", false, "COVER_URL");
        public final static Property SavePath = new Property(11, String.class, "savePath", false, "SAVE_PATH");
        public final static Property FileSize = new Property(12, long.class, "fileSize", false, "FILE_SIZE");
        public final static Property DanmakuFilePath = new Property(13, String.class, "danmakuFilePath", false, "DANMAKU_FILE_PATH");
    }


    public DownloadHistoryDao(DaoConfig config) {
        super(config);
    }
    
    public DownloadHistoryDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DOWNLOAD_HISTORY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"RES_TYPE\" INTEGER NOT NULL ," + // 1: resType
                "\"TASK_ID\" INTEGER NOT NULL ," + // 2: taskId
                "\"IS_COMPLETED\" INTEGER NOT NULL ," + // 3: isCompleted
                "\"IS_FAILED\" INTEGER NOT NULL ," + // 4: isFailed
                "\"RES_STREAM_URL\" TEXT," + // 5: resStreamUrl
                "\"LEVEL_ONE_ID\" TEXT," + // 6: levelOneId
                "\"LEVEL_TWO_ID\" TEXT," + // 7: levelTwoId
                "\"MAIN_TITLE\" TEXT," + // 8: mainTitle
                "\"SUB_TITLE\" TEXT," + // 9: subTitle
                "\"COVER_URL\" TEXT," + // 10: coverUrl
                "\"SAVE_PATH\" TEXT," + // 11: savePath
                "\"FILE_SIZE\" INTEGER NOT NULL ," + // 12: fileSize
                "\"DANMAKU_FILE_PATH\" TEXT);"); // 13: danmakuFilePath
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DOWNLOAD_HISTORY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, DownloadHistory entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getResType());
        stmt.bindLong(3, entity.getTaskId());
        stmt.bindLong(4, entity.getIsCompleted() ? 1L: 0L);
        stmt.bindLong(5, entity.getIsFailed() ? 1L: 0L);
 
        String resStreamUrl = entity.getResStreamUrl();
        if (resStreamUrl != null) {
            stmt.bindString(6, resStreamUrl);
        }
 
        String levelOneId = entity.getLevelOneId();
        if (levelOneId != null) {
            stmt.bindString(7, levelOneId);
        }
 
        String levelTwoId = entity.getLevelTwoId();
        if (levelTwoId != null) {
            stmt.bindString(8, levelTwoId);
        }
 
        String mainTitle = entity.getMainTitle();
        if (mainTitle != null) {
            stmt.bindString(9, mainTitle);
        }
 
        String subTitle = entity.getSubTitle();
        if (subTitle != null) {
            stmt.bindString(10, subTitle);
        }
 
        String coverUrl = entity.getCoverUrl();
        if (coverUrl != null) {
            stmt.bindString(11, coverUrl);
        }
 
        String savePath = entity.getSavePath();
        if (savePath != null) {
            stmt.bindString(12, savePath);
        }
        stmt.bindLong(13, entity.getFileSize());
 
        String danmakuFilePath = entity.getDanmakuFilePath();
        if (danmakuFilePath != null) {
            stmt.bindString(14, danmakuFilePath);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, DownloadHistory entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getResType());
        stmt.bindLong(3, entity.getTaskId());
        stmt.bindLong(4, entity.getIsCompleted() ? 1L: 0L);
        stmt.bindLong(5, entity.getIsFailed() ? 1L: 0L);
 
        String resStreamUrl = entity.getResStreamUrl();
        if (resStreamUrl != null) {
            stmt.bindString(6, resStreamUrl);
        }
 
        String levelOneId = entity.getLevelOneId();
        if (levelOneId != null) {
            stmt.bindString(7, levelOneId);
        }
 
        String levelTwoId = entity.getLevelTwoId();
        if (levelTwoId != null) {
            stmt.bindString(8, levelTwoId);
        }
 
        String mainTitle = entity.getMainTitle();
        if (mainTitle != null) {
            stmt.bindString(9, mainTitle);
        }
 
        String subTitle = entity.getSubTitle();
        if (subTitle != null) {
            stmt.bindString(10, subTitle);
        }
 
        String coverUrl = entity.getCoverUrl();
        if (coverUrl != null) {
            stmt.bindString(11, coverUrl);
        }
 
        String savePath = entity.getSavePath();
        if (savePath != null) {
            stmt.bindString(12, savePath);
        }
        stmt.bindLong(13, entity.getFileSize());
 
        String danmakuFilePath = entity.getDanmakuFilePath();
        if (danmakuFilePath != null) {
            stmt.bindString(14, danmakuFilePath);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public DownloadHistory readEntity(Cursor cursor, int offset) {
        DownloadHistory entity = new DownloadHistory( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // resType
            cursor.getLong(offset + 2), // taskId
            cursor.getShort(offset + 3) != 0, // isCompleted
            cursor.getShort(offset + 4) != 0, // isFailed
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // resStreamUrl
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // levelOneId
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // levelTwoId
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // mainTitle
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // subTitle
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // coverUrl
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // savePath
            cursor.getLong(offset + 12), // fileSize
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13) // danmakuFilePath
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, DownloadHistory entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setResType(cursor.getInt(offset + 1));
        entity.setTaskId(cursor.getLong(offset + 2));
        entity.setIsCompleted(cursor.getShort(offset + 3) != 0);
        entity.setIsFailed(cursor.getShort(offset + 4) != 0);
        entity.setResStreamUrl(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setLevelOneId(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setLevelTwoId(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setMainTitle(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setSubTitle(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setCoverUrl(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setSavePath(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setFileSize(cursor.getLong(offset + 12));
        entity.setDanmakuFilePath(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(DownloadHistory entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(DownloadHistory entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(DownloadHistory entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}