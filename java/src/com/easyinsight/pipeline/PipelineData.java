package com.easyinsight.pipeline;

import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.InsightRequestMetadata;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 2:41:44 PM
 */
public class PipelineData {
    private WSAnalysisDefinition report;
    private List<AnalysisItem> allItems;
    private Collection<AnalysisItem> reportItems;
    private InsightRequestMetadata insightRequestMetadata;
    private Map<String, String> dataSourceProperties;
    private Set<AnalysisItem> allRequestedItems;
    private Map<AnalysisItem, Integer> refMap;

    public PipelineData(WSAnalysisDefinition report, Collection<AnalysisItem> reportItems, InsightRequestMetadata insightRequestMetadata,
                        List<AnalysisItem> allItems, Map<String, String> dataSourceProperties, Set<AnalysisItem> allRequestedItems,
                        Map<AnalysisItem, Integer> refMap) {
        this.report = report;
        this.reportItems = reportItems;
        this.insightRequestMetadata = insightRequestMetadata;
        this.allItems = allItems;
        this.dataSourceProperties = dataSourceProperties;
        this.allRequestedItems = allRequestedItems;
        this.refMap = refMap;
    }

    public Map<AnalysisItem, Integer> getRefMap() {
        return refMap;
    }

    public void setRefMap(Map<AnalysisItem, Integer> refMap) {
        this.refMap = refMap;
    }

    public boolean decrementReferenceCount(AnalysisItem analysisItem) {
        Integer count = getRefMap().get(analysisItem);
        count--;
        getRefMap().put(analysisItem, count);
        return count == 0;
    }

    public Set<AnalysisItem> getAllRequestedItems() {
        return allRequestedItems;
    }

    public Map<String, String> getDataSourceProperties() {
        return dataSourceProperties;
    }

    public List<AnalysisItem> getAllItems() {
        return allItems;
    }

    public WSAnalysisDefinition getReport() {
        return report;
    }

    public void setReport(WSAnalysisDefinition report) {
        this.report = report;
    }

    public Collection<AnalysisItem> getReportItems() {
        return reportItems;
    }

    public void setReportItems(Collection<AnalysisItem> reportItems) {
        this.reportItems = reportItems;
    }

    public InsightRequestMetadata getInsightRequestMetadata() {
        return insightRequestMetadata;
    }

    public void setInsightRequestMetadata(InsightRequestMetadata insightRequestMetadata) {
        this.insightRequestMetadata = insightRequestMetadata;
    }
}
