package com.leon.biuvideo.utils.dataBaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.leon.biuvideo.beans.orderBeans.LocalOrder;
import com.leon.biuvideo.beans.orderBeans.LocalVideoFolder;
import com.leon.biuvideo.utils.InitValueUtils;
import com.leon.biuvideo.values.LocalOrderType;
import com.leon.biuvideo.values.Tables;

import java.util.ArrayList;
import java.util.List;

public class LocalOrdersDatabaseUtils extends SQLiteHelper {
    private final Context context;
    private final SQLiteHelper sqLiteHelper;
    private final SQLiteDatabase sqLiteDatabase;

    private final String LocalVideoFolders = Tables.LocalVideoFolders.value;
    private final String LocalOrders = Tables.LocalOrders.value;

    public LocalOrdersDatabaseUtils(@Nullable Context context) {
        super(context, 1);

        this.context = context;
        this.sqLiteHelper = new SQLiteHelper(context, 1);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
    }

    /**
     * 查询所有视频收藏文件
     *
     * @return  返回所有视频收藏文件夹
     */
    public List<LocalVideoFolder> queryAllLocalVideoFolder () {
        Cursor cursor = sqLiteDatabase.query(LocalVideoFolders, null, "isDelete = ?", new String[]{"0"}, null, null, null);

        if (cursor.getCount() > 0) {
            List<LocalVideoFolder> localVideoFolderList = new ArrayList<>();
            while (cursor.moveToNext()) {
                LocalVideoFolder localVideoFolder = new LocalVideoFolder();
                localVideoFolder.id = cursor.getInt(cursor.getColumnIndex("id"));
                localVideoFolder.folderName = cursor.getString(cursor.getColumnIndex("folderName"));

                Cursor innerCursor = sqLiteDatabase.query(LocalOrders, new String[]{"id"}, "isDelete = ? AND folderName = ? AND orderType = ?", new String[]{"0", localVideoFolder.folderName, String.valueOf(LocalOrderType.VIDEO.value)}, null, null, null);
                localVideoFolder.videoCount = innerCursor.getCount();
                innerCursor.close();

                localVideoFolder.creator = cursor.getString(cursor.getColumnIndex("creator"));
                localVideoFolder.createTime = cursor.getLong(cursor.getColumnIndex("createTime"));

                localVideoFolderList.add(localVideoFolder);
            }

            cursor.close();
            return localVideoFolderList;
        }
        return null;
    }

    /**
     * 根据folderName查询localOrder
     *
     * @param folderName    收藏文件夹名称
     * @return  返回LocalOrder集合
     */
    public List<LocalOrder> queryLocalOrder (String folderName) {
        Cursor cursor = sqLiteDatabase.query(LocalOrders, null, "isDelete = ? AND folderName = ?", new String[]{"0", folderName}, null, null, null);
        return getLocalOrders(cursor);
    }

    /**
     * 查询单个localOrder是否存在
     */
    public boolean queryLocalOrder (String mainId, String subId, LocalOrderType localOrderType) {
        Cursor cursor;
        if (subId == null) {
            cursor = sqLiteDatabase.query(LocalOrders, null, "isDelete = ? AND mainId = ? AND adder = ? AND orderType = ?", new String[]{"0", mainId, String.valueOf(InitValueUtils.getUID(context)), String.valueOf(localOrderType.value)}, null, null, null);
        } else {
            cursor = sqLiteDatabase.query(LocalOrders, null, "isDelete = ? AND mainId = ? AND subId = ? AND adder = ? AND orderType = ?", new String[]{"0", mainId, subId, String.valueOf(InitValueUtils.getUID(context)), String.valueOf(localOrderType.value)}, null, null, null);
        }
        int count = cursor.getCount();

        cursor.close();
        return count > 0;
    }

    /**
     * 根据LocalOrderType查询LocalOrder
     *
     * @param localOrderType    localOrderType
     * @return  返回LocalOrder集合
     */
    public List<LocalOrder> queryLocalOrder (LocalOrderType localOrderType) {
        Cursor cursor = sqLiteDatabase.query(LocalOrders, null, "isDelete = ? AND orderType = ?", new String[]{"0", String.valueOf(localOrderType.value)}, null, null, null);
        return getLocalOrders(cursor);
    }

    @Nullable
    private List<LocalOrder> getLocalOrders(Cursor cursor) {
        if (cursor.getCount() > 0) {
            List<LocalOrder> localOrderList = new ArrayList<>();
            while (cursor.moveToNext()) {
                LocalOrder localOrder = new LocalOrder();
                localOrder.id = cursor.getInt(cursor.getColumnIndex("id"));
                localOrder.title = cursor.getString(cursor.getColumnIndex("title"));
                localOrder.desc = cursor.getString(cursor.getColumnIndex("desc"));
                localOrder.area = cursor.getString(cursor.getColumnIndex("area"));
                localOrder.progress = cursor.getString(cursor.getColumnIndex("progress"));
                localOrder.duration = cursor.getInt(cursor.getColumnIndex("duration"));
                localOrder.count = cursor.getInt(cursor.getColumnIndex("count"));
                localOrder.mainId = cursor.getString(cursor.getColumnIndex("mainId"));
                localOrder.subId = cursor.getString(cursor.getColumnIndex("subId"));
                localOrder.orderType = LocalOrderType.getType(cursor.getInt(cursor.getColumnIndex("orderType")));
                localOrder.adder = cursor.getLong(cursor.getColumnIndex("adder"));
                localOrder.addTime = cursor.getLong(cursor.getColumnIndex("addTime"));
                localOrder.folderName = cursor.getString(cursor.getColumnIndex("folderName"));

                localOrderList.add(localOrder);
            }

            cursor.close();
            return localOrderList;
        }
        return null;
    }

