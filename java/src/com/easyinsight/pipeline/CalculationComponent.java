package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.calculations.*;
import com.easyinsight.calculations.generated.CalculationsLexer;
import com.easyinsight.calculations.generated.CalculationsParser;
import com.easyinsight.core.Key;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Nov 23, 2009
 * Time: 11:25:37 AM
 */
public class CalculationComponent implements IComponent {

    private AnalysisCalculation analysisCalculation;

    public CalculationComponent(AnalysisCalculation analysisCalculation) {
        this.analysisCalculation = analysisCalculation;
    }

    public AnalysisCalculation getAnalysisCalculation() {
        return analysisCalculation;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        if (dataSet.getRows().size() == 0) {
            return dataSet;
        }
        FieldCalculationLogic fieldCalculationLogic = new FieldCalculationLogic(analysisCalculation, dataSet);
        fieldCalculationLogic.calculate(analysisCalculation.getCalculationString(), pipelineData.getReport(), pipelineData.getAllItems());
        pipelineData.getReportItems().add(analysisCalculation);
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {

    }
}
