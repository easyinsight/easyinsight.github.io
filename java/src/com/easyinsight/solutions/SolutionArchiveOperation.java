package com.easyinsight.solutions;

import com.easyinsight.userupload.FileOperation;
import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Oct 11, 2008
 * Time: 6:42:00 PM
 */
public class SolutionArchiveOperation implements FileOperation {
    public byte[] retrieve(long solutionID, Long userID) {
        byte[] bytes;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT ARCHIVE, SOLUTION_ARCHIVE_NAME FROM SOLUTION WHERE SOLUTION_ID = ?");
            queryStmt.setLong(1, solutionID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                bytes = rs.getBytes(1);
            } else {
                throw new RuntimeException("No data found for that file ID.");
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return bytes;
    }
}
