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

public class SimpleThreadPool extends ThreadPoolExecutor {
    private static final String TAG = "SimpleThreadPool";

    // 资源下载线程数
    public static final int DownloadTaskNum = 3;
    public static final String DownloadTask = "download";

    // 数据加载线程数
    public static final int LoadTaskNum = 5;
    public static final String LoadTask = "loading";

    private Map<String, FutureTask<String>> futureTaskMap;

    public SimpleThreadPool(int maxRunningThread, String poolName) {
        super(maxRunningThread, maxRunningThread, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new SimpleThreadFactory(poolName));
        futureTaskMap = new HashMap<>();
    }

    /**
     * 执行任务
     * @param futureTask  任务对象
     * @param tag   任务唯一TAG
     */
    public void submit(FutureTask<String> futureTask , String tag){
        synchronized (this){
            // 执行线程
            if (!futureTaskMap.containsKey(tag)) {
                // 如果池内没有相同的任务则开始执行
                Log.d(TAG, "submit: " + "Task submit TAG:" + tag);
                futureTaskMap.put(tag, futureTask);
                submit(futureTask);
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
    public boolean cancelTask(String tag) {
        return cancelTask(tag, false);
    }

    /**
     * 终止任务
     *
     * @param tag   任务tag
     * @param isContainText 是否为相似的tag
     * @return  终止状态
     */
    public boolean cancelTask(String tag, boolean isContainText) {
        // 中断线程
        synchronized (this) {
            if (isContainText) {
                Log.d(TAG, "cancelTask: " + "Cancel Task TAG:" + tag);
                for (String key : futureTaskMap.keySet()) {
                    if (key.contains(tag)) {
                        remove(futureTaskMap.get(key));
                        FutureTask task = futureTaskMap.remove(key);
                        if (task != null) {
                            task.cancel(true);
                        }

                        Log.d(TAG, "cancelTask: " + "Task Canceled TAG: " + tag);
                        return true;
                    }
                }

                return false;
            } else {
                if (futureTaskMap.containsKey(tag)) {
                    Log.d(TAG, "cancelTask: " + "Task canceled TAG:" + tag);
                    remove(futureTaskMap.get(tag));

                    FutureTask removedTask = futureTaskMap.remove(tag);
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
    }

    /**
     * 终止所有任务
     *
     * @return  终止状态
     */
    public boolean cancelAllTask () {
        Iterator<FutureTask<String>> futureTaskIterator = futureTaskMap.values().iterator();

        int count = 0;
        while (futureTaskIterator.hasNext()) {
            count++;

            FutureTask<String> nextFutureTask = futureTaskIterator.next();
            nextFutureTask.cancel(true);

            remove(nextFutureTask);
        }

        Log.d(TAG, "cancelAllTask: " + count + "Tasks canceled.");

        return count > 0;
    }

    /**
     * 移除任务
     *
     * @param tag   任务tag
     * @return  移除状态
     */
    public boolean remove(String tag) {
        // 移除tag
        if (futureTaskMap.remove(tag) != null) {
            Log.d(TAG, "TAG removed. TAG Count:" + futureTaskMap.size() + "  Pool of " + ((SimpleThreadFactory) getThreadFactory()).getPoolName());
            return true;
        } else {
            Log.d(TAG, "remove: TAG dose not exist.");
            return false;
        }
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
