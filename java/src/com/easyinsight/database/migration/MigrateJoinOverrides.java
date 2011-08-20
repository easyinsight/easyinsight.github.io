package com.easyinsight.database.migration;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.util.RandomTextGenerator;
import org.hibernate.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: Apr 2, 2010
 * Time: 11:17:34 AM
 */
public class MigrateJoinOverrides implements Migration {

    public boolean needToRun() {
        return true;
    }

    public void migrate() {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement("SELECT analysis.analysis_id, analysis_to_join_override.join_override_id, data_feed_id from analysis_to_join_override, analysis, join_override where " +
                    "analysis_to_join_override.analysis_id = analysis.analysis_id and join_override.data_source_id is null and analysis_to_join_override.join_override_id = join_override.join_override_id");
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE join_override SET data_source_id = ? WHERE join_override_id = ?");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                long reportID = rs.getLong(1);
                long joinID = rs.getLong(2);
                long dataSourceID = rs.getInt(3);
                updateStmt.setLong(1, dataSourceID);
                updateStmt.setLong(2, joinID);
                updateStmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            LogClass.error(e);
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }
}
