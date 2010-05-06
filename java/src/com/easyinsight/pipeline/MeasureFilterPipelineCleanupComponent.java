package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.dataset.DataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * User: James Boe
 * Date: May 28, 2009
 * Time: 9:05:37 AM
 */
public class MeasureFilterPipelineCleanupComponent implements IComponent {
    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        List<AnalysisItem> allRequestedAnalysisItems = new ArrayList<AnalysisItem>(pipelineData.getAllRequestedItems());
        for (AnalysisItem analysisItem : new ArrayList<AnalysisItem>(pipelineData.getReportItems())) {
            if (analysisItem.getFilters().size() > 0) {
                for (FilterDefinition filterDefinition : analysisItem.getFilters()) {
                    boolean itemFound = findItem(filterDefinition.getField(), allRequestedAnalysisItems, pipelineData.getAllItems());
                    if (!itemFound) {
                        pipelineData.getReportItems().remove(filterDefinition.getField());
                    }
                }
            }
        }
        return dataSet;
    }

    private boolean findItem(AnalysisItem field, List<AnalysisItem> allRequestedAnalysisItems, List<AnalysisItem> allFields) {
        int found = 0;
        for (AnalysisItem item : allRequestedAnalysisItems) {
            List<AnalysisItem> items = item.getAnalysisItems(allFields, new ArrayList<AnalysisItem>(), false, false);
            found += items.contains(field) ? 1 : 0;
        }
        return found > 0;
    }

    public void decorate(DataResults listDataResults) {
    }
}