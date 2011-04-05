package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisStep;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.IRow;
import com.easyinsight.dataset.DataSet;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 3:18:51 PM
 */
public class StepTransformComponent implements IComponent {

    private AnalysisStep analysisStep;

    public StepTransformComponent(AnalysisStep analysisStep) {
        this.analysisStep = analysisStep;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        for (IRow row : dataSet.getRows()) {
            row.addValue(analysisStep.createAggregateKey(), analysisStep.transformValue(row.getValue(analysisStep.createAggregateKey()), pipelineData.getInsightRequestMetadata(), false));
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}