package com.easyinsight.datafeeds;

import com.easyinsight.analysis.FilterDefinition;

import java.util.List;

/**
 * User: James Boe
 * Date: May 5, 2008
 * Time: 10:33:07 PM
 */

public class InitialAnalysis {
    private Long dataFeedID;
    private List<FilterDefinition> filterDefinitions;

    public Long getDataFeedID() {
        return dataFeedID;
    }

    public void setDataFeedID(Long dataFeedID) {
        this.dataFeedID = dataFeedID;
    }

    public List<FilterDefinition> getFilterDefinitions() {
        return filterDefinitions;
    }

    public void setFilterDefinitions(List<FilterDefinition> filterDefinitions) {
        this.filterDefinitions = filterDefinitions;
    }
}
