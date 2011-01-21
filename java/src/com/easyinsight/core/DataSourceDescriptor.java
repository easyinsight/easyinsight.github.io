package com.easyinsight.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: James Boe
 * Date: Mar 30, 2009
 * Time: 2:31:58 PM
 */
public class DataSourceDescriptor extends EIDescriptor {

    private String description;
    private int dataSourceType;
    private long size;
    private Date creationDate;
    private Date lastDataTime;
    private long groupSourceID;
    private List<EIDescriptor> children = new ArrayList<EIDescriptor>();

    public List<EIDescriptor> getChildren() {
        return children;
    }

    public void setChildren(List<EIDescriptor> children) {
        this.children = children;
    }

    public long getGroupSourceID() {
        return groupSourceID;
    }

    public void setGroupSourceID(long groupSourceID) {
        this.groupSourceID = groupSourceID;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastDataTime() {
        return lastDataTime;
    }

    public void setLastDataTime(Date lastDataTime) {
        this.lastDataTime = lastDataTime;
    }

    public int getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(int dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getType() {
        return EIDescriptor.DATA_SOURCE;
    }

    public DataSourceDescriptor() {
    }

    public DataSourceDescriptor(String name, long id, int dataSourceType) {
        super(name, id);
        this.dataSourceType = dataSourceType;
    }
}
