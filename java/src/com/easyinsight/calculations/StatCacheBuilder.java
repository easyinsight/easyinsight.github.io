package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;

/**
 * User: jamesboe
 * Date: 9/11/12
 * Time: 2:22 PM
 */
public class StatCacheBuilder implements ICacheBuilder {

    private AnalysisItem instanceID;
    private AnalysisItem statMeasure;
    private StatCalculationCache.StatFunction statFunction;

    public StatCacheBuilder(AnalysisItem instanceID, AnalysisItem statMeasure, StatCalculationCache.StatFunction statFunction) {
        this.instanceID = instanceID;
        this.statMeasure = statMeasure;
        this.statFunction = statFunction;
    }

    public ICalculationCache createCache() {
        return new StatCalculationCache(instanceID, statMeasure, statFunction);
    }
}
