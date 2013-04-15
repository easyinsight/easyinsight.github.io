package com.easyinsight.analysis;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
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
    private JCS addonReportCache = getCache("addonReports");

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
    public EmbeddedResults getResults(long dataSourceID, CacheKey cacheKey, int cacheTime) {
        Map<CacheKey, EmbeddedResults> map = (Map<CacheKey, EmbeddedResults>) reportCache.get(dataSourceID);
        if (map != null) {
            EmbeddedResults results = map.get(cacheKey);
            if (results != null) {
                System.out.println("got time = " + new Date(results.getTime()));
                if (cacheTime > 0 && System.currentTimeMillis() > (results.getTime() + (cacheTime * 1000 * 60))) {
                    try {
                        System.out.println("expired...");
                        reportCache.remove(dataSourceID);
                    } catch (CacheException e) {
                    }
                    return null;
                }
            }
            return results;
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
        reportCache.put(dataSourceID, map);
    }

    @Nullable
    public DataSet getAddonResults(long dataSourceID, CacheKey cacheKey, int cacheTime) {
        Map<CacheKey, DataSet> map = (Map<CacheKey, DataSet>) reportCache.get(dataSourceID);
        if (map != null) {
            DataSet results = map.get(cacheKey);
            if (results != null) {
                if (cacheTime > 0 && System.currentTimeMillis() > (results.getTime() + (cacheTime * 1000 * 60))) {
                    try {
                        addonReportCache.remove(dataSourceID);
                    } catch (CacheException e) {
                    }
                    return null;
                }
            }
            return results;
        }
        return null;
    }

    public void storeAddonReport(long dataSourceID, CacheKey cacheKey, DataSet results) throws CacheException {
        Map<CacheKey, DataSet> map = (Map<CacheKey, DataSet>) reportCache.get(dataSourceID);
        if (map == null) {
            map = new HashMap<CacheKey, DataSet>();

        }
        map.put(cacheKey, results);
        addonReportCache.remove(dataSourceID);
        addonReportCache.put(dataSourceID, map);
    }

    public void flushResults(long dataSourceID) throws CacheException {
        reportCache.remove(dataSourceID);
        addonReportCache.remove(dataSourceID);
    }
}
