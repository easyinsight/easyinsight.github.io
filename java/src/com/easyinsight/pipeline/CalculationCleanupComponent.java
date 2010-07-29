package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisCalculation;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.dataset.DataSet;

import java.util.List;

/**
 * User: jamesboe
 * Date: Jul 21, 2010
 * Time: 10:11:07 AM
 */
public class CalculationCleanupComponent implements IComponent {

    private AnalysisCalculation analysisCalculation;

    public CalculationCleanupComponent(AnalysisCalculation analysisCalculation) {
        this.analysisCalculation = analysisCalculation;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        List<AnalysisItem> items = analysisCalculation.getAnalysisItems(pipelineData.getAllItems(),
                pipelineData.getReportItems(), false, true, false);
        for (AnalysisItem item : items) {
            if (item != analysisCalculation) {
                if (pipelineData.decrementReferenceCount(item)) {
                    pipelineData.getReportItems().remove(item);
                }
            }
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
