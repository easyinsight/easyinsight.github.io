package com.easyinsight.analysis;

import java.io.Serializable;
import java.util.List;

/**
 * User: jamesboe
 * Date: 1/20/14
 * Time: 11:37 AM
 */
public class FilterSet implements Serializable {
    private List<FilterDefinition> filters;
    private long id;
    private String name;
    private long filterSetID;
    private long dataSourceID;
    private String urlKey;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public long getFilterSetID() {
        return filterSetID;
    }

    public void setFilterSetID(long filterSetID) {
        this.filterSetID = filterSetID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FilterDefinition> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDefinition> filters) {
        this.filters = filters;
    }
}
