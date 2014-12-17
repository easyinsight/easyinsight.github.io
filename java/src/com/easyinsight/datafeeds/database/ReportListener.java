package com.easyinsight.datafeeds.database;

import com.easyinsight.analysis.AsyncReport;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;

import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User: jamesboe
 * Date: 12/3/12
 * Time: 10:03 PM
 */
public class ReportListener implements Runnable {

    private boolean running;

    private static ReportListener instance;

    public static ReportListener instance() {
        return instance;
    }

    public static void initialize() {
        instance = new ReportListener();
        thread = new Thread(instance);
        thread.setName("Report Listener");
        thread.start();
    }

    public void stop() {
        running = false;
        thread.interrupt();
    }

    private static Thread thread;

    public void blah() throws Exception {
        running = true;

        int serverID = 0;

        EIConnection conn = Database.instance().getConnection();
        try {
            String host = InetAddress.getLocalHost().getHostName();
            PreparedStatement stmt = conn.prepareStatement("SELECT server_id FROM server WHERE server_host = ?");
            stmt.setString(1, host);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            serverID = rs.getInt(1);
            stmt.close();
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }

        while (running) {
            try {
                new AsyncReport(serverID).claimAndRun();
            } catch (Exception e) {
                LogClass.error(e);
            }
        }
    }

    public void run() {
        try {
            blah();
        } catch (Exception e) {
            LogClass.error(e);
        }
    }
}
