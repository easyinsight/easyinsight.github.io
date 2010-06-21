package com.easyinsight.analysis;

import com.easyinsight.logging.LogClass;
import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

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

    public Map<EmbeddedCacheKey, EmbeddedResults> getReports(long dataSourceID) {
        return (Map<EmbeddedCacheKey, EmbeddedResults>) reportCache.get(dataSourceID);
    }

    public void storeResults(long dataSourceID, Map<EmbeddedCacheKey, EmbeddedResults> resultsCache) throws CacheException {
        reportCache.put(dataSourceID, resultsCache);
    }

    public void flushResults(long dataSourceID) throws CacheException {
        reportCache.remove(dataSourceID);
    }
}
