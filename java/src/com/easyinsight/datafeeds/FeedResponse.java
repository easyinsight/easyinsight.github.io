package com.easyinsight.datafeeds;

/**
 * User: James Boe
 * Date: Sep 13, 2008
 * Time: 4:46:18 PM
 */
public class FeedResponse {
    private boolean successful;
    private FeedDescriptor feedDescriptor;

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public FeedDescriptor getFeedDescriptor() {
        return feedDescriptor;
    }

    public void setFeedDescriptor(FeedDescriptor feedDescriptor) {
        this.feedDescriptor = feedDescriptor;
    }

    public FeedResponse() {
    }

    public FeedResponse(boolean successful, FeedDescriptor feedDescriptor) {
        this.successful = successful;
        this.feedDescriptor = feedDescriptor;
    }
}
