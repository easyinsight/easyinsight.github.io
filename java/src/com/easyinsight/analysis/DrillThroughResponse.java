package com.easyinsight.analysis;

import com.easyinsight.core.EIDescriptor;

import java.util.List;

/**
 * User: jamesboe
 * Date: 1/26/12
 * Time: 11:07 AM
 */
public class DrillThroughResponse {
    private EIDescriptor descriptor;
    private List<FilterDefinition> filters;

    public EIDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(EIDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public List<FilterDefinition> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDefinition> filters) {
        this.filters = filters;
    }
}
