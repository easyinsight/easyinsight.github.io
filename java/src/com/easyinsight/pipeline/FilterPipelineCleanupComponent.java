package com.easyinsight.pipeline;

import com.easyinsight.analysis.DataResults;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.FilterDefinition;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: May 28, 2009
 * Time: 9:05:37 AM
 */
public class FilterPipelineCleanupComponent implements IComponent {

    private boolean before;

    public FilterPipelineCleanupComponent(boolean before) {
        this.before = before;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        List<AnalysisItem> allRequestedAnalysisItems = new ArrayList<AnalysisItem>(pipelineData.getAllRequestedItems());
        if (pipelineData.getReport().retrieveFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : pipelineData.getReport().retrieveFilterDefinitions()) {
                if ((before && filterDefinition.isApplyBeforeAggregation()) || (!before && !filterDefinition.isApplyBeforeAggregation())) {
                    boolean itemFound = findItem(filterDefinition.getField(), allRequestedAnalysisItems, pipelineData.getAllItems());
                    if (!itemFound) {
                        pipelineData.getReportItems().remove(filterDefinition.getField());
                    }
                }
            }
        }
        return dataSet;
    }

    private boolean findItem(AnalysisItem field, Collection<AnalysisItem> allRequestedAnalysisItems, List<AnalysisItem> allFields) {
        int found = 0;
        for (AnalysisItem item : allRequestedAnalysisItems) {
            List<AnalysisItem> items = item.getAnalysisItems(allFields, allRequestedAnalysisItems, false, true);
            found += items.contains(field) ? 1 : 0;
        }
        return found > 0;
    }

    public void decorate(DataResults listDataResults) {
    }
}
