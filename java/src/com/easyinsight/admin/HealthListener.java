package com.easyinsight.admin;

import com.easyinsight.config.ConfigLoader;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.DatabaseManager;
import com.xerox.amazonws.sqs2.Message;
import com.xerox.amazonws.sqs2.MessageQueue;
import com.xerox.amazonws.sqs2.SQSUtils;

import java.net.InetAddress;
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

    private boolean running = true;

    public void run() {
        try {
            MessageQueue messageQueue = SQSUtils.connectToQueue("EIHealth" + ConfigLoader.instance().isProduction(), "0AWCBQ78TJR8QCY8ABG2",
                    "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");

            while (running) {
                try {
                    Message message = messageQueue.receiveMessage();
                    if (message == null) {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException ex) {
                            // ignore
                        }
                    } else {
                        messageQueue.deleteMessage(message);
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
                        MessageQueue returnQueue = SQSUtils.connectToQueue("EIHealthReturn" + ConfigLoader.instance().isProduction(),
                                "0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
                        returnQueue.sendMessage(InetAddress.getLocalHost().getHostAddress() + "," + code + "," + response);
                    }
                } catch (Exception e) {
                    LogClass.error(e);
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
        }
    }

    public void stop() {
        running = false;
    }
}
