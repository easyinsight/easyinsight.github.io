package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.WSAnalysisDefinition;

/**
 * User: jamesboe
 * Date: 9/11/12
 * Time: 2:22 PM
 */
public class AltCumulativeCacheBuilder implements ICacheBuilder {

    private AnalysisItem dateField;
    private WSAnalysisDefinition report;

    public AltCumulativeCacheBuilder(AnalysisItem dateField, WSAnalysisDefinition report) {
        this.dateField = dateField;
        this.report = report;
    }

    public ICalculationCache createCache() {
        return new AltCumulativeCalculationCache(dateField, report);
    }
}
