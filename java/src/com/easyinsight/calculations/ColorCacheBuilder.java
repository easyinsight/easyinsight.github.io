package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;

/**
 * User: jamesboe
 * Date: 7/21/14
 * Time: 10:16 AM
 */
public class ColorCacheBuilder implements ICacheBuilder {

    private AnalysisItem column;
    private CalculationMetadata calculationMetadata;

    public ColorCacheBuilder(AnalysisItem column, CalculationMetadata calculationMetadata) {
        this.column = column;
        this.calculationMetadata = calculationMetadata;
    }

    @Override
    public ICalculationCache createCache() {
        return new ColorCache(column, calculationMetadata);
    }
}
