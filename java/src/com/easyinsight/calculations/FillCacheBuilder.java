package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisItem;

/**
 * User: jamesboe
 * Date: 9/11/12
 * Time: 2:22 PM
 */
public class FillCacheBuilder implements ICacheBuilder {

    private AnalysisItem instanceID;
    private AnalysisItem startField;
    private AnalysisItem endField;
    private CalculationMetadata calculationMetadata;
    private AnalysisDateDimension target;

    public FillCacheBuilder(AnalysisItem instanceID, AnalysisItem startField, AnalysisItem endField, CalculationMetadata calculationMetadata, AnalysisDateDimension target) {
        this.instanceID = instanceID;
        this.startField = startField;
        this.endField = endField;
        this.calculationMetadata = calculationMetadata;
        this.target = target;
    }

    public ICalculationCache createCache() {
        return new FillCalculationCache(instanceID, startField, endField, calculationMetadata, target);
    }
}
