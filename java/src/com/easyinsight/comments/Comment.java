package com.easyinsight.comments;

import java.util.Date;

/**
 * User: James Boe
 * Date: Aug 29, 2008
 * Time: 12:04:44 PM
 */
public class Comment {
    private Date timestamp;
    private String message;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
