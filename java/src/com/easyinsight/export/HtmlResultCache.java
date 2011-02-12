package com.easyinsight.export;

import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 2/9/11
 * Time: 11:08 PM
 */
public class HtmlResultCache {

    private static HtmlResultCache instance;

    private Map<Long, Object> locks = new HashMap<Long, Object>();
    private Map<Long, byte[]> results = new HashMap<Long, byte[]>();

    public static HtmlResultCache getInstance() {
        return instance;
    }

    public static void initialize() {
        instance = new HtmlResultCache();
    }

    public byte[] waitForResults(long id) throws InterruptedException {
        final Object lock = new Object();
        locks.put(id, lock);
        synchronized(lock) {
            System.out.println("waiting...");
            lock.wait(60000);
            System.out.println("got notified!");
            return results.remove(id);
        }
    }

    public void addResults(byte[] bytes, long id) {
        final Object lock = locks.get(id);
        if (lock != null) {
            results.put(id, bytes);
            synchronized (lock) {
                lock.notify();
            }
        }
    }
}
