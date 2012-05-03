package com.easyinsight.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;

import java.util.*;

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
        AnalysisItemRetrievalStructure structure = new AnalysisItemRetrievalStructure();
        structure.setReport(pipelineData.getReport());
        Set<AnalysisItem> allNeededAnalysisItems = new LinkedHashSet<AnalysisItem>();
        Set<AnalysisItem> allRequestedAnalysisItems = pipelineData.getAllRequestedItems();
        WSAnalysisDefinition report = pipelineData.getReport();
        if (report.retrieveFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : report.retrieveFilterDefinitions()) {
                if (filterDefinition.isEnabled() && !filterDefinition.isApplyBeforeAggregation()) {
                    List<AnalysisItem> items = filterDefinition.getAnalysisItems(pipelineData.getAllItems(), allRequestedAnalysisItems, false, keepFilters, cleanupCriteria, allNeededAnalysisItems, structure);
                    allNeededAnalysisItems.addAll(items);
                }
            }
        }
        for (AnalysisItem item : allRequestedAnalysisItems) {
            if (item.isValid()) {
                List<AnalysisItem> baseItems = item.getAnalysisItems(pipelineData.getAllItems(), allRequestedAnalysisItems, false, keepFilters, cleanupCriteria, allNeededAnalysisItems, structure);
                allNeededAnalysisItems.addAll(baseItems);
                List<AnalysisItem> linkItems = item.addLinkItems(pipelineData.getAllItems());
                allNeededAnalysisItems.addAll(linkItems);
            }
        }
        Map<String, List<AnalysisItem>> keyMap = new HashMap<String, List<AnalysisItem>>();
        Map<String, List<AnalysisItem>> displayMap = new HashMap<String, List<AnalysisItem>>();
        for (AnalysisItem analysisItem : pipelineData.getAllItems()) {
            List<AnalysisItem> items = keyMap.get(analysisItem.getKey().toKeyString());
            if (items == null) {
                items = new ArrayList<AnalysisItem>(1);
                keyMap.put(analysisItem.getKey().toKeyString(), items);
            }
            items.add(analysisItem);
        }
        for (AnalysisItem analysisItem : pipelineData.getAllItems()) {
            List<AnalysisItem> items = displayMap.get(analysisItem.toDisplay());
            if (items == null) {
                items = new ArrayList<AnalysisItem>(1);
                displayMap.put(analysisItem.toDisplay(), items);
            }
            items.add(analysisItem);
        }
        if (report.getReportRunMarmotScript() != null) {
            StringTokenizer toker = new StringTokenizer(report.getReportRunMarmotScript(), "\r\n");
            while (toker.hasMoreTokens()) {
                String line = toker.nextToken();
                List<AnalysisItem> items = ReportCalculation.getAnalysisItems(line, pipelineData.getAllItems(), keyMap, displayMap, allRequestedAnalysisItems, false, keepFilters, AGGREGATE_CALCULATIONS);
                allNeededAnalysisItems.addAll(items);
            }
        }
        if (cleanupCriteria == AGGREGATE_CALCULATIONS) {
            if (pipelineData.getUniqueItems() != null) {
                allNeededAnalysisItems.addAll(pipelineData.getUniqueItems().values());
            }
        }
        pipelineData.setReportItems(allNeededAnalysisItems);
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
