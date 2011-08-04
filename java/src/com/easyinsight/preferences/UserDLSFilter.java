package com.easyinsight.preferences;

import com.easyinsight.analysis.FilterDefinition;

/**
 * User: jamesboe
 * Date: 8/1/11
 * Time: 8:06 PM
 */
public class UserDLSFilter {
    private FilterDefinition filterDefinition;
    private long originalFilterID;

    public FilterDefinition getFilterDefinition() {
        return filterDefinition;
    }

    public void setFilterDefinition(FilterDefinition filterDefinition) {
        this.filterDefinition = filterDefinition;
    }

    public long getOriginalFilterID() {
        return originalFilterID;
    }

    public void setOriginalFilterID(long originalFilterID) {
        this.originalFilterID = originalFilterID;
    }
}
