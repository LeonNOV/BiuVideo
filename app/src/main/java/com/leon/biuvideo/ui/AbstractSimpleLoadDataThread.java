package com.leon.biuvideo.ui;

import com.leon.biuvideo.utils.SimpleThreadPool;

import java.util.concurrent.Callable;

/**
 * @Author Leon
 * @Time 2021/3/6
 * @Desc 一个简单的加载数据抽象类<br />只需重写该类中的load()方法即可
 */
public abstract class AbstractSimpleLoadDataThread implements Callable<String> {

    /**
     * <strong>需要重写该方法</strong>
     * <br/>
     * 在该方法中编写加载数据的代码
     */
    public abstract void load();

    @Override
    public String call() {
        load();

        return null;
    }

    public SimpleThreadPool getSimpleThreadPool () {
        return new SimpleThreadPool(SimpleThreadPool.LoadTaskNum, SimpleThreadPool.LoadTask);
    }
}
