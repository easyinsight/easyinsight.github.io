package com.easyinsight.datafeeds;

/**
 * User: James Boe
 * Date: Sep 13, 2008
 * Time: 4:46:18 PM
 */
public class FeedResponse {

    public static final int SUCCESS = 1;
    public static final int NEED_LOGIN = 2;
    public static final int REJECTED = 3;

    private int status;
    private FeedDescriptor feedDescriptor;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public FeedDescriptor getFeedDescriptor() {
        return feedDescriptor;
    }

    public void setFeedDescriptor(FeedDescriptor feedDescriptor) {
        this.feedDescriptor = feedDescriptor;
    }

    public FeedResponse() {
    }

    public FeedResponse(int status, FeedDescriptor feedDescriptor) {
        this.status = status;
        this.feedDescriptor = feedDescriptor;
    }
}
