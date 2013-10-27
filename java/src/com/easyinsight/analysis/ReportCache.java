package com.easyinsight.analysis;

import com.easyinsight.cache.MemCachedManager;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User: jamesboe
 * Date: Jun 21, 2010
 * Time: 4:10:36 PM
 */
public class ReportCache {
    private static ReportCache instance;

    public static void initialize() {
        instance = new ReportCache();
    }

    public static ReportCache instance() {
        return instance;
    }

    @Nullable
    public EmbeddedResults getResults(long dataSourceID, CacheKey cacheKey, int cacheTime) {
        return (EmbeddedResults) MemCachedManager.get("reportResults" + cacheKey.toString());
    }

    public void storeReport(long dataSourceID, CacheKey cacheKey, EmbeddedResults results, int cacheTime) {
        String cacheKeyString = cacheKey.toString();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO REPORT_CACHE (cache_key, data_source_id) VALUES (?, ?)");
            ps.setString(1, cacheKeyString);
            ps.setLong(2, dataSourceID);
            ps.execute();
            ps.close();
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
        MemCachedManager.add(cacheKeyString, cacheTime * 60, results);
    }

    @Nullable
    public DataSet getAddonResults(long dataSourceID, CacheKey cacheKey, int cacheTime) {
        return (DataSet) MemCachedManager.get("addonResults" + cacheKey.toString());
    }

    public void storeAddonReport(long dataSourceID, CacheKey cacheKey, DataSet results, int cacheTime) {
        String cacheKeyString = cacheKey.toString();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO REPORT_CACHE (cache_key, data_source_id, report_id) VALUES (?, ?, ?)");
            ps.setString(1, cacheKeyString);
            ps.setLong(2, dataSourceID);
            ps.setLong(3, cacheKey.getReportID());
            ps.execute();
            ps.close();
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
        MemCachedManager.add(cacheKeyString, cacheTime * 60, results);
    }

    public void flushResults(long dataSourceID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT CACHE_KEY FROM REPORT_CACHE WHERE DATA_SOURCE_ID = ?");
            queryStmt.setLong(1, dataSourceID);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                String cacheID = rs.getString(1);
                MemCachedManager.delete(cacheID);
            }
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM REPORT_CACHE WHERE DATA_SOURCE_ID = ?");
            deleteStmt.setLong(1, dataSourceID);
            deleteStmt.executeUpdate();
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void flushResultsForReport(long reportID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT CACHE_KEY FROM REPORT_CACHE WHERE REPORT_ID = ?");
            queryStmt.setLong(1, reportID);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                String cacheID = rs.getString(1);
                MemCachedManager.delete(cacheID);
            }
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM REPORT_CACHE WHERE REPORT_ID = ?");
            deleteStmt.setLong(1, reportID);
            deleteStmt.executeUpdate();
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }
}
