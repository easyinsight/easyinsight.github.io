package com.easyinsight.intention;

import com.easyinsight.analysis.FilterDefinition;

/**
 * User: jamesboe
 * Date: 9/17/11
 * Time: 12:16 PM
 */
public class AddFilterIntention extends Intention {
    private FilterDefinition filterDefinition;

    public AddFilterIntention() {
    }

    public AddFilterIntention(FilterDefinition filterDefinition) {
        this.filterDefinition = filterDefinition;
    }

    public FilterDefinition getFilterDefinition() {
        return filterDefinition;
    }

    public void setFilterDefinition(FilterDefinition filterDefinition) {
        this.filterDefinition = filterDefinition;
    }
}