    /**
     * 添加视频收藏文件夹
     *
     * @param localVideoFolder    localVideoFolder
     * @return  true：添加成功， false：有相同名称
     */
    public boolean addLocalVideoFolder(LocalVideoFolder localVideoFolder) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("folderName", localVideoFolder.folderName);
        contentValues.put("creator", localVideoFolder.creator);
        contentValues.put("createTime", localVideoFolder.createTime);

        long insert = sqLiteDatabase.insert(LocalVideoFolders, null, contentValues);

        return insert > 0;
    }

    /**
     * 添加收藏
     *
     * @param localOrder    localOrder
     * @return  true：添加成功
     */
    public boolean addLocalOrder(LocalOrder localOrder) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", localOrder.title);
        contentValues.put("desc", localOrder.desc);
        contentValues.put("area", localOrder.area);
        contentValues.put("progress", localOrder.progress);
        contentValues.put("duration", localOrder.duration);
        contentValues.put("count", localOrder.count);
        contentValues.put("mainId", localOrder.mainId);
        contentValues.put("subId", localOrder.subId);
        contentValues.put("orderType", localOrder.orderType.value);
        contentValues.put("adder", InitValueUtils.getUID(context));
        contentValues.put("addTime", localOrder.addTime);
        contentValues.put("folderName", localOrder.folderName);

        long insert = sqLiteDatabase.insert(LocalOrders, null, contentValues);

        return insert > 0;
    }

    /**
     * 删除订阅数据
     *
     * @param title 标题
     * @param mainId    mainId
     * @param subId subId
     */
    public boolean deleteLocalOrder(String title, String mainId, String subId, LocalOrderType localOrderType) {
        int deleteState;
        if (subId == null) {
            deleteState = sqLiteDatabase.delete(LocalOrders, "title = ? AND mainId = ? AND adder = ? AND orderType = ?", new String[]{title, mainId, String.valueOf(InitValueUtils.getUID(context)), String.valueOf(localOrderType.value)});
        } else {
            deleteState = sqLiteDatabase.delete(LocalOrders, "title = ? AND mainId = ? AND subId = ? AND adder = ? AND orderType = ?", new String[]{title, mainId, subId, String.valueOf(InitValueUtils.getUID(context)), String.valueOf(localOrderType.value)});
        }

        return deleteState > 0;
    }

    /**
     * 删除视频收藏文件夹
     *
     * @param folderName    收藏文件夹名称
     * @param creator   创建者
     */
    public void deleteLocalVideoFolder(String folderName, String creator) {
        sqLiteDatabase.delete(LocalVideoFolders, "folderName = ? AND creator = ?", new String[]{folderName, creator});
    }

    /**
     * 判断默认用户是否存在收藏数据
     *
     * @return  true：有数据；false：无数据
     */
    public boolean checkDefaultLocalData() {
        Cursor localVideoFolderCursor = sqLiteDatabase.query(LocalVideoFolders, null, null, null, null, null, null);
        int localVideoFolderCount = localVideoFolderCursor.getCount();


        Cursor localOrderCursor = sqLiteDatabase.query(LocalOrders, null, null, null, null, null, null);
        int localOrderCount = localOrderCursor.getCount();

        localOrderCursor.close();
        localVideoFolderCursor.close();

        return (localVideoFolderCount + localOrderCount) > 0;
    }

    /**
     * 将默认用户数据合并到当前已登录用户
     */
    public void mergeCurrentUserData() {
        ContentValues videoFolderContentValues = new ContentValues();
        videoFolderContentValues.put("creator", InitValueUtils.getUID(context));
        int localVideoFolderUpdate = sqLiteDatabase.update(LocalVideoFolders, videoFolderContentValues, "creator = ?", new String[]{"0"});

        ContentValues orderContentValues = new ContentValues();
        orderContentValues.put("adder", InitValueUtils.getUID(context));
        int localOrderUpdate = sqLiteDatabase.update(LocalOrders, orderContentValues, "adder = ?", new String[]{"0"});

        Toast.makeText(context, "合并了" + localVideoFolderUpdate + "个收藏夹，" + localOrderUpdate + "个收藏数据，再次打开本地订阅页面即可查看", Toast.LENGTH_SHORT).show();
    }

    @Override
    public synchronized void close() {
        if (sqLiteHelper != null) {
            sqLiteDatabase.close();
            sqLiteHelper.close();
        }
    }
}
