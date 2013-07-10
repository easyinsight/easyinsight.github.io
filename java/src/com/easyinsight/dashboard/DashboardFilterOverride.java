package com.easyinsight.dashboard;

import com.easyinsight.analysis.FilterDefinition;

/**
 * User: jamesboe
 * Date: 7/1/13
 * Time: 11:08 AM
 */
public class DashboardFilterOverride {
    private long filterID;
    private FilterDefinition filterDefinition;
    private boolean hideFilter;

    public DashboardFilterOverride() {
    }

    public DashboardFilterOverride(long filterID, boolean hideFilter) {
        this.filterID = filterID;
        this.hideFilter = hideFilter;
    }

    public FilterDefinition getFilterDefinition() {
        return filterDefinition;
    }

    public void setFilterDefinition(FilterDefinition filterDefinition) {
        this.filterDefinition = filterDefinition;
    }

    public long getFilterID() {
        return filterID;
    }

    public void setFilterID(long filterID) {
        this.filterID = filterID;
    }

    public boolean isHideFilter() {
        return hideFilter;
    }

    public void setHideFilter(boolean hideFilter) {
        this.hideFilter = hideFilter;
    }
}
