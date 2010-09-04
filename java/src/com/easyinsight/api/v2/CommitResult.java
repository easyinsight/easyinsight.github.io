package com.easyinsight.api.v2;

/**
 * User: jamesboe
 * Date: Sep 3, 2010
 * Time: 6:49:45 PM
 */
public class CommitResult {
    private RowStatus[] failedRows;
    private String dataSourceURL;
    private String dataSourceAPIKey;
    private boolean successful;
    private String failureMessage;

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public RowStatus[] getFailedRows() {
        return failedRows;
    }

    public void setFailedRows(RowStatus[] failedRows) {
        this.failedRows = failedRows;
    }

    public String getDataSourceURL() {
        return dataSourceURL;
    }

    public void setDataSourceURL(String dataSourceURL) {
        this.dataSourceURL = dataSourceURL;
    }

    public String getDataSourceAPIKey() {
        return dataSourceAPIKey;
    }

    public void setDataSourceAPIKey(String dataSourceAPIKey) {
        this.dataSourceAPIKey = dataSourceAPIKey;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
}
