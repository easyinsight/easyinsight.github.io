package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisMeasure;

/**
 * User: jamesboe
 * Date: 9/11/12
 * Time: 2:22 PM
 */
public class RankingCacheBuilder implements ICacheBuilder {

    private AnalysisItem instanceID;
    private AnalysisMeasure metric;
    private boolean ascending;

    public RankingCacheBuilder(AnalysisItem instanceID, AnalysisMeasure metric, boolean ascending) {
        this.instanceID = instanceID;
        this.metric = metric;
        this.ascending = ascending;
    }

    public ICalculationCache createCache() {
        return new RankingCalculationCache(instanceID, metric, ascending);
    }
}
