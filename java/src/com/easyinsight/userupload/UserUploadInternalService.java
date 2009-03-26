package com.easyinsight.userupload;

import com.easyinsight.logging.LogClass;
import com.easyinsight.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Mar 26, 2009
 * Time: 4:14:09 PM
 */
public class UserUploadInternalService {
    public void createUserFeedLink(long userID, long dataFeedID, int owner, Connection conn) {
        try {
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT UPLOAD_POLICY_USERS_ID FROM UPLOAD_POLICY_USERS WHERE " +
                    "USER_ID = ? AND FEED_ID = ?");
            existingLinkQuery.setLong(1, userID);
            existingLinkQuery.setLong(2, dataFeedID);
            ResultSet existingRS = existingLinkQuery.executeQuery();
            if (existingRS.next()) {
                long existingID = existingRS.getLong(1);
                PreparedStatement updateLinkStmt = conn.prepareStatement("UPDATE UPLOAD_POLICY_USERS SET ROLE = ? WHERE " +
                        "UPLOAD_POLICY_USERS_ID = ?");
                updateLinkStmt.setLong(1, owner);
                updateLinkStmt.setLong(2, existingID);
                updateLinkStmt.executeUpdate();
            } else {
                PreparedStatement insertFeedStmt = conn.prepareStatement("INSERT INTO UPLOAD_POLICY_USERS (USER_ID, FEED_ID, ROLE) " +
                        "VALUES (?, ?, ?)");
                insertFeedStmt.setLong(1, userID);
                insertFeedStmt.setLong(2, dataFeedID);
                insertFeedStmt.setLong(3, owner);
                insertFeedStmt.execute();
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void createUserFeedLink(long userID, long dataFeedID, int owner) {
        Connection conn = Database.instance().getConnection();
        try {
            createUserFeedLink(userID, dataFeedID, owner, conn);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }
}
