package com.easyinsight.core;

import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;

import org.hibernate.Session;

/**
 * User: James Boe
 * Date: Jun 12, 2008
 * Time: 12:14:55 PM
 */
public class KeyStorage {

    public Key retrieveKey(long feedID, String name) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT ITEM_KEY.ITEM_KEY_ID FROM NAMED_ITEM_KEY, ITEM_KEY, ANALYSIS_ITEM, FEED_TO_ANALYSIS_ITEM WHERE " +
                    "NAMED_ITEM_KEY.ITEM_KEY_ID = ITEM_KEY.ITEM_KEY_ID AND ITEM_KEY.ITEM_KEY_ID = ANALYSIS_ITEM.ITEM_KEY_ID AND " +
                    "ANALYSIS_ITEM.ANALYSIS_ITEM_ID = FEED_TO_ANALYSIS_ITEM.ANALYSIS_ITEM_ID AND FEED_TO_ANALYSIS_ITEM.FEED_ID = ? AND NAMED_ITEM_KEY.NAME = ?");
            queryStmt.setLong(1, feedID);
            queryStmt.setString(2, name);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long itemKeyID = rs.getLong(1);
                Session session = Database.instance().createSession(conn);
                List results = session.createQuery("from Key where keyID = ?").setLong(0, itemKeyID).list();
                return (Key) results.get(0);                
            } else {
                throw new RuntimeException("Couldn't locate key in feed");
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }
}
