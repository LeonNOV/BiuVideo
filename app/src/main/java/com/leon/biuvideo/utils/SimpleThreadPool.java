package com.leon.biuvideo.utils;

import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author Leon
 * @Time 2021/1/1
 * @Desc 一个简单的线程池，加载截面数据或下载资源时可使用该类
 */
public class SimpleThreadPool {
    private static final String TAG = "SimpleThreadPool";

    /**
     * 资源下载线程数
     */
    private static final int DOWNLOAD_TASK_NUM = 3;
    private static final String DOWNLOAD_TASK = "download";

    private static final Map<String, FutureTask<String>> FUTURE_TASK_MAP = new HashMap<>();

    private static final ThreadPoolExecutor DOWNLOAD_THREAD_POOL;

    static {
        DOWNLOAD_THREAD_POOL = new ThreadPoolExecutor(DOWNLOAD_TASK_NUM, DOWNLOAD_TASK_NUM, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new SimpleThreadFactory(DOWNLOAD_TASK));
    }

    /**
     * 执行任务
     * @param futureTask  任务对象
     * @param tag   任务唯一TAG
     */
    public static void submit(FutureTask<String> futureTask , String tag){
        synchronized (SimpleThreadPool.class){
            // 执行线程
            if (!FUTURE_TASK_MAP.containsKey(tag)) {
                // 如果池内没有相同的任务则开始执行
                Log.d(TAG, "submit: " + "Task submit TAG:" + tag);
                FUTURE_TASK_MAP.put(tag, futureTask);
                DOWNLOAD_THREAD_POOL.submit(futureTask);
            } else {
                Log.d(TAG, "Same task TAG. Skipped.");
            }
        }
    }

    /**
     * 终止任务
     *
     * @param tag   任务tag
     * @return  终止状态
     */
    private static boolean cancelTask(String tag) {
        // 中断线程
        synchronized (SimpleThreadPool.class) {
            if (FUTURE_TASK_MAP.containsKey(tag)) {
                Log.d(TAG, "cancelTask: " + "Task canceled TAG:" + tag);
                DOWNLOAD_THREAD_POOL.remove(FUTURE_TASK_MAP.get(tag));

                FutureTask<String> removedTask = FUTURE_TASK_MAP.remove(tag);
                if (removedTask != null) {
                    removedTask.cancel(true);
                }

                return true;
            } else {
                Log.d(TAG, "cancelTask: " + tag + " dose not exist");
                return false;
            }
        }
    }

    /**
     * 终止所有任务
     *
     * @return  终止状态
     */
    public static boolean cancelAllTask () {
        Iterator<FutureTask<String>> futureTaskIterator = FUTURE_TASK_MAP.values().iterator();

        int count = 0;
        while (futureTaskIterator.hasNext()) {
            count++;

            FutureTask<String> nextFutureTask = futureTaskIterator.next();
            nextFutureTask.cancel(true);

            DOWNLOAD_THREAD_POOL.remove(nextFutureTask);
        }

        Log.d(TAG, "cancelAllTask: " + count + "Tasks canceled.");

        return count > 0;
    }

    static class SimpleThreadFactory implements ThreadFactory {
        private final String name;

        public SimpleThreadFactory(String name) {
            this.name = name;
        }

        public String getPoolName() {
            return name;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new SimpleThread(r, name);
        }
    }

    static class SimpleThread extends Thread {
        public SimpleThread(@Nullable Runnable target, @NonNull String name) {
            super(target, name);
            setName(name);
        }

        @Override
        public void run() {
            super.run();
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        }
    }
}
