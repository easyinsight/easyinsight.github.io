package com.easyinsight.core;

/**
 * User: James Boe
 * Date: Mar 30, 2009
 * Time: 2:31:58 PM
 */
public class DataSourceDescriptor extends EIDescriptor {

    private String description;
    private int dataSourceType;

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
