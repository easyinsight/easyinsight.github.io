package com.easyinsight.security;

/**
 * User: James Boe
 * Date: Aug 12, 2008
 * Time: 10:26:47 AM
 */
public class SecurityException extends RuntimeException {
    public static final int LOGIN_REQUIRED = 1;
    public static final int REJECTED = 2;

    private int reason;

    public SecurityException() {
        this(REJECTED);
    }

    public SecurityException(int reason) {
        this.reason = reason;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }
}
