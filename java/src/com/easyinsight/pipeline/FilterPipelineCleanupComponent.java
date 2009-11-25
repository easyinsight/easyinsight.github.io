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
                if (filterDefinition.isApplyBeforeAggregation()) {
                    boolean itemFound = findItem(filterDefinition.getField(), allRequestedAnalysisItems, pipelineData.getAllItems());
                    if (!itemFound) {
                        pipelineData.getReportItems().remove(filterDefinition.getField());
                    }
                }
            }
        }
        return dataSet;
    }

    private boolean findItem(AnalysisItem field, List<AnalysisItem> allRequestedAnalysisItems, List<AnalysisItem> allItems) {
        boolean found = false;
        for (AnalysisItem item : allRequestedAnalysisItems) {
            List<AnalysisItem> items = item.getAnalysisItems(allItems, allRequestedAnalysisItems, false);
            found = items.contains(field);
            if (found) {
                break;
            }
        }
        return found;
    }

    public void decorate(ListDataResults listDataResults) {
    }
}
