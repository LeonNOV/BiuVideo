package com.leon.biuvideo.utils.dataBaseUtils;

import android.content.Context;

import com.leon.biuvideo.values.Tables;

/**
 * 创建数据库连接时使用该类创建
 */
public class SQLiteHelperFactory {
    private final Context context;
    private final Tables table;

    public SQLiteHelperFactory(Context context, Tables table) {
        this.context = context;
        this.table = table;
    }

    /**
     * 根据table获取对应的对象
     *
     * @return  返回对应数据库所使用的工具类
     */
    public SQLiteHelper getInstance() {

        SQLiteHelper sqLiteHelper;

        //根据table来创建对应的对象
        switch (table) {
            case FavoriteUp:
                sqLiteHelper = new FavoriteDatabaseUtils(context);
                break;
            case Article:
                sqLiteHelper = new ArticleDatabaseUtils(context);
                break;
            case DownloadDetailsForVideo:
            case DownloadRecordsForVideo:
                sqLiteHelper = new DownloadRecordsDatabaseUtils(context);
                break;
            case LocalOrders:
            case LocalVideoFolders:
                sqLiteHelper = new LocalOrdersDatabaseUtils(context);
                break;
            default:
                sqLiteHelper = new SQLiteHelper(context, 1);
                break;
        }

        return sqLiteHelper;
    }
}
