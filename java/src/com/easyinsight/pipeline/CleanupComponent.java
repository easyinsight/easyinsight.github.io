package com.easyinsight.pipeline;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DataResults;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.dataset.DataSet;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * User: jamesboe
 * Date: 1/25/11
 * Time: 11:34 AM
 */
public class CleanupComponent implements IComponent {

    public static final int AGGREGATE_CALCULATIONS = 1;

    private int cleanupCriteria;

    public CleanupComponent(int cleanupCriteria) {
        this.cleanupCriteria = cleanupCriteria;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        Set<AnalysisItem> allNeededAnalysisItems = new LinkedHashSet<AnalysisItem>();
        Set<AnalysisItem> allRequestedAnalysisItems = pipelineData.getAllRequestedItems();
        WSAnalysisDefinition report = pipelineData.getReport();
        if (report.retrieveFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : report.retrieveFilterDefinitions()) {
                if (filterDefinition.isEnabled() && !filterDefinition.isApplyBeforeAggregation()) {
                    List<AnalysisItem> items = filterDefinition.getAnalysisItems(pipelineData.getAllItems(), allRequestedAnalysisItems, false, false, cleanupCriteria);
                    allNeededAnalysisItems.addAll(items);
                }
            }
        }
        for (AnalysisItem item : allRequestedAnalysisItems) {
            if (item.isValid()) {
                List<AnalysisItem> baseItems = item.getAnalysisItems(pipelineData.getAllItems(), allRequestedAnalysisItems, false, false, true, cleanupCriteria);
                allNeededAnalysisItems.addAll(baseItems);
                List<AnalysisItem> linkItems = item.addLinkItems(pipelineData.getAllItems(), allRequestedAnalysisItems);
                allNeededAnalysisItems.addAll(linkItems);
            }
        }
        pipelineData.setReportItems(allNeededAnalysisItems);
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
