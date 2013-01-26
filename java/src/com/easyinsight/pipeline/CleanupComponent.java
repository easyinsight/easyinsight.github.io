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

    //private int cleanupCriteria;

    private String pipelineName;
    private boolean keepFilters;

    public CleanupComponent(String pipelineName, boolean keepFilters) {
        this.pipelineName = pipelineName;
        this.keepFilters = keepFilters;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        AnalysisItemRetrievalStructure structure = new AnalysisItemRetrievalStructure(pipelineName);
        int i = 1;
        if (pipelineData.getInsightRequestMetadata() != null && pipelineData.getInsightRequestMetadata().getIntermediatePipelines() != null) {
            for (String pipe : pipelineData.getInsightRequestMetadata().getIntermediatePipelines()) {
                structure.getSections().add(i++, pipe);
            }
        }
        structure.setReport(pipelineData.getReport());
        structure.setInsightRequestMetadata(pipelineData.getInsightRequestMetadata());
        Set<AnalysisItem> allNeededAnalysisItems = new LinkedHashSet<AnalysisItem>();
        Set<AnalysisItem> allRequestedAnalysisItems = pipelineData.getAllRequestedItems();
        WSAnalysisDefinition report = pipelineData.getReport();
        if (report.retrieveFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : report.retrieveFilterDefinitions()) {
                if (filterDefinition.isEnabled()) {
                    if (structure.onOrAfter(filterDefinition.getPipelineName())) {
                        List<AnalysisItem> items = filterDefinition.getAnalysisItems(pipelineData.getAllItems(), allRequestedAnalysisItems, false, keepFilters, allNeededAnalysisItems, structure);
                        allNeededAnalysisItems.addAll(items);
                    }
                }
            }
        }
        for (AnalysisItem item : allRequestedAnalysisItems) {
            if (item.isValid()) {
                List<AnalysisItem> baseItems = item.getAnalysisItems(pipelineData.getAllItems(), allRequestedAnalysisItems, false, keepFilters, allNeededAnalysisItems, structure);
                allNeededAnalysisItems.addAll(baseItems);
                List<AnalysisItem> linkItems = item.addLinkItems(pipelineData.getAllItems());
                allNeededAnalysisItems.addAll(linkItems);
            }
        }
        KeyDisplayMapper mapper = KeyDisplayMapper.create(pipelineData.getAllItems());
        Map<String, List<AnalysisItem>> keyMap = mapper.getKeyMap();
        Map<String, List<AnalysisItem>> displayMap = mapper.getDisplayMap();
        if (report.getReportRunMarmotScript() != null) {
            StringTokenizer toker = new StringTokenizer(report.getReportRunMarmotScript(), "\r\n");
            while (toker.hasMoreTokens()) {
                String line = toker.nextToken();
                List<AnalysisItem> items = ReportCalculation.getAnalysisItems(line, pipelineData.getAllItems(), keyMap, displayMap, allRequestedAnalysisItems, false, keepFilters, structure);
                allNeededAnalysisItems.addAll(items);
            }
        }
        if (!pipelineName.equals(Pipeline.LAST)) {
            if (pipelineData.getUniqueItems() != null) {
                allNeededAnalysisItems.addAll(pipelineData.getUniqueItems().values());
            }
            allNeededAnalysisItems.addAll(pipelineData.getReport().getAdditionalGroupingItems());
        }
        pipelineData.setReportItems(allNeededAnalysisItems);
        return dataSet;
    }

    public void decorate(DataResults listDataResults) {
    }
}
