package com.easyinsight.userupload;

import com.easyinsight.AnalysisItem;

import java.util.List;

/**
 * User: James Boe
 * Date: Jan 26, 2008
 * Time: 9:20:29 PM
 */
public class UserUploadAnalysis {
    private boolean successful;
    private String failureMessage;
    private List<AnalysisItem> fields;
    private GridMapping gridMapping;
    private long size;

    public UserUploadAnalysis() {
    }

    public UserUploadAnalysis(String failureMessage) {
        this.successful = false;
        this.failureMessage = failureMessage;
    }

    public UserUploadAnalysis(List<AnalysisItem> fields) {
        this.successful = true;
        this.fields = fields;
    }

    public GridMapping getGridMapping() {
        return gridMapping;
    }

    public void setGridMapping(GridMapping gridMapping) {
        this.gridMapping = gridMapping;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
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

    public List<AnalysisItem> getFields() {
        return fields;
    }

    public void setFields(List<AnalysisItem> fields) {
        this.fields = fields;
    }
}
