package com.easyinsight.users;

import java.util.Date;

/**
 * User: jamesboe
 * Date: 2/24/12
 * Time: 12:45 PM
 */
public class TimerRequest {
    private int dataSourceID;
    private Date date;

    public int getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(int dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
