package com.easyinsight.audit;

import java.util.Date;

/**
 * User: James Boe
 * Date: Jan 23, 2009
 * Time: 9:16:16 PM
 */
public class AuditMessage {
    private long userID;
    private String userName;
    private Date timestamp;
    private String message;
    private boolean audit;

    public AuditMessage(long userID, Date timestamp, String message) {
        this.userID = userID;
        this.timestamp = timestamp;
        this.message = message;
    }

    public AuditMessage() {

    }

    public boolean isAudit() {
        return audit;
    }

    public void setAudit(boolean audit) {
        this.audit = audit;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
