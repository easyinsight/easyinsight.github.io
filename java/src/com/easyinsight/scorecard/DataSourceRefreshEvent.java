package com.easyinsight.scorecard;

import com.easyinsight.kpi.KPI;
import com.easyinsight.scheduler.OutboundEvent;

import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: Feb 24, 2010
 * Time: 11:15:01 AM
 */
public class DataSourceRefreshEvent extends OutboundEvent {

    public static final int DATA_SOURCE_NAME = 1;
    public static final int PROGRESS = 3;
    public static final int DONE = 2;
    public static final int BLOCKED = 4;

    private long dataSourceID;
    private int type;
    private String dataSourceName;
    private int current;
    private int max;
    private Date timestamp = new Date();
    private boolean async;

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }
}