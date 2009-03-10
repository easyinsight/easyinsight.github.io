package com.easyinsight.dbservice;

/**
 * User: James Boe
 * Date: Jan 31, 2009
 * Time: 7:01:00 PM
 */
public class QueryConfiguration {

    public static final int ADD = 1;
    public static final int REPLACE = 2;
    public static final int ADD_EXCLUSIVE_TODAY = 3;

    private String query;
    private String dataSource;
    private boolean adHoc;
    private long queryConfigurationID;
    private String name;
    private int publishMode;

    public int getPublishMode() {
        return publishMode;
    }

    public void setPublishMode(int publishMode) {
        this.publishMode = publishMode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getQueryConfigurationID() {
        return queryConfigurationID;
    }

    public void setQueryConfigurationID(long queryConfigurationID) {
        this.queryConfigurationID = queryConfigurationID;
    }

    public boolean isAdHoc() {
        return adHoc;
    }

    public void setAdHoc(boolean adHoc) {
        this.adHoc = adHoc;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
}
