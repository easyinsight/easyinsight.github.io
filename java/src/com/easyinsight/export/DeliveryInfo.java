package com.easyinsight.export;

import com.easyinsight.analysis.FilterDefinition;

import java.util.List;

/**
 * User: jamesboe
 * Date: 6/3/11
 * Time: 12:21 PM
 */
public class DeliveryInfo {

    public static final int REPORT = 1;
    public static final int SCORECARD = 2;

    private String name;
    private long id;
    private long dataSourceID;
    private String label;
    private List<FilterDefinition> filters;
    private int type;
    private int index;
    private int format;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public List<FilterDefinition> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDefinition> filters) {
        this.filters = filters;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
