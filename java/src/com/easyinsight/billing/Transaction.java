package com.easyinsight.billing;

/**
 * User: James Boe
 * Date: Aug 3, 2008
 * Time: 3:08:03 PM
 */
public class Transaction {
    private String transactionID;
    private Object callerReference;

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public Object getCallerReference() {
        return callerReference;
    }
}
