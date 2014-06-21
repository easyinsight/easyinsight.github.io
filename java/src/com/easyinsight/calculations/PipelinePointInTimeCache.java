package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.PipelinePointInTime;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 9/11/12
 * Time: 2:16 PM
 */
public class PipelinePointInTimeCache implements ICalculationCache {
    private AnalysisItem instanceID;
    private AnalysisItem stageField;
    private AnalysisDateDimension dateField;
    private CalculationMetadata calculationMetadata;
    private AnalysisItem target;
    private List<String> lastStage;

    private PipelinePointInTime pipelinePointInTime;

    public AnalysisItem getInstanceID() {
        return instanceID;
    }

    public AnalysisItem getStageField() {
        return stageField;
    }

    public AnalysisDateDimension getDateField() {
        return dateField;
    }

    public PipelinePointInTimeCache(AnalysisItem instanceID, AnalysisItem stageField, AnalysisDateDimension dateField, CalculationMetadata calculationMetadata,
                                    AnalysisItem target, List<String> lastStage) {
        this.instanceID = instanceID;
        this.stageField = stageField;
        this.dateField = dateField;
        this.calculationMetadata = calculationMetadata;
        this.target = target;
        this.lastStage = lastStage;
    }

    public void fromDataSet(DataSet dataSet) {
        pipelinePointInTime = new PipelinePointInTime(instanceID, stageField, dateField, target, lastStage);
        pipelinePointInTime.blah(calculationMetadata.getReport(), dataSet, calculationMetadata.getInsightRequestMetadata(), calculationMetadata.getConnection());
        calculationMetadata.getGeneratedComponents().add(pipelinePointInTime);
    }
}
