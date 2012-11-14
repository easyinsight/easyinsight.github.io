package com.easyinsight.analysis;

import com.easyinsight.logging.LogClass;
import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Jun 21, 2010
 * Time: 4:10:36 PM
 */
public class ReportCache {
    private static ReportCache instance;
    private JCS reportCache = getCache("embeddedReports");

    public static void initialize() {
        instance = new ReportCache();
    }

    public static ReportCache instance() {
        return instance;
    }

    private JCS getCache(String cacheName) {
        try {
            return JCS.getInstance(cacheName);
        } catch (Exception e) {
            LogClass.error(e);
        }
        return null;
    }

    @Nullable
    public EmbeddedResults getResults(long dataSourceID, CacheKey cacheKey) {
        Map<CacheKey, EmbeddedResults> map = (Map<CacheKey, EmbeddedResults>) reportCache.get(dataSourceID);
        if (map != null) {
            return map.get(cacheKey);
        }
        return null;
    }

    public void storeReport(long dataSourceID, CacheKey cacheKey, EmbeddedResults results) throws CacheException {
        Map<CacheKey, EmbeddedResults> map = (Map<CacheKey, EmbeddedResults>) reportCache.get(dataSourceID);
        if (map == null) {
            map = new HashMap<CacheKey, EmbeddedResults>();
        }
        map.put(cacheKey, results);
        reportCache.remove(dataSourceID);
        reportCache.put(cacheKey, results);
    }

    public void flushResults(long dataSourceID) throws CacheException {
        reportCache.remove(dataSourceID);
    }
}
