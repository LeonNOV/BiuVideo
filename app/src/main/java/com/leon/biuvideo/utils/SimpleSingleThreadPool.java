package com.leon.biuvideo.utils;

import androidx.annotation.NonNull;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author Leon
 * @Time 2021/3/6
 * @Desc 一个贼简单的执行单线程池
 */
public class SimpleSingleThreadPool {
    private static final ThreadPoolExecutor SINGLE_THREAD_POOL_EXECUTOR;

    // 保证该类只有一个对象
    static {
        SINGLE_THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "singleThread");
            }
        });
    }

    /**
     * 执行单个线程
     *
     * @param runnable  要执行的任务
     */
    public static void executor(@NonNull Runnable runnable) {
        SINGLE_THREAD_POOL_EXECUTOR.execute(runnable);
    }
}
