package com.leon.biuvideo.greendao.dao;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

/**
 * @Author Leon
 * @Time 2021/4/1
 * @Desc CRUD基础类
 */
public class DaoBaseUtils<T> {
    private final DaoSession daoSession;
    private final Class<T> entityClass;
    private final AbstractDao<T, Long> entityDao;

    public DaoBaseUtils(Class<T> entityClass, AbstractDao<T, Long> entityDao, DaoSession daoSession) {
        this.daoSession = daoSession;
        this.entityClass = entityClass;
        this.entityDao = entityDao;
    }

    /**
     * 插入单条数据
     *
     * @param entity    数据
     * @return  插入状态
     */
    public boolean insert(T entity) {
        return entityDao.insert(entity) != -1;
    }

    /**
     * 插入多条数据
     *
     * @param entityList    数据集合
     * @return  插入状态
     */
    public boolean insertMultiple (final List<T> entityList) {
        try {
            daoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T entity : entityList) {
                        daoSession.insertOrReplace(entity);
                    }
                }
            });

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 修改数据
     *
     * @param entity    新数据
     * @return  修改状态
     */
    public boolean update (T entity) {
        try {
            daoSession.update(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 删除数据
     *
     * @param entity    被删除数据
     * @return  删除状态
     */
    public boolean delete (T entity) {
        try {
            daoSession.delete(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 删除所有数据
     *
     * @return  删除状态
     */
    public boolean deleteAll () {
        try {
            daoSession.deleteAll(entityClass);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 查询所有数据
     *
     * @return  查询到的结果
     */
    public List<T> queryAll () {
        return daoSession.loadAll(entityClass);
    }

    /**
     * 根据ID进行查询
     *
     * @param key   ID/Key
     * @return  查询到的数据
     */
    public T queryById (long key) {
        return daoSession.load(entityClass, key);
    }

    /**
     * 使用native sql进行查询
     *
     * @param sql   SQL语句
     * @param conditions    条件
     * @return  查询到的结果
     */
    public List<T> queryByNativeSql (String sql, String[] conditions) {
        return daoSession.queryRaw(entityClass, sql, conditions);
    }

    /**
     * 使用queryBuilder进行查询
     */
    public List<T> queryByQueryBuilder (WhereCondition condition, WhereCondition ... conditionsMore) {
        QueryBuilder<T> queryBuilder = daoSession.queryBuilder(entityClass);
        return queryBuilder.where(condition, conditionsMore).list();
    }

    /**
     * 是否存在符合条件的条目
     */
    public boolean isExists (WhereCondition condition, WhereCondition ... conditionsMore) {
        return count(condition, conditionsMore) > 0;
    }

    /**
     * 获取符合条件的条目数
     */
    public Long count (WhereCondition condition, WhereCondition ... conditionsMore) {
        QueryBuilder<T> queryBuilder = daoSession.queryBuilder(entityClass);
        return queryBuilder.where(condition, conditionsMore).count();
    }
}
