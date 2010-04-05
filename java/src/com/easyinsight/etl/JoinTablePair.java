package com.easyinsight.etl;

import com.easyinsight.analysis.FilterDefinition;

/**
 * User: jamesboe
 * Date: Apr 4, 2010
 * Time: 3:25:13 PM
 */
public class JoinTablePair {
    private FilterDefinition sourceFilter;
    private FilterDefinition targetFilter;

    public FilterDefinition getSourceFilter() {
        return sourceFilter;
    }

    public void setSourceFilter(FilterDefinition sourceFilter) {
        this.sourceFilter = sourceFilter;
    }

    public FilterDefinition getTargetFilter() {
        return targetFilter;
    }

    public void setTargetFilter(FilterDefinition targetFilter) {
        this.targetFilter = targetFilter;
    }
}
