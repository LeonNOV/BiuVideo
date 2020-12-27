package com.leon.biuvideo.utils.dataBaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.leon.biuvideo.beans.articleBeans.Article;
import com.leon.biuvideo.values.Tables;

import java.util.ArrayList;
import java.util.List;

public class ArticleDatabaseUtils extends SQLiteHelper {
    private final SQLiteHelper sqLiteHelper;
    private final SQLiteDatabase sqLiteDatabase;
    private final String dbName = Tables.Article.value;

    public ArticleDatabaseUtils(Context context) {
        super(context, 1);

        this.sqLiteHelper = new SQLiteHelper(context, 1);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
    }

    /**
     * 移除对应ID的数据
     *
     * @param articleId
     * @return
     */
    public boolean removeArticle(long articleId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("isDelete", 1);

        int update = sqLiteDatabase.update(dbName, contentValues, "articleId = ?", new String[]{String.valueOf(articleId)});

        return update > 0;
    }

    /**
     * 查询对应ID的Article的存在状态
     *
     * @param articleId
     * @return
     */
    public boolean queryArticleState(long articleId) {
        Cursor cursor = sqLiteDatabase.query(dbName, new String[]{"isDelete"}, "articleId = ? AND isDelete = ?", new String[]{String.valueOf(articleId), "0"}, null, null, null);

        int count = cursor.getCount();

        cursor.close();
        return count > 0;
    }

    /**
     * 查询所有未删除的Article
     *
     * @return  返回Article集合
     */
    public List<Article> queryArticles() {
        Cursor cursor = sqLiteDatabase.query(dbName, null, "isDelete = ?", new String [] {"0"}, null, null, "id DESC");

        List<Article> articles = new ArrayList<>();
        while (cursor.moveToNext()) {
            Article article = new Article();

            article.mid = cursor.getLong(cursor.getColumnIndex("mid"));
            article.face = cursor.getString(cursor.getColumnIndex("face"));
            article.title = cursor.getString(cursor.getColumnIndex("title"));
            article.summary = cursor.getString(cursor.getColumnIndex("summary"));
            article.coverUrl = cursor.getString(cursor.getColumnIndex("coverUrl"));
            article.articleId = cursor.getLong(cursor.getColumnIndex("articleId"));
            article.author = cursor.getString(cursor.getColumnIndex("author"));
            article.category = cursor.getString(cursor.getColumnIndex("category"));
            article.ctime = cursor.getLong(cursor.getColumnIndex("ctime"));
            article.view = cursor.getInt(cursor.getColumnIndex("view"));
            article.like = cursor.getInt(cursor.getColumnIndex("like"));
            article.reply = cursor.getInt(cursor.getColumnIndex("reply"));

            articles.add(article);
        }

        cursor.close();
        return articles;
    }

    /**
     * 添加Article
     *
     * @param article   article对象
     * @return  返回插入状态
     */
    public boolean addArticle(Article article) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("mid", article.mid);
        contentValues.put("face", article.face);
        contentValues.put("title", article.title);
        contentValues.put("summary", article.summary);
        contentValues.put("coverUrl", article.coverUrl);
        contentValues.put("articleId", article.articleId);
        contentValues.put("author", article.author);
        contentValues.put("category", article.category);
        contentValues.put("ctime", article.ctime);
        contentValues.put("view", article.view);
        contentValues.put("like", article.like);
        contentValues.put("reply", article.reply);

        long insert = sqLiteDatabase.insert(dbName, null, contentValues);

        return insert > 0;
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
