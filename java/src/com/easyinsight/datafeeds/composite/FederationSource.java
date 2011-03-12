package com.easyinsight.datafeeds.composite;

/**
 * User: jamesboe
 * Date: 3/5/11
 * Time: 6:30 PM
 */
public class FederationSource {
    private long dataSourceID;
    private String value;
    private String name;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
