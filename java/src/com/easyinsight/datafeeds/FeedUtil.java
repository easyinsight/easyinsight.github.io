package com.easyinsight.datafeeds;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 1, 2009
 * Time: 10:27:53 AM
 */
public class FeedUtil {

    public static Map<Long, String> getFeedNames(List<Long> feedIds, Connection conn) throws SQLException {
        Map<Long, String> results = new HashMap<Long, String>();
        String str = "SELECT data_feed_id, feed_name FROM data_feed WHERE data_feed_id IN (";
        boolean first = true;
        for(int i = 0;i < feedIds.size();i++) {
            if(!first)
                str += ",";
            str += "?";
            first = false;
        }
        str += ")";
        PreparedStatement stmt = conn.prepareStatement(str);
        for(int i = 0;i < feedIds.size();i++) {
            stmt.setLong(i+1, feedIds.get(i));
        }
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            long feedID = rs.getLong(1);
            String feedName = rs.getString(2);
            results.put(feedID, feedName);
        }
        return results;
    }
}
