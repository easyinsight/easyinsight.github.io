package com.easyinsight.dataset;

import com.easyinsight.analysis.FilterDefinition;

import java.io.Serializable;
import java.util.List;

/**
 * User: jamesboe
 * Date: 6/2/14
 * Time: 10:30 AM
 */
public class CacheableDataSet implements Serializable {
    private DataSet dataSet;
    private List<String> filters;

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public List<String> getFilters() {
        return filters;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }
}
