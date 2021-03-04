package com.leon.biuvideo.utils;

import androidx.annotation.NonNull;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 一个贼简单的执行单线程池
 */
public class SimpleThread {
    private static final ThreadPoolExecutor threadPoolExecutor;

    // 保证该类只有一个对象
    static {
        threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(3));
    }

    /**
     * 执行单个线程
     *
     * @param runnable  要执行的任务
     */
    public static void executor(@NonNull Runnable runnable) {
        threadPoolExecutor.execute(runnable);
    }
}
