package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
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
    private AnalysisItem target;

    public StepTransformComponent(AnalysisStep analysisStep, AnalysisItem target) {
        this.analysisStep = analysisStep;
        this.target = target;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        for (IRow row : dataSet.getRows()) {
            if (target == null) {
                row.addValue(analysisStep.createAggregateKey(), analysisStep.transformValue(row.getValue(analysisStep.createAggregateKey()), pipelineData.getInsightRequestMetadata(), false));
            } else {
                row.addValue(target.createAggregateKey(), target.transformValue(row.getValue(target.createAggregateKey()), pipelineData.getInsightRequestMetadata(), false));
            }
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}