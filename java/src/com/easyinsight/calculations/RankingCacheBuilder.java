package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisMeasure;

import java.util.List;

/**
 * User: jamesboe
 * Date: 9/11/12
 * Time: 2:22 PM
 */
public class RankingCacheBuilder implements ICacheBuilder {

    private AnalysisItem instanceID;
    private AnalysisMeasure metric;
    private boolean ascending;
    private List<AnalysisItem> additionals;

    public RankingCacheBuilder(AnalysisItem instanceID, AnalysisMeasure metric, boolean ascending, List<AnalysisItem> additionals) {
        this.instanceID = instanceID;
        this.metric = metric;
        this.ascending = ascending;
        this.additionals = additionals;
    }

    public ICalculationCache createCache() {
        return new RankingCalculationCache(instanceID, metric, ascending, additionals);
    }
}
