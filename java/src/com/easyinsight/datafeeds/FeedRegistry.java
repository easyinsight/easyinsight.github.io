package com.easyinsight.datafeeds;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

/**
 * User: jboe
 * Date: Jan 3, 2008
 * Time: 3:37:02 PM
 */
public class FeedRegistry {
    private FeedStorage feedStorage = new FeedStorage();
    private static FeedRegistry instance;

    private JCS cache = getCache();

    private JCS getCache() {

        try {
            return JCS.getInstance("feeds");
        } catch (Exception e) {
            LogClass.error(e);
        }
        return null;
    }

    public static void initialize() {
        instance = new FeedRegistry();
    }

    public static FeedRegistry instance() {
        return instance;
    }

    public void flushCache(long feedID) {
        try {
            if(cache != null)
                cache.remove(feedID);
        }
        catch(CacheException ce) {
            LogClass.error(ce);
        }
    }

    public Feed getFeed(long identifier, EIConnection conn) {
        Feed feed = null;
        if(cache != null)
            feed = (Feed) cache.get(identifier);

        if (feed == null || !isLatestVersion(feed, identifier)) {
            LogClass.debug("Cache miss for feed id: " + identifier);
            FeedDefinition feedDefinition;
            try {
                feedDefinition = feedStorage.getFeedDefinitionData(identifier);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            feed = feedDefinition.createFeed();
            feed.setVersion(getLatestVersion(identifier));
            try {
                if(cache != null)
                    cache.put(identifier, feed);
            }
            catch(CacheException e) {
                LogClass.error(e);
            }
        }
        else
            LogClass.debug("Cache hit for feed id: " + identifier);
        return feed;
    }

    private boolean isLatestVersion(Feed feed, long identifier) {
        if(feed == null)
            return false;
        LogClass.debug("Feed version: " + feed.getVersion());
        int latestVersion = getLatestVersion(identifier);
        LogClass.debug("Latest version: " + latestVersion);
        return feed.getVersion() == latestVersion;
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
            stmt.close();
        } catch (SQLException e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
        return version;
    }
}
