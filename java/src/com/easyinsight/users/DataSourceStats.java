package com.easyinsight.users;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 3/8/12
 * Time: 3:27 PM
 */
public class DataSourceStats {
    private String name;
    private boolean visible;
    private long dataSourceID;
    private long size;
    private List<DataSourceStats> childStats = new ArrayList<DataSourceStats>();

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public List<DataSourceStats> getChildStats() {
        return childStats;
    }

    public void setChildStats(List<DataSourceStats> childStats) {
        this.childStats = childStats;
    }
}
