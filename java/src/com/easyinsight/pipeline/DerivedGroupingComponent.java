package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.calculations.*;
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
        if (dataSet.getRows().size() == 0) {
            return dataSet;
        }
        if (dataSet.getRows().size() == 0) {
            return dataSet;
        }
        FieldCalculationLogic fieldCalculationLogic = new FieldCalculationLogic(dimension, dataSet);
        fieldCalculationLogic.calculate(dimension.getDerivationCode(), pipelineData.getReport(), pipelineData.getAllItems(), pipelineData.getInsightRequestMetadata());
        pipelineData.getReportItems().add(dimension);
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {

    }
}
