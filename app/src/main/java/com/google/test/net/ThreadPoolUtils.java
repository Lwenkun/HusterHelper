package com.google.test.net;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 15119 on 2015/10/22.
 */
public class ThreadPoolUtils {

    private static final int CORE_POOL_SIZE = 5;

    private static final int MAX_POOL_SIZE = 50;

    private static final int KEEP_ALIVE_TIME = 10000;

    private static ThreadFactory factory = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r);
        }
    };

    private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(10);

    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue, factory);

    public static void execute(Runnable r) {
        threadPool.execute(r);
    }
}
