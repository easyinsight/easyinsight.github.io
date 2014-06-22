package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisItem;

import java.util.List;

/**
 * User: jamesboe
 * Date: 9/11/12
 * Time: 2:22 PM
 */
public class PipelinePointInTimeBuilder implements ICacheBuilder {

    private AnalysisItem instanceID;
    private AnalysisItem stageField;
    private AnalysisDateDimension dateField;
    private CalculationMetadata calculationMetadata;
    private AnalysisItem target;
    private List<String> lastStage;

    public PipelinePointInTimeBuilder(AnalysisItem instanceID, AnalysisItem stageField, AnalysisDateDimension dateField, CalculationMetadata calculationMetadata,
                                      AnalysisItem target, List<String> lastStage) {
        this.instanceID = instanceID;
        this.stageField = stageField;
        this.dateField = dateField;
        this.calculationMetadata = calculationMetadata;
        this.target = target;
        this.lastStage = lastStage;
    }

    public ICalculationCache createCache() {
        return new PipelinePointInTimeCache(instanceID, stageField, dateField, calculationMetadata, target, lastStage);
    }
}
