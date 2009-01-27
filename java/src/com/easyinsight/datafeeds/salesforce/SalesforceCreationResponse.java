package com.easyinsight.datafeeds.salesforce;

import com.easyinsight.datafeeds.FeedDescriptor;

/**
 * User: James Boe
 * Date: Jul 6, 2008
 * Time: 12:23:00 PM
 */
public class SalesforceCreationResponse {
    private boolean successful;
    private String failureMessage;
    private FeedDescriptor feedDescriptor;

    public SalesforceCreationResponse(FeedDescriptor feedDescriptor, boolean successful) {
        this.feedDescriptor = feedDescriptor;
        this.successful = successful;
    }

    public SalesforceCreationResponse(boolean successful, String failureMessage) {
        this.successful = successful;
        this.failureMessage = failureMessage;
    }

    public SalesforceCreationResponse() {
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public FeedDescriptor getFeedDescriptor() {
        return feedDescriptor;
    }

    public void setFeedDescriptor(FeedDescriptor feedDescriptor) {
        this.feedDescriptor = feedDescriptor;
    }
}
