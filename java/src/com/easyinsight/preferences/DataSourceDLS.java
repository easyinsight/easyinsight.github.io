package com.easyinsight.preferences;

import com.easyinsight.analysis.FilterDefinition;

import java.util.List;

/**
 * User: jamesboe
 * Date: 8/1/11
 * Time: 2:18 PM
 */
public class DataSourceDLS {
    private long dataSourceDLSID;
    private long dataSourceID;
    private String dataSourceName;
    private List<FilterDefinition> filters;

    public long getDataSourceDLSID() {
        return dataSourceDLSID;
    }

    public void setDataSourceDLSID(long dataSourceDLSID) {
        this.dataSourceDLSID = dataSourceDLSID;
    }

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

    public List<FilterDefinition> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDefinition> filters) {
        this.filters = filters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataSourceDLS that = (DataSourceDLS) o;

        if (dataSourceDLSID != that.dataSourceDLSID) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (dataSourceDLSID ^ (dataSourceDLSID >>> 32));
    }
}
