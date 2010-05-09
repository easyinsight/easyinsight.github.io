package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.dataset.DataSet;

import java.util.ArrayList;

/**
 * User: James Boe
 * Date: May 28, 2009
 * Time: 9:05:37 AM
 */
public class MeasureFilterPipelineCleanupComponent implements IComponent {
    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        for (AnalysisItem analysisItem : new ArrayList<AnalysisItem>(pipelineData.getReportItems())) {
            if (analysisItem.getFilters().size() > 0) {
                for (FilterDefinition filterDefinition : analysisItem.getFilters()) {
                    if (pipelineData.decrementReferenceCount(filterDefinition.getField())) {
                        pipelineData.getReportItems().remove(filterDefinition.getField());
                    }
                }
            }
        }
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}