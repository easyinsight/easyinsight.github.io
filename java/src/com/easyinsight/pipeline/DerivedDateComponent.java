package com.easyinsight.pipeline;

import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.DerivedAnalysisDateDimension;
import com.easyinsight.analysis.DerivedAnalysisDimension;
import com.easyinsight.analysis.IRow;
import com.easyinsight.calculations.CalculationMetadata;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

/**
 * User: jamesboe
 * Date: Nov 23, 2009
 * Time: 11:25:37 AM
 */
public class DerivedDateComponent implements IComponent {

    private DerivedAnalysisDateDimension dimension;

    public DerivedDateComponent(DerivedAnalysisDateDimension dimension) {
        this.dimension = dimension;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        CalculationMetadata calculationMetadata = new CalculationMetadata();
        calculationMetadata.setReport(pipelineData.getReport());
        for (IRow row : dataSet.getRows()) {
            Value value = dimension.calculate(row, pipelineData.getAllItems(), calculationMetadata);
            row.addValue(dimension.createAggregateKey(), value);
        }
        pipelineData.getReportItems().add(dimension);
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {

    }
}