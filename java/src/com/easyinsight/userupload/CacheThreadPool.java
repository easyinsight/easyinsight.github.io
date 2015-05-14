package com.easyinsight.userupload;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * User: jamesboe
 * Date: 2/11/14
 * Time: 9:21 AM
 */
public class CacheThreadPool {
    private ThreadPoolExecutor tpe;
    private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();

    private static CacheThreadPool instance;

    public static void initialize() {
        CacheThreadPool pool = new CacheThreadPool();
        pool.tpe = new ThreadPoolExecutor(2, 2, 2, TimeUnit.MINUTES, pool.queue);
        instance = pool;
    }

    public void addActivity(Runnable runnable) {
        tpe.execute(runnable);
    }

    public void shutdown() {
        System.out.println("Shutting down recache thread pool...");
        try {
            tpe.shutdown();
            tpe.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            // ignore
        }
        System.out.println("Shut down recache thread pool.");
    }

    public static CacheThreadPool instance() {
        return instance;
    }
}
