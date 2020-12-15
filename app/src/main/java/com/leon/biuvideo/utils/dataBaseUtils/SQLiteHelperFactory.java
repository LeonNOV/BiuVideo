package com.leon.biuvideo.utils.dataBaseUtils;

import android.content.Context;

import com.leon.biuvideo.utils.SQLiteHelper;

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
            case VideoPlayList:
                sqLiteHelper = new VideoListDatabaseUtils(context);
                break;
            case MusicPlayList:
                sqLiteHelper = new MusicListDatabaseUtils(context);
                break;
            case Article:
                sqLiteHelper = new ArticleDatabaseUtils(context);
                break;
            default:
                sqLiteHelper = new SQLiteHelper(context, 1);
                break;
        }

        return sqLiteHelper;
    }
}
