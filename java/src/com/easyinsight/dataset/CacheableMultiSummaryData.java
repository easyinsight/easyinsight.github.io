package com.easyinsight.dataset;

import com.easyinsight.export.MultiSummaryData;

import java.io.Serializable;
import java.util.List;

/**
 * User: jamesboe
 * Date: 6/2/14
 * Time: 10:30 AM
 */
public class CacheableMultiSummaryData implements Serializable {
    private MultiSummaryData multiSummaryData;
    private List<String> filters;

    public MultiSummaryData getMultiSummaryData() {
        return multiSummaryData;
    }

    public void setMultiSummaryData(MultiSummaryData multiSummaryData) {
        this.multiSummaryData = multiSummaryData;
    }

    public List<String> getFilters() {
        return filters;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }
}
