package com.easyinsight.notifications;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 2, 2009
 * Time: 9:05:10 AM
 */
public class ConfigureDataFeedInfo extends TodoEventInfo {
    private String feedName;
    private long feedID;

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public long getFeedID() {
        return feedID;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }
}
