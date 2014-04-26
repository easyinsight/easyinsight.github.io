package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.DerivedAnalysisDateDimension;

/**
 * User: jamesboe
 * Date: 4/20/14
 * Time: 11:13 AM
 */
public class IntervalCacheBuilder implements ICacheBuilder {

    private AnalysisItem baseDate;
    private AnalysisMeasure measure;
    private DerivedAnalysisDateDimension date;
    private CalculationMetadata calculationMetadata;
    private AnalysisItem destination;

    public IntervalCacheBuilder(AnalysisItem destination,
                                AnalysisItem baseDate, AnalysisMeasure measure, DerivedAnalysisDateDimension intervalDate, CalculationMetadata calculationMetadata) {
        this.destination = destination;
        this.baseDate = baseDate;
        this.measure = measure;
        this.date = intervalDate;
        this.calculationMetadata = calculationMetadata;
    }

    public ICalculationCache createCache() {
        return new IntervalCalculationCache(destination, baseDate, measure, date, calculationMetadata);
    }
}
