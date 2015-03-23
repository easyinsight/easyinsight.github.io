package com.easyinsight.servlet;

import com.easyinsight.analysis.AsyncReport;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;

import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Timer;
import java.util.TimerTask;

/**
 * User: jamesboe
 * Date: 12/17/14
 * Time: 1:41 PM
 */
public class WorkerManager {
    private Timer timer;

    private int serverID;

    private WorkerManager() {
        EIConnection conn = Database.instance().getConnection();
        try {
            String host = InetAddress.getLocalHost().getHostName();
            PreparedStatement stmt = conn.prepareStatement("SELECT server_id FROM server WHERE server_host = ?");
            stmt.setString(1, host);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            this.serverID = rs.getInt(1);
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private static WorkerManager instance;

    public static WorkerManager instance() {
        return instance;
    }

    public static void initialize() {
        instance = new WorkerManager();
        instance.start();
    }

    public void stop() {
        timer.cancel();
    }

    private void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                new AsyncReport(serverID).assign();
            }
        }, 250, 250);
    }
}
