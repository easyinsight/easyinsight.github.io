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
public class DataSourceThreadPool {
    private ThreadPoolExecutor tpe;
    private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();

    private static DataSourceThreadPool instance;

    public static void initialize() {
        DataSourceThreadPool pool = new DataSourceThreadPool();
        pool.tpe = new ThreadPoolExecutor(5, 5, 5, TimeUnit.MINUTES, pool.queue);
        instance = pool;
    }

    public void addActivity(Runnable runnable) {
        tpe.execute(runnable);
    }

    public void shutdown() {
        System.out.println("Shutting down primary thread pool...");
        try {
            tpe.shutdown();
            tpe.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            // ignore
        }
        System.out.println("Shut down primary thread pool.");
    }

    public static DataSourceThreadPool instance() {
        return instance;
    }
}
