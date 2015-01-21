package com.easyinsight.analysis;

/**
 * User: jamesboe
 * Date: 1/21/15
 * Time: 6:55 AM
 */
public abstract class AsyncRequestRunnable implements Runnable {

    private long requestID;

    protected AsyncRequestRunnable(long requestID) {
        this.requestID = requestID;
    }

    public long getRequestID() {
        return requestID;
    }
}
