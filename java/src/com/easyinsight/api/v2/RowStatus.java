package com.easyinsight.api.v2;

import com.easyinsight.api.Row;

/**
 * User: jamesboe
 * Date: Sep 3, 2010
 * Time: 6:53:45 PM
 */
public class RowStatus {
    private Row row;
    private String failureMessage;

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }
}
