package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;

/**
 * User: jamesboe
 * Date: 9/11/12
 * Time: 2:22 PM
 */
public class ProcessCacheBuilder implements ICacheBuilder {

    private AnalysisItem instanceID;
    private AnalysisItem sortField;

    public ProcessCacheBuilder(AnalysisItem instanceID, AnalysisItem sortField) {
        this.instanceID = instanceID;
        this.sortField = sortField;
    }

    public ICalculationCache createCache() {
        return new ProcessCalculationCache(instanceID, sortField);
    }
}
