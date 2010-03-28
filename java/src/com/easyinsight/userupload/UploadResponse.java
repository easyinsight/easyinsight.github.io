package com.easyinsight.userupload;

import java.util.List;

/**
 * User: James Boe
 * Date: Jun 14, 2008
 * Time: 12:40:04 AM
 */
public class UploadResponse {
    private boolean successful;
    private String failureMessage;
    private long feedID;
    private List<FieldUploadInfo> infos;

    public UploadResponse(String failureMessage) {
        this.successful = false;
        this.failureMessage = failureMessage;
    }

    public UploadResponse(long feedID) {
        this.successful = true;
        this.feedID = feedID;
    }

    public UploadResponse() {
    }

    public List<FieldUploadInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<FieldUploadInfo> infos) {
        this.infos = infos;
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
}
