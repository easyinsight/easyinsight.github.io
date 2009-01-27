package com.easyinsight.groups;

import java.util.Date;

/**
 * User: James Boe
 * Date: Sep 9, 2008
 * Time: 1:57:27 PM
 */
public class GroupChange {
    private Date date;
    private String message;

    public GroupChange() {
    }

    public GroupChange(Date date, String message) {
        this.date = date;
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
