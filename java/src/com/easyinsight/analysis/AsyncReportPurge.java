package com.easyinsight.analysis;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * User: jamesboe
 * Date: 3/19/15
 * Time: 12:53 PM
 */
public class AsyncReportPurge {
    private Timer timer;

    private static AsyncReportPurge instance;

    public static AsyncReportPurge instance() {
        return instance;
    }

    public static void initialize() {
        instance = new AsyncReportPurge();
        instance.start();
    }

    public void start() {
        timer = new Timer();
        int interval = (int) (1000 * 60 * 60 * (Math.random() * 12 + 6));
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                EIConnection conn = Database.instance().getConnection();
                try {
                    conn.setAutoCommit(false);
                    PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM ASYNC_REPORT_REQUEST WHERE " +
                            "ASYNC_REPORT_REQUEST.REQUEST_CREATED < ?");
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_YEAR, -1);
                    System.out.println("Purging async report request entries before " + cal.getTime());
                    deleteStmt.setTimestamp(1, new Timestamp(cal.getTimeInMillis()));
                    deleteStmt.executeUpdate();
                    deleteStmt.close();

                    PreparedStatement deleteDetsStmt = conn.prepareStatement("DELETE FROM ASYNC_REPORT_REQUEST_DETAILS WHERE " +
                            "ASYNC_REPORT_REQUEST_DETAILS.CREATE_TIME < ?");
                    deleteDetsStmt.setTimestamp(1, new Timestamp(cal.getTimeInMillis()));
                    deleteDetsStmt.executeUpdate();
                    deleteDetsStmt.close();
                    conn.commit();
                } catch (Exception e) {
                    LogClass.error(e);
                    conn.rollback();
                } finally {
                    conn.setAutoCommit(true);
                    Database.closeConnection(conn);
                }
            }
        }, interval, interval);
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
