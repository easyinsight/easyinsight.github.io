package com.easyinsight.servlet;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * User: jamesboe
 * Date: 7/27/13
 * Time: 5:06 PM
 */
public class SystemSettings {
    private static SystemSettings instance;
    private Timer timer;

    private int semaphoreLimit = 2;

    private int maxFilterValues = 1000000;

    private int maxOperations = 10000000;

    private int sequenceLimit = 100000;

    private long headerImageID;

    private Map<String, Long> databaseMap = new HashMap<String, Long>();

    public SystemSettings() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                EIConnection conn = Database.instance().getConnection();
                try {
                    PreparedStatement ps = conn.prepareStatement("SELECT user_activity_semaphore_limit, max_filter_values, max_operations, " +
                            "header_image_id, sequence_limit FROM system_settings");
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        semaphoreLimit = rs.getInt(1);
                        maxFilterValues = rs.getInt(2);
                        maxOperations = rs.getInt(3);
                        headerImageID = rs.getLong(4);
                        sequenceLimit = rs.getInt(5);
                    }
                    ps.close();

                    PreparedStatement dbStmt = conn.prepareStatement("SELECT SUM(SIZE), DATABASE_NAME FROM FEED_PERSISTENCE_METADATA WHERE DATABASE_NAME IS NOT NULL GROUP BY DATABASE_NAME");
                    ResultSet dbRS = dbStmt.executeQuery();
                    while (dbRS.next()) {
                        long size = dbRS.getLong(1);
                        String dbName = dbRS.getString(2);
                        databaseMap.put(dbName, size);
                    }
                    dbStmt.close();
                } catch (Exception e) {
                    LogClass.error(e);
                } finally {
                    Database.closeConnection(conn);
                }
            }
        }, new Date(), 3600000);
    }

    public Map<String, Long> getDatabaseMap() {
        return new HashMap<String, Long>(databaseMap);
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public int getSequenceLimit() {
        return sequenceLimit;
    }

    public void setSequenceLimit(int sequenceLimit) {
        this.sequenceLimit = sequenceLimit;
    }

    public long getHeaderImageID() {
        return headerImageID;
    }

    public void setHeaderImageID(long headerImageID) {
        this.headerImageID = headerImageID;
    }

    public int getMaxOperations() {
        return maxOperations;
    }

    public void setMaxOperations(int maxOperations) {
        this.maxOperations = maxOperations;
    }

    public int getMaxFilterValues() {
        return maxFilterValues;
    }

    public void setMaxFilterValues(int maxFilterValues) {
        this.maxFilterValues = maxFilterValues;
    }

    public int getSemaphoreLimit() {
        return semaphoreLimit;
    }

    public void setSemaphoreLimit(int semaphoreLimit) {
        this.semaphoreLimit = semaphoreLimit;
    }

    public static SystemSettings instance() {
        return instance;
    }

    public static void initialize() {
        instance = new SystemSettings();
    }
}
