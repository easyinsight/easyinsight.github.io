package com.easyinsight.userupload;

/**
 * User: James Boe
 * Date: Jun 14, 2008
 * Time: 12:40:04 AM
 */
public class UploadResponse {
    private boolean successful;
    private String failureMessage;
    private long feedID;
    private long initialAnalysisID;

    public UploadResponse(String failureMessage) {
        this.successful = false;
        this.failureMessage = failureMessage;
    }

    public UploadResponse(long feedID, long initialAnalysisID) {
        this.successful = true;
        this.feedID = feedID;
        this.initialAnalysisID = initialAnalysisID;
    }

    public UploadResponse() {
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

    public long getFeedID() {
        return feedID;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }

    public long getInitialAnalysisID() {
        return initialAnalysisID;
    }

    public void setInitialAnalysisID(long initialAnalysisID) {
        this.initialAnalysisID = initialAnalysisID;
    }
}
