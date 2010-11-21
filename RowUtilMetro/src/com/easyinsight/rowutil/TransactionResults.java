package com.easyinsight.rowutil;

import java.util.List;

/**
 * User: jamesboe
 * Date: Sep 10, 2010
 * Time: 8:52:37 AM
 */
public class TransactionResults {
    private boolean successful;
    private List<RowResult> rows;
    private String failureMessage;

    public TransactionResults(boolean successful, List<RowResult> rows, String failureMessage) {
        this.successful = successful;
        this.rows = rows;
        this.failureMessage = failureMessage;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public List<RowResult> getRows() {
        return rows;
    }
}
