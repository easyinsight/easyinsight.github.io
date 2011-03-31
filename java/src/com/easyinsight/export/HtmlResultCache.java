package com.easyinsight.export;

import com.easyinsight.logging.LogClass;
import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 2/9/11
 * Time: 11:08 PM
 */
public class HtmlResultCache {

    private static HtmlResultCache instance;

    public static HtmlResultCache getInstance() {
        return instance;
    }

    public static void initialize() {
        instance = new HtmlResultCache();
    }

    private JCS resultCache = getCache("htmlcache");

    private JCS getCache(String cacheName) {

        try {
            return JCS.getInstance(cacheName);
        } catch (Exception e) {
            LogClass.error(e);
        }
        return null;
    }

    public byte[] waitForResults(long id) throws InterruptedException {
        long time = System.currentTimeMillis() + 60000;
        try {
            while (System.currentTimeMillis() < time) {
                byte[] results = (byte[]) resultCache.get(id);
                if (results != null) {
                    resultCache.remove(id);
                    return results;
                } else {
                    Thread.sleep(2500);
                }
            }
        } catch (CacheException e) {
            LogClass.error(e);
        }
        return null;
    }

    public void addResults(byte[] bytes, long id) {
        try {
            resultCache.put(id, bytes);
        } catch (Exception e) {
            LogClass.error(e);
        }
    }
}
