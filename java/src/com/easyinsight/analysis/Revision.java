package com.easyinsight.analysis;

import java.util.Date;

/**
 * User: jamesboe
 * Date: 9/25/12
 * Time: 10:53 AM
 */
public class Revision {
    private Date date;
    private long id;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
