package com.easyinsight.database;

import com.easyinsight.logging.LogClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Feb 26, 2009
 * Time: 9:55:00 PM
 */
public class KeepAliveThread implements Runnable {

    private Database database;

    public KeepAliveThread(Database database) {
        this.database = database;
    }

    public void run() {
        while (true) {
            Connection conn = Database.instance().getConnection();
            try {
                PreparedStatement stmt = conn.prepareStatement("SHOW TABLES");
                stmt.executeQuery();
            } catch (SQLException e) {
                LogClass.error(e);
            } finally {
                Database.instance().closeConnection(conn);
            }
            try {
                Thread.sleep(1000 * 60 * 60);
            } catch (InterruptedException e) {
                LogClass.error(e);
            }
        }
    }
}
