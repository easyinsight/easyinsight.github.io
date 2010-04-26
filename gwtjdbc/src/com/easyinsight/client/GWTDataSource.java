package com.easyinsight.client;

import java.io.Serializable;

/**
 * User: jamesboe
 * Date: Apr 24, 2010
 * Time: 1:59:50 PM
 */
public class GWTDataSource implements Serializable {
    private long dataSourceID;
    private String dataSourceName;

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }
}
