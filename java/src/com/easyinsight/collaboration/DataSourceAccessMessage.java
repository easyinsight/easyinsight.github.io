package com.easyinsight.collaboration;

/**
 * User: James Boe
 * Date: Feb 9, 2009
 * Time: 2:14:27 PM
 */
public class DataSourceAccessMessage extends NotificationMessage {

    private long dataSourceID;
    private int newRole;

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public int getNewRole() {
        return newRole;
    }

    public void setNewRole(int newRole) {
        this.newRole = newRole;
    }

    public int messageType() {
        return NotificationMessage.DATA_SOURCE_ACCESS;
    }
}
