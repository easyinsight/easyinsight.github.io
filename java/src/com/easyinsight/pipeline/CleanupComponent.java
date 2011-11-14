package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * User: jamesboe
 * Date: 1/25/11
 * Time: 11:34 AM
 */
public class CleanupComponent implements IComponent {

    public static final int AGGREGATE_CALCULATIONS = 1;

    private int cleanupCriteria;

    private boolean keepFilters;

    public CleanupComponent(int cleanupCriteria, boolean keepFilters) {
        this.cleanupCriteria = cleanupCriteria;
        this.keepFilters = keepFilters;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        Set<AnalysisItem> allNeededAnalysisItems = new LinkedHashSet<AnalysisItem>();
        Set<AnalysisItem> allRequestedAnalysisItems = pipelineData.getAllRequestedItems();
        WSAnalysisDefinition report = pipelineData.getReport();
        if (report.retrieveFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : report.retrieveFilterDefinitions()) {
                if (filterDefinition.isEnabled() && !filterDefinition.isApplyBeforeAggregation()) {
                    List<AnalysisItem> items = filterDefinition.getAnalysisItems(pipelineData.getAllItems(), allRequestedAnalysisItems, false, keepFilters, cleanupCriteria);
                    allNeededAnalysisItems.addAll(items);
                }
            }
        }
        for (AnalysisItem item : allRequestedAnalysisItems) {
            if (item.isValid()) {
                List<AnalysisItem> baseItems = item.getAnalysisItems(pipelineData.getAllItems(), allRequestedAnalysisItems, false, keepFilters, cleanupCriteria);
                allNeededAnalysisItems.addAll(baseItems);
                List<AnalysisItem> linkItems = item.addLinkItems(pipelineData.getAllItems());
                allNeededAnalysisItems.addAll(linkItems);
            }
        }
        if (report.getReportRunMarmotScript() != null) {
            StringTokenizer toker = new StringTokenizer(report.getReportRunMarmotScript(), "\r\n");
            while (toker.hasMoreTokens()) {
                String line = toker.nextToken();
                List<AnalysisItem> items = ReportCalculation.getAnalysisItems(line, pipelineData.getAllItems(), allRequestedAnalysisItems, false, keepFilters, AGGREGATE_CALCULATIONS);
                allNeededAnalysisItems.addAll(items);
            }
        }
        pipelineData.setReportItems(allNeededAnalysisItems);
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
