package com.easyinsight.dbclient;

import org.hibernate.annotations.Entity;

import java.io.Serializable;

/**
 * User: jamesboe
 * Date: Apr 12, 2010
 * Time: 5:28:14 PM
 */
@Entity
public class DataSource implements Serializable {
    private Long dataSourceID;
    private String dataSourceName;

    public Long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(Long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }
}
