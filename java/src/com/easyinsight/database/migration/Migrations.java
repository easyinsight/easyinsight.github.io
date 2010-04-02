package com.easyinsight.database.migration;

import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Apr 27, 2009
 * Time: 11:45:57 AM
 */
public class Migrations {
    public static final Migration[] migrations = new Migration [] { new Migrate132to133() };
    public static final String MIGRATION = "migration";

    public void migrate() {
        if (obtainLock()) {
            try {
                for (Migration migration : migrations) {
                    if (migration.needToRun()) {
                        migration.migrate();
                    }
                }
            } finally {
                releaseLock();
            }
        }
    }

    private void releaseLock() {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement lockStmt = conn.prepareStatement("DELETE FROM DISTRIBUTED_LOCK WHERE LOCK_NAME = ?");
            lockStmt.setString(1, MIGRATION);
            lockStmt.executeUpdate();
            lockStmt.close();
        } catch (SQLException e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private boolean obtainLock() {
        boolean locked = false;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement lockStmt = conn.prepareStatement("INSERT INTO DISTRIBUTED_LOCK (LOCK_NAME) VALUES (?)");
            lockStmt.setString(1, MIGRATION);
            lockStmt.execute();
            locked = true;
            lockStmt.close();
        } catch (SQLException e) {
            LogClass.debug("Failed to obtain distributed lock, assuming another app server has it.");
        } finally {
            Database.closeConnection(conn);
        }
        return locked;
    }
}
