package com.easyinsight.datafeeds;

import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * User: jboe
 * Date: Jan 3, 2008
 * Time: 3:37:02 PM
 */
public class FeedRegistry {
    private Map<Long, Feed> dataFeedMap = new WeakHashMap<Long, Feed>();
    private FeedStorage feedStorage = new FeedStorage();
    private static FeedRegistry instance;

    public static void initialize() {
        instance = new FeedRegistry();
    }

    public static FeedRegistry instance() {
        return instance;
    }

    public void flushCache(long feedID) {
        dataFeedMap.remove(feedID);        
    }

    public Feed getFeed(long identifier) {
        Feed feed = dataFeedMap.get(identifier);
        if (feed == null || (getLatestVersion(identifier) != feed.getVersion())) {
            FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(identifier);
            feed = feedDefinition.createFeed();
            dataFeedMap.put(identifier, feed);
        }
        return feed;
    }

    private int getLatestVersion(long identifier) {
        int version = 0;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT MAX(VERSION) FROM feed_persistence_metadata WHERE feed_id = ?");
            stmt.setLong(1, identifier);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                version = rs.getInt(1);
            }
        } catch (SQLException e) {
            LogClass.error(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return version;
    }

    public long addDataFeed(Feed feed) {
        //feedStorage.addDataFeed(dataFeed);
        dataFeedMap.put(feed.getFeedID(), feed);
        return feed.getFeedID();
    }
}
