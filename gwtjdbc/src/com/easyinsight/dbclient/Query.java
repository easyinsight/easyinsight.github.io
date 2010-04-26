package com.easyinsight.dbclient;

import org.hibernate.annotations.Entity;

import java.io.Serializable;

/**
 * User: jamesboe
 * Date: Apr 12, 2010
 * Time: 5:29:34 PM
 */
@Entity
public class Query implements Serializable {
    private DataSource dataSource;
    private Long queryID;
    private String sql;
    private int mode;
    private String eiDataSourceName;
    private String eiDataSourceAPIKey;

    public String getEiDataSourceAPIKey() {
        return eiDataSourceAPIKey;
    }

    public void setEiDataSourceAPIKey(String eiDataSourceAPIKey) {
        this.eiDataSourceAPIKey = eiDataSourceAPIKey;
    }

    public String getEiDataSourceName() {
        return eiDataSourceName;
    }

    public void setEiDataSourceName(String eiDataSourceName) {
        this.eiDataSourceName = eiDataSourceName;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Long getQueryID() {
        return queryID;
    }

    public void setQueryID(Long queryID) {
        this.queryID = queryID;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
