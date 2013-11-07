package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisMeasure;

/**
 * User: jamesboe
 * Date: 9/11/12
 * Time: 2:22 PM
 */
public class CumulativeCacheBuilder implements ICacheBuilder {

    private AnalysisItem dateField;
    private AnalysisMeasure measureField;
    private int interval;

    public CumulativeCacheBuilder(AnalysisItem dateField, AnalysisMeasure measureField, int interval) {
        this.dateField = dateField;
        this.measureField = measureField;
        this.interval = interval;
    }

    public ICalculationCache createCache() {
        return new CumulativeCalculationCache(dateField, measureField, interval);
    }
}
