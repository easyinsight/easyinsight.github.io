package com.easyinsight.cache;

import org.apache.jcs.JCS;
import com.easyinsight.logging.LogClass;

/**
 * User: James Boe
 * Date: Jun 28, 2009
 * Time: 11:12:36 AM
 */
public class Cache {

    public static final String EMBEDDED_REPORTS = "embeddedReports";
    public static final String DATA_SOURCES = "dataSources";

    public static JCS getCache(String cacheName) {
        try {
            return JCS.getInstance(cacheName);
        } catch (Exception e) {
            LogClass.error(e);
        }
        return null;
    }
}
