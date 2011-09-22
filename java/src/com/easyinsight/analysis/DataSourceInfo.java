package com.easyinsight.analysis;

import java.util.Date;
import java.util.List;
import java.io.Serializable;

/**
 * User: jamesboe
 * Date: Jul 30, 2009
 * Time: 2:39:04 PM
 */
public class DataSourceInfo implements Serializable {

    public static final int STORED_PUSH = 1;
    public static final int STORED_PULL = 2;
    public static final int LIVE = 3;
    public static final int COMPOSITE = 4;
    public static final int COMPOSITE_PULL = 5;

    private String dataSourceName;
    private long dataSourceID;
    private Date lastDataTime;
    private boolean liveDataSource;
    private String originName;
    private int type;
    private List<DataSourceInfo> childSources;
    private long originSolution;
    private boolean scheduled;

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    public long getOriginSolution() {
        return originSolution;
    }

    public void setOriginSolution(long originSolution) {
        this.originSolution = originSolution;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<DataSourceInfo> getChildSources() {
        return childSources;
    }

    public void setChildSources(List<DataSourceInfo> childSources) {
        this.childSources = childSources;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public Date getLastDataTime() {
        return lastDataTime;
    }

    public void setLastDataTime(Date lastDataTime) {
        this.lastDataTime = lastDataTime;
    }

    public boolean isLiveDataSource() {
        return liveDataSource;
    }

    public void setLiveDataSource(boolean liveDataSource) {
        this.liveDataSource = liveDataSource;
    }
}
