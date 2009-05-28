package com.easyinsight.pipeline;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.FilterDefinition;

import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: May 28, 2009
 * Time: 9:05:37 AM
 */
public class FilterPipelineCleanupComponent implements IComponent {
    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        List<AnalysisItem> allRequestedAnalysisItems = new ArrayList<AnalysisItem>(pipelineData.getReport().getAllAnalysisItems());
        if (pipelineData.getReport().getFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : pipelineData.getReport().getFilterDefinitions()) {
                if (!allRequestedAnalysisItems.contains(filterDefinition.getField())) {
                    pipelineData.getReportItems().remove(filterDefinition.getField());
                }
            }
        }
        return dataSet;
    }

    public void decorate(ListDataResults listDataResults) {
    }
}
