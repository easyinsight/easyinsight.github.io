package com.easyinsight.datafeeds.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 3/5/11
 * Time: 6:30 PM
 */
public class FederationSource {
    private long dataSourceID;
    private String value;
    private String name;
    private int dataSourceType;
    private List<FieldMapping> fieldMappings = new ArrayList<FieldMapping>();

    public List<FieldMapping> getFieldMappings() {
        return fieldMappings;
    }

    public void setFieldMappings(List<FieldMapping> fieldMappings) {
        this.fieldMappings = fieldMappings;
    }

    public int getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(int dataSourceType) {
        this.dataSourceType = dataSourceType;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
