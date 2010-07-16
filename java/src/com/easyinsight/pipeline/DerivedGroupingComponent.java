package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisCalculation;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.DerivedAnalysisDimension;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;

/**
 * User: jamesboe
 * Date: Nov 23, 2009
 * Time: 11:25:37 AM
 */
public class DerivedGroupingComponent implements IComponent {

    private DerivedAnalysisDimension dimension;

    public DerivedGroupingComponent(DerivedAnalysisDimension dimension) {
        this.dimension = dimension;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        for (IRow row : dataSet.getRows()) {
            Value value = dimension.calculate(row, pipelineData.getAllItems());
            row.addValue(dimension.createAggregateKey(), value);
        }
        pipelineData.getReportItems().add(dimension);
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {

    }
}