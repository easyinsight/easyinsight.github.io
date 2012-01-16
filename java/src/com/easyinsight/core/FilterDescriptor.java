package com.easyinsight.core;

import com.easyinsight.analysis.FilterDefinition;

/**
 * User: jamesboe
 * Date: 1/16/12
 * Time: 10:15 AM
 */
public class FilterDescriptor extends EIDescriptor {

    @Override
    public int getType() {
        return EIDescriptor.FILTER;
    }
    
    public FilterDescriptor(FilterDefinition filterDefinition) {
        super(filterDefinition.getFilterName(), filterDefinition.getFilterID(), false);
    }
}
