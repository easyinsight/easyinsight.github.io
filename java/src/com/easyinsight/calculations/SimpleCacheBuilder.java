package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;

/**
 * User: jamesboe
 * Date: 9/11/12
 * Time: 2:22 PM
 */
public class SimpleCacheBuilder implements ICacheBuilder {

    private AnalysisItem instanceID;

    public SimpleCacheBuilder(AnalysisItem instanceID) {
        this.instanceID = instanceID;
    }

    public ICalculationCache createCache() {
        return new SimpleCalculationCache(instanceID);
    }
}
