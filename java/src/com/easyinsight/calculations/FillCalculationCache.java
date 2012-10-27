package com.easyinsight.calculations;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.pipeline.StepCorrelationComponent;
import com.easyinsight.pipeline.StepTransformComponent;

import java.util.*;

/**
 * User: jamesboe
 * Date: 9/11/12
 * Time: 2:16 PM
 */
public class FillCalculationCache implements ICalculationCache {
    private AnalysisItem instanceID;
    private AnalysisItem startField;
    private AnalysisItem endField;
    private CalculationMetadata calculationMetadata;
    private AnalysisDateDimension target;

    public FillCalculationCache(AnalysisItem instanceID, AnalysisItem startField, AnalysisItem endField, CalculationMetadata calculationMetadata, AnalysisDateDimension target) {
        this.instanceID = instanceID;
        this.startField = startField;
        this.endField = endField;
        this.calculationMetadata = calculationMetadata;
        this.target = target;
    }

    public void fromDataSet(DataSet dataSet) {
        AnalysisStep step = new AnalysisStep();
        step.setDateLevel(target.getDateLevel());
        step.setCorrelationDimension((AnalysisDimension) instanceID);
        step.setStartDate((AnalysisDateDimension) startField);
        step.setEndDate((AnalysisDateDimension) endField);
        calculationMetadata.getGeneratedComponents().add(new StepCorrelationComponent(step, target));
        calculationMetadata.getGeneratedComponents().add(new StepTransformComponent(step, target));

    }
}
