package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisCalculation;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

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

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        for (IRow row : dataSet.getRows()) {
            Value value = analysisCalculation.calculate(dataSet, row);
            row.addValue(analysisCalculation.createAggregateKey(), value);
        }
        pipelineData.getReportItems().add(analysisCalculation);
        return dataSet;
    }

    public void decorate(ListDataResults listDataResults) {

    }
}
