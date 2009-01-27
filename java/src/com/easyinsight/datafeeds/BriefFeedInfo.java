package com.easyinsight.datafeeds;

/**
 * User: James Boe
 * Date: Jul 12, 2008
 * Time: 7:20:21 PM
 */
public class BriefFeedInfo {
    private String name;
    private long feedID;

    public BriefFeedInfo() {
    }

    public BriefFeedInfo(String name, long feedID) {
        this.name = name;
        this.feedID = feedID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getFeedID() {
        return feedID;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }
}
