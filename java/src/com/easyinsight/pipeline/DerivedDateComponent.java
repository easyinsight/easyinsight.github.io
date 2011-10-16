package com.easyinsight.pipeline;

import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.DerivedAnalysisDateDimension;
import com.easyinsight.analysis.DerivedAnalysisDimension;
import com.easyinsight.analysis.IRow;
import com.easyinsight.calculations.CalculationMetadata;
import com.easyinsight.calculations.FieldCalculationLogic;
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
        if (dataSet.getRows().size() == 0) {
            return dataSet;
        }
        FieldCalculationLogic fieldCalculationLogic = new FieldCalculationLogic(dimension, dataSet);
        fieldCalculationLogic.calculate(dimension.getDerivationCode(), pipelineData.getReport(), pipelineData.getAllItems());
        pipelineData.getReportItems().add(dimension);
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {

    }
}