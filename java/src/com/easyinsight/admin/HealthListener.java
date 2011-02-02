package com.easyinsight.admin;

import com.easyinsight.config.ConfigLoader;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.DatabaseManager;
import com.xerox.amazonws.sqs2.Message;
import com.xerox.amazonws.sqs2.MessageQueue;
import com.xerox.amazonws.sqs2.SQSUtils;
import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 1/26/11
 * Time: 10:58 AM
 */
public class HealthListener implements Runnable {

    public static final String SUCCESS = "Success";
    public static final String FAILURE = "Failure";

    private JCS serverCache = getCache("servers");

    private JCS getCache(String cacheName) {

        try {
            return JCS.getInstance(cacheName);
        } catch (Exception e) {
            LogClass.error(e);
        }
        return null;
    }

    private boolean running = true;

    public void run() {
        initialSetup();

        while (running && serverCache != null) {
            try {

                String response = "All Good";
                String code = SUCCESS;
                {
                    int activeConnections = Database.instance().getActiveConnections();
                    int maxConnections = Database.instance().getMaxConnections();
                    if (activeConnections == maxConnections) {
                        response = "Core database pool is exhausted";
                        code = FAILURE;
                    } else {
                        EIConnection conn = Database.instance().getConnection();
                        try {
                            Statement stmt = conn.createStatement();
                            stmt.executeQuery("SELECT 1");
                        } catch (Exception e) {
                            response = e.getMessage() + " on query of core database pool";
                            code = FAILURE;
                        } finally {
                            Database.closeConnection(conn);
                        }
                    }
                }
                for (Map.Entry<String, Database> storageEntry : DatabaseManager.instance().getDbMap().entrySet()) {
                    Database database = storageEntry.getValue();
                    int activeConnections = database.getActiveConnections();
                    int maxConnections = database.getMaxConnections();
                    if (activeConnections == maxConnections) {
                        response = "Database pool " + storageEntry.getKey() + " is exhausted";
                        code = FAILURE;
                    } else {
                        EIConnection conn = database.getConnection();
                        try {
                            Statement stmt = conn.createStatement();
                            stmt.executeQuery("SELECT 1");
                        } catch (Exception e) {
                            response = e.getMessage() + " on query of " + storageEntry.getKey() + " database pool";
                            code = FAILURE;
                        } finally {
                            Database.closeConnection(conn);
                        }
                    }
                }
                Status status = new Status();
                status.setTime(System.currentTimeMillis());
                status.setCode(code);
                status.setMessage(response);
                status.setHealthInfo(new AdminService().getHealthInfo());
                serverCache.put(InetAddress.getLocalHost().getHostName(), status);
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    // ignore
                }
            } catch (CacheException ce) {
                // ignore
            } catch (Exception e) {
                LogClass.error(e);
            }
        }
    }

    private void initialSetup() {
        EIConnection conn = Database.instance().getConnection();
        try
        {
            String host = InetAddress.getLocalHost().getHostName();
            PreparedStatement stmt = conn.prepareStatement("SELECT SERVER_ID FROM SERVER WHERE SERVER_HOST = ?");
            stmt.setString(1, host);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO SERVER (SERVER_HOST, ENABLED) VALUES (?, ?)");
                insertStmt.setString(1, host);
                insertStmt.setBoolean(2, false);
                insertStmt.execute();
            }
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void stop() {
        running = false;
    }
}
