package com.easyinsight.export;

import com.easyinsight.userupload.FileOperation;
import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Jan 20, 2009
 * Time: 2:14:07 PM
 */
public class ExcelOperation implements FileOperation {
    public byte[] retrieve(long fileID, long userID) {
        byte[] bytes;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT excel_file, USER_ID FROM EXCEL_EXPORT WHERE EXCEL_EXPORT_ID = ?");
            queryStmt.setLong(1, fileID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long dbUserID = rs.getLong(2);
                if (dbUserID != userID) {
                    throw new SecurityException();
                } else {
                    bytes = rs.getBytes(1);
                }
            } else {
                throw new RuntimeException("No data found for that file ID.");
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return bytes;
    }
}
