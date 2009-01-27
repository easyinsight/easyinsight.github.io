package com.easyinsight.datafeeds;

import java.util.*;

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
        if (feed == null) {
            FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(identifier);
            feed = feedDefinition.createFeed();
            dataFeedMap.put(identifier, feed);
        }
        return feed;
    }

    public long addDataFeed(Feed feed) {
        //feedStorage.addDataFeed(dataFeed);
        dataFeedMap.put(feed.getFeedID(), feed);
        return feed.getFeedID();
    }
}
