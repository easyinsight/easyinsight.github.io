package com.easyinsight.pipeline;

import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.DerivedAnalysisDateDimension;
import com.easyinsight.calculations.FieldCalculationLogic;
import com.easyinsight.dataset.DataSet;

import java.util.List;

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
        fieldCalculationLogic.calculate(dimension.getDerivationCode(), pipelineData.getReport(), pipelineData.getAllItems(), pipelineData.getInsightRequestMetadata());
        pipelineData.getReportItems().add(dimension);
        List<IComponent> components = fieldCalculationLogic.getGeneratedComponents();
        for (IComponent component : components) {
            dataSet = component.apply(dataSet, pipelineData);
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {

    }
}