package com.easyinsight.servlet;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * User: jamesboe
 * Date: 7/27/13
 * Time: 5:06 PM
 */
public class SystemSettings {
    private static SystemSettings instance;
    private Timer timer;

    private int semaphoreLimit = 2;

    public SystemSettings() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                EIConnection conn = Database.instance().getConnection();
                try {
                    PreparedStatement ps = conn.prepareStatement("SELECT user_activity_semaphore_limit FROM system_settings");
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        semaphoreLimit = rs.getInt(1);
                    }
                } catch (Exception e) {
                    LogClass.error(e);
                } finally {
                    Database.closeConnection(conn);
                }
            }
        }, new Date(), 60000);
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
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
