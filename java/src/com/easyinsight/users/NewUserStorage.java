package com.easyinsight.users;

import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Oct 2, 2008
 * Time: 11:02:18 AM
 */
public class NewUserStorage {

    public String inviteNewUserToGroup(long groupID) {
        Connection conn = Database.instance().getConnection();
        try {
            char[] activationBuf = new char[30];
            for (int i = 0; i < 30; i++) {
                char c;
                int randVal = (int) (Math.random() * 36);
                if (randVal < 26) {
                    c = (char) ('a' + randVal);
                } else {
                    c = (char) (randVal - 26);
                }
                activationBuf[i] = c;
            }
            PreparedStatement addActivationStmt = conn.prepareStatement("INSERT INTO GROUP_NEW_USER_INVITE (GROUP_ID, ACTIVATION_STRING) VALUES (?, ?)");
            addActivationStmt.setLong(1, groupID);
            String activationString = new String(activationBuf);
            addActivationStmt.setString(2, activationString);
            addActivationStmt.execute();
            return activationString;
        } catch (
            SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }
}
