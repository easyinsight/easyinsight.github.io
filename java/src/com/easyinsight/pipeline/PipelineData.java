package com.easyinsight.pipeline;

import com.easyinsight.analysis.WSAnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.InsightRequestMetadata;

import java.util.Collection;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 2:41:44 PM
 */
public class PipelineData {
    private WSAnalysisDefinition report;
    private Collection<AnalysisItem> reportItems;
    private InsightRequestMetadata insightRequestMetadata;

    public PipelineData(WSAnalysisDefinition report, Collection<AnalysisItem> reportItems, InsightRequestMetadata insightRequestMetadata) {
        this.report = report;
        this.reportItems = reportItems;
        this.insightRequestMetadata = insightRequestMetadata;
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
